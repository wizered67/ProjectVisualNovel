package com.wizered67.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wizered67.game.Constants;
import com.wizered67.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Constants.GAME_NAME;
		config.width = Constants.DEFAULT_WIDTH; //16 : 9 //1024
		config.height = Constants.DEFAULT_HEIGHT; //576
		config.resizable = Constants.RESIZABLE;
		//config.fullscreen = true;
		//config.foregroundFPS = 5;
		//config.vSyncEnabled = true;
		new LwjglApplication(new MainGame(), config);
	}
}
