package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.wizered67.game.Inputs.MyInputProcessor;
import com.wizered67.game.Screens.GameScreen;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
	public static MainGame game;
	private static MyInputProcessor inputProcessor;
	public static void init(MainGame g){
		game = g;
	}
	public static GameScreen getGameScreen(){
		if (game.getScreen() instanceof GameScreen){
			return (GameScreen) game.getScreen();
		}
		else
			return null;
	}
	public static void setMainInputProcessor(MyInputProcessor ip) {
		inputProcessor = ip;
	}

	public static MyInputProcessor getMainInputProcessor() {
		return inputProcessor;
	}

	public static AssetManager assetManager() {
		return game.assetManager;
	}

    public static Map<String, Map<String, Animation>> loadedAnimations() {
        return game.loadedAnimations;
    }

    public static MusicManager musicManager() {
        return game.musicManager;
    }
}
