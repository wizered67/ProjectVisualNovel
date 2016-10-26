package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.wizered67.game.Screens.GameScreen;
import com.wizered67.game.Screens.LoadingScreen;

public class MainGame extends Game {
	GameScreen gameScreen;
	AssetManager assetManager;
    MusicManager musicManager;
	@Override
	public void create() {
		Gdx.app.log("TEST", "TEST");
		assetManager = new AssetManager();
        musicManager = new MusicManager();
		GameManager.init(this);
		gameScreen = new GameScreen();
        setScreen(new LoadingScreen(assetManager, gameScreen));
	}
	
}
