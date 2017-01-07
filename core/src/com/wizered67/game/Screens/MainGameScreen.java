package com.wizered67.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.Inputs.MyInputProcessor;
import com.wizered67.game.Saving.SaveManager;

/**
 * Main Game Screen for initializing and updating GUIManager.
 * @author Adam Victor
 */
public class MainGameScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private OrthographicCamera debugCamera;
    private BitmapFont font;
    private Viewport hudViewport;
    private MyInputProcessor inputProcessor;
    private Stage stage;
    private GUIManager gui;
    private ShapeRenderer shapes;

    public MainGameScreen() {
        initRendering();
        initInput();
        setupGUI();
    }

    public void setupGUI() {
        gui = new GUIManager(stage);
    }

    private void initRendering() {
        font = new BitmapFont(false);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);//, Constants.VIRTUAL_WIDTH ,Constants.VIRTUAL_HEIGHT);
        //camera.zoom = cameraZoom;
        //myViewport = new CustomExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, mapWidthInPixels, mapHeightInPixels, camera);
        //myViewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //myViewport.apply();

        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());
        hudCamera.setToOrtho(false); //was true prior to scene2D
        hudCamera.zoom = 1;
        hudViewport = new ScreenViewport(hudCamera);

        //myViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debugCamera = new OrthographicCamera();
        //debugCamera.setToOrtho(false, Constants.toMeters(myViewport.getWorldWidth()), Constants.toMeters(myViewport.getWorldHeight()));
        //debugCamera.zoom = camera.zoom;

        stage = new Stage(hudViewport);

        shapes = new ShapeRenderer();
    }

    public void initInput(){
        inputProcessor = new MyInputProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        GameManager.setMainInputProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        updateCameras(delta);
        updateGUI(delta);
        updateInput();
    }

    private void updateCameras(float delta) {

    }

    private void updateGUI(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GUIManager.update(delta);
        hudViewport.apply(true);
        stage.act(delta);
        stage.draw();
    }

    public void updateInput() {
        inputProcessor.update();
    }

    @Override
    public void resize(int width, int height) {
        //myViewport.update(width, height);
        hudViewport.update(width, height);
        //debugCamera.viewportWidth = Constants.toMeters(myViewport.getWorldWidth());//Constants.toMeters(width / myViewport.getScale());
        //debugCamera.viewportHeight = Constants.toMeters(myViewport.getWorldHeight());//Constants.toMeters(height / myViewport.getScale());
        //debugCamera.update();
        GUIManager.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        batch.dispose();
        shapes.dispose();
    }
}
