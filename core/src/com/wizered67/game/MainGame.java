package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.saving.SaveManager;
import com.wizered67.game.screens.LoadingScreen;
import com.wizered67.game.screens.MainGameScreen;
import com.wizered67.game.assets.Assets;

public class MainGame extends Game {
	MainGameScreen gameScreen;
	Assets assetManager;
    MusicManager musicManager;
    SpriteBatch mainBatch;
	@Override
	public void create() {
		assetManager = new Assets();
        musicManager = new MusicManager();
        mainBatch = new SpriteBatch();
		GameManager.init(this);
		SaveManager.init();
		gameScreen = new MainGameScreen();
		GameManager.assetManager().loadGroup("common");
        setScreen(new LoadingScreen(new LoadingScreen.LoadResult() {
			@Override
			public void finishLoading() {
				GameManager.game.setScreen(gameScreen);
				GUIManager.conversationController().setConv(GameManager.assetManager().getConversation("demonstration.conv"));
				GUIManager.conversationController().setBranch("default");
			}
		}));
	}
	
}
