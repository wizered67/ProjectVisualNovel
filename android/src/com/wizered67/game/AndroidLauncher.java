package com.wizered67.game;

import android.os.Bundle;

import android.support.multidex.MultiDex;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		//Gdx.app.log("Test", "Loading test.");
		super.onCreate(savedInstanceState);
		MultiDex.install(getContext());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainGame(), config);
	}
}
