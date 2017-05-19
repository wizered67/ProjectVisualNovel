package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.inputs.Controls;
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
    GUIManager guiManager;
    ConversationController conversationController;
	Controls controls;
    InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		GameManager.init(this);

		initInput();

		assetManager = new Assets();
        musicManager = new MusicManager();
        mainBatch = new SpriteBatch();

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false);
        mainViewport = new ExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT,
				Constants.MAX_VIEWPORT_WORLD_WIDTH, Constants.MAX_VIEWPORT_WORLD_HEIGHT);
        guiViewport = new ScreenViewport();

        guiManager = new GUIManager(new Stage(guiViewport));
        conversationController = guiManager.conversationController();
		SaveManager.init();
		GameManager.assetManager().loadGroup("common");
		gameScreen = new MainGameScreen();
		setScreen(new LoadingScreen(new LoadingScreen.LoadResult() {
			@Override
			public void finishLoading() {
				GameManager.game.setScreen(gameScreen);
				conversationController.setConv(GameManager.assetManager().get("demonstration.conv", Conversation.class));
				conversationController.setBranch("default");
			}
		}));
	}

	private void initInput() {
		controls = new Controls();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}
}
