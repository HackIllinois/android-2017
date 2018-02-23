package org.hackillinois.android.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.hackillinois.android.App;
import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.api.response.APIErrorResonse;
import org.hackillinois.android.helper.Utils;

import java.io.IOException;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.base.DebugModule;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.logs.LogsModule;
import io.palaima.debugdrawer.okhttp3.OkHttp3Module;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import io.palaima.debugdrawer.timber.TimberModule;
import okhttp3.ResponseBody;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
		super.onCreate(savedInstanceState);
	}

	public void enableDebug(DebugModule... debugModules) {
		DebugModule[] activeModules;
		if (debugModules.length != 0) {
			activeModules = Utils.concat(debugModules, getDefaultDebugModules());
		} else {
			activeModules = getDefaultDebugModules();
		}
		new DebugDrawer.Builder(this)
				.modules(activeModules)
				.build();
	}

	@NonNull
	private DebugModule[] getDefaultDebugModules() {
		return new DebugModule[]{
				new ScalpelModule(this),
				new TimberModule(),
				new OkHttp3Module(getApp().getOkHttp()),
				new LogsModule(),
				new BuildModule(),
				new DeviceModule()
		};
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	public HackIllinoisAPI getApi() {
		return App.getApi();
	}

	public App getApp() {
		return (App) getApplication();
	}

	public void reportIfError(ResponseBody errorBody) {
		if (errorBody != null) {
			try {
				APIErrorResonse error = App.getGson().fromJson(errorBody.string(), APIErrorResonse.class);
				Toast.makeText(getApplicationContext(), "Failed because " + error.getError().getMessage(), Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Timber.wtf(e, "Failed to display error");
			}
		}
	}
}
