package com.wizered67.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.inputs.MyInputProcessor;
import com.wizered67.game.assets.Assets;

public class GameManager {
	public static MainGame game;
	public static void init(MainGame g){
		game = g;
		/* //TODO remove this entirely
		GroovyTest groovy = new GroovyTest();
		groovy.init();
		*/

	}

	public static MyInputProcessor getMainInputProcessor() {
		return game.mainInputProcessor;
	}

	public static Assets assetManager() {
		return game.assetManager;
	}

	public static SpriteBatch mainBatch() {
		return game.mainBatch;
	}

	public static OrthographicCamera mainCamera() {
		return game.mainCamera;
	}

	public static Viewport mainViewport() {
		return game.mainViewport;
	}

	public static Viewport guiViewport() {
		return game.guiViewport;
	}

	public static ConversationController conversationController() {
		return game.conversationController;
	}

	public static GUIManager guiManager() {
		return game.guiManager;
	}

	public static void error(String error) {
		Gdx.app.error("ERROR", error);
	}

    public static MusicManager musicManager() {
        return game.musicManager;
    }

}
