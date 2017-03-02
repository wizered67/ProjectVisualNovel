package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    OrthographicCamera mainCamera;
    Viewport mainViewport;
    Viewport guiViewport;
	@Override
	public void create() {
		assetManager = new Assets();
        musicManager = new MusicManager();
        mainBatch = new SpriteBatch();

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false);
        mainViewport = new ExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, mainCamera);

        guiViewport = new ScreenViewport();

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
