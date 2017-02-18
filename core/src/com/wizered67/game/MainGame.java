package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.wizered67.game.Screens.LoadingScreen;
import com.wizered67.game.Screens.MainGameScreen;
import com.wizered67.game.assets.Assets;

public class MainGame extends Game {
	MainGameScreen gameScreen;
	Assets assetManager;
    MusicManager musicManager;
	@Override
	public void create() {
		//Gdx.app.log("TEST", "TEST");
		assetManager = new Assets();
        musicManager = new MusicManager();
		GameManager.init(this);
		gameScreen = new MainGameScreen();
        setScreen(new LoadingScreen(gameScreen));
	}
	
}
