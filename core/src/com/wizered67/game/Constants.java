package com.wizered67.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Constants {
	//Debug Settings
	public final static boolean DEBUG = false;

	//todo fix
	public final static boolean LOAD = false;

	//Game Settings
	public final static String GAME_NAME = "Visual Novel";
	public final static int DEFAULT_WIDTH = 800; //1024
	public final static int DEFAULT_HEIGHT = 600; //576
	public final static boolean RESIZABLE = true;
	public final static int REGULAR_FONT_SIZE = Gdx.app.getType() == Application.ApplicationType.Android ? (20) : 36;
	public final static float VIRTUAL_WIDTH = 400; //400
	public final static float VIRTUAL_HEIGHT = 240; //240
	//Tile Settings
	public final static int TILE_SIZE = 32;
	
}
