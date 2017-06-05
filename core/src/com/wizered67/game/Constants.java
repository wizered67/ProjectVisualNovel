package com.wizered67.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.rafaskoberg.gdx.typinglabel.TypingConfig;

public class Constants {
	//Debug Settings
	public final static boolean DEBUG = false;

	//Game Settings
	public final static String GAME_NAME = "Visual Novel";
	public final static int DEFAULT_WIDTH = 800;//1920;
	public final static int DEFAULT_HEIGHT = 600;//1080;

	public final static float VIRTUAL_WIDTH = 800; //400
	public final static float VIRTUAL_HEIGHT = 600; //240

	public final static float MAX_VIEWPORT_WORLD_WIDTH = 0;
	public final static float MAX_VIEWPORT_WORLD_HEIGHT = 0;

	public final static boolean FULLSCREEN = false;
	public final static boolean RESIZABLE = true;
	public final static int REGULAR_FONT_SIZE = Gdx.app.getType() == Application.ApplicationType.Android ? (36) : 24;

	//Conversation Settings
	public static void initTextboxSettings() {
		TypingConfig.CHAR_LIMIT_PER_FRAME = 5;
		TypingConfig.DEFAULT_CLEAR_COLOR = Color.WHITE;
		TypingConfig.FORCE_COLOR_MARKUP_BY_DEFAULT = true;
		TypingConfig.INTERVAL_MULTIPLIERS_BY_CHAR.put('\n', 0);
	}
	
}
