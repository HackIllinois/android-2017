package org.hackillinois.android;

import android.support.multidex.MultiDexApplication;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.readystatesoftware.chuck.ChuckInterceptor;

import net.danlew.android.joda.JodaTimeAndroid;

import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.helper.Settings;
import org.hackillinois.android.helper.Utils;

import io.palaima.debugdrawer.timber.data.LumberYard;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static org.hackillinois.android.api.HackIllinoisAPI.SERVER_ADDRESS;

public class App extends MultiDexApplication {
	private final Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
	private HackIllinoisAPI api;
	private Retrofit retrofit;
	private OkHttpClient okHttpClient;
	private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
		Response originalResponse = chain.proceed(chain.request());
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			int maxAge = 60 * 5; // read from cache for 5 minute
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		} else {
			int maxStale = 60 * 60 * 24 * 7; // tolerate 1-week stale
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					.build();
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setFontAttrId(R.attr.fontPath)
				.build()
		);

		JodaTimeAndroid.init(this);

		Iconics.init(this);
		Iconics.registerFont(new GoogleMaterial());

		Settings.initialize(this);

		LumberYard lumberYard = LumberYard.getInstance(this);
		lumberYard.cleanUp();
		Timber.plant(lumberYard.tree());
		Timber.plant(new Timber.DebugTree());
	}

	public Gson getGson() {
		return gson;
	}

	public OkHttpClient getOkHttp() {
		if (okHttpClient == null) {
			int cacheSize = 10 * 1024 * 1024; // 10 MB
			Cache cache = new Cache(getCacheDir(), cacheSize);

			OkHttpClient.Builder builder = new OkHttpClient.Builder()
					.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
			if (BuildConfig.DEBUG) {
				builder.addNetworkInterceptor(new ChuckInterceptor(this).showNotification(false));
			}
			okHttpClient = builder.cache(cache)
					.build();
		}

		return okHttpClient;
	}

	public Retrofit getRetrofit() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(SERVER_ADDRESS)
					.addConverterFactory(GsonConverterFactory.create(getGson()))
					.client(getOkHttp())
					.build();
		}

		return retrofit;
	}

	public HackIllinoisAPI getApi() {
		if (api == null) {
			api = getRetrofit().create(HackIllinoisAPI.class);
		}

		return api;
	}
}
