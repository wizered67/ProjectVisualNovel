package com.wizered67.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.inputs.MyInputProcessor;
import com.wizered67.game.assets.Assets;

public class GameManager {
	public static MainGame game;
	private static MyInputProcessor inputProcessor;
	public static void init(MainGame g){
		game = g;
		/* //TODO remove this entirely
		GroovyTest groovy = new GroovyTest();
		groovy.init();
		*/

	}

	public static void setMainInputProcessor(MyInputProcessor ip) {
		inputProcessor = ip;
	}

	public static MyInputProcessor getMainInputProcessor() {
		return inputProcessor;
	}

	public static Assets assetManager() {
		return game.assetManager;
	}

	public static SpriteBatch mainBatch() {
		return game.mainBatch;
	}

	public static void error(String error) {
		Gdx.app.error("ERROR", error);
	}

    public static MusicManager musicManager() {
        return game.musicManager;
    }

}
