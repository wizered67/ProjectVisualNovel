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
	//Tile Settings
	public final static int TILE_SIZE = 32;
	//Physics Settings
	public final static int PPM = 32;
	public final static float TIME_STEP = 1 / 60f;
	public final static int POSITION_ITERATIONS = 2; //2
	public final static int VELOCITY_ITERATIONS = 6; //6
	public final static float MAX_VELOCITY = 4f; //5
	public final static float VELOCITY_THRESHOLD = 0.1f;
	public final static Vector2 DEFAULT_GRAVITY_VECTOR = new Vector2(0, 0);
	public final static float VIRTUAL_WIDTH = 400; //400
	public final static float VIRTUAL_HEIGHT = 240; //240
	public final static short CATEGORY_PLAYER = 0x0001;  
	public final static short CATEGORY_ENEMY = 0x0002; 
	public final static short CATEGORY_SCENERY = 0x0004;
	public final static short CATEGORY_PLAYER_ATTACK = 0x0008;
	public final static short CATEGORY_ENEMY_ATTACK = 0x0016;
	public final static short CATEGORY_LIGHT = 0x0032;
	public final static short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_SCENERY | CATEGORY_ENEMY_ATTACK;
	public final static short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_SCENERY | CATEGORY_PLAYER_ATTACK;
	public final static short MASK_SCENERY = CATEGORY_PLAYER | CATEGORY_SCENERY | CATEGORY_ENEMY | CATEGORY_ENEMY_ATTACK | CATEGORY_PLAYER_ATTACK;
	public final static short MASK_PLAYER_ATTACK = CATEGORY_ENEMY | CATEGORY_SCENERY | CATEGORY_ENEMY_ATTACK;
	public final static short MASK_ENEMY_ATTACK = CATEGORY_PLAYER | CATEGORY_SCENERY | CATEGORY_PLAYER_ATTACK;
	public final static short MASK_LIGHT = CATEGORY_SCENERY;

	public static boolean isRectangle(Object c){
		return c.getClass().equals(Rectangle.class);
	}

	//Physics Utility Functions
	public static float toPixels(float meters){
		return meters * PPM;
	}
	
	public static float toMeters(float pixels){
		return pixels / PPM;
	}
	
	public static Vector2 toPixels(Vector2 metersVector){
		return metersVector.cpy().scl(PPM);
	}

	public static Vector2 toMeters(Vector2 pixelsVector){
		return pixelsVector.cpy().scl(1f / PPM);
	}

	public static Vector3 toPixels(Vector3 metersVector){
		return metersVector.cpy().scl(PPM);
	}

	public static Vector3 toMeters(Vector3 pixelsVector){
		return pixelsVector.cpy().scl(1f / PPM);
	}

	public static float toBox2DAngle(float angle){
		return (float)(angle + Math.PI / 2);
	}

	public static float toBox2DAngle(double angle){
		return toBox2DAngle((float) angle);
	}
	
}
