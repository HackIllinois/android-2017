package org.hackillinois.android.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.hackillinois.android.App;
import org.hackillinois.android.api.HackIllinoisAPI;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	public HackIllinoisAPI getApi() {
		return ((App) getApplication()).getApi();
	}
}
