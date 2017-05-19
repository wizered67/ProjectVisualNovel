package com.wizered67.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.inputs.MyInputProcessor;
import com.wizered67.game.saving.SaveManager;
import com.wizered67.game.screens.LoadingScreen;
import com.wizered67.game.screens.MainGameScreen;
import com.wizered67.game.assets.Assets;
import com.wizered67.game.scripting.LuaGameMethods;

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
    MyInputProcessor mainInputProcessor;
    InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		GameManager.init(this);

		initInput();

		assetManager = new Assets();
		assetManager.initResources();
        musicManager = new MusicManager();
        mainBatch = new SpriteBatch();

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false);
        mainViewport = new ExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT,
				Constants.MAX_VIEWPORT_WORLD_WIDTH, Constants.MAX_VIEWPORT_WORLD_HEIGHT);
        guiViewport = new ScreenViewport();

        guiManager = new GUIManager(new Stage(guiViewport));
        conversationController = guiManager.conversationController();
		inputMultiplexer.addProcessor(0, guiManager.getStage());
		Gdx.input.setInputProcessor(inputMultiplexer);
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
		mainInputProcessor = new MyInputProcessor();
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(mainInputProcessor);
	}
}
