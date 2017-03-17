package com.wizered67.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.Constants;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.inputs.Controllable;
import com.wizered67.game.inputs.MyInputProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Main Game Screen for initializing and updating GUIManager.
 * @author Adam Victor
 */
public class MainGameScreen implements Screen, Controllable {
    private SpriteBatch batch;
    private BitmapFont font;
    private MyInputProcessor inputProcessor;
    private Stage stage;
    private GUIManager gui;
    private ShapeRenderer shapes;

    public MainGameScreen() {
        initRendering();
        initInput();
        setupGUI();

        inputProcessor.register(this);

    }

    public void setupGUI() {
        gui = new GUIManager(stage);
    }

    private void initRendering() {
        font = new BitmapFont(false);
        font.setColor(Color.WHITE);
        batch = GameManager.mainBatch();
        //camera.zoom = cameraZoom;
        //myViewport = new CustomExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, mapWidthInPixels, mapHeightInPixels, camera);
        //myViewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //myViewport.apply();
        /*
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());
        hudCamera.setToOrtho(false); //was true prior to scene2D
        hudCamera.zoom = 1;
        hudViewport = new ScreenViewport(hudCamera);

        //myViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //debugCamera.setToOrtho(false, Constants.toMeters(myViewport.getWorldWidth()), Constants.toMeters(myViewport.getWorldHeight()));
        //debugCamera.zoom = camera.zoom;
        */
        stage = new Stage(GameManager.guiViewport());

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
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCameras(delta);
        updateGUI(delta);
        updateInput();
    }

    private void updateCameras(float delta) {

    }

    private void updateGUI(float delta){
        //hudViewport.apply(true);
        GameManager.mainViewport().apply();
        GUIManager.update(delta);
    }

    public void updateInput() {
        inputProcessor.update();
    }

    @Override
    public void resize(int width, int height) {
        //myViewport.update(width, height);
        //hudViewport.update(width, height);
        //debugCamera.viewportWidth = Constants.toMeters(myViewport.getWorldWidth());//Constants.toMeters(width / myViewport.getScale());
        //debugCamera.viewportHeight = Constants.toMeters(myViewport.getWorldHeight());//Constants.toMeters(height / myViewport.getScale());
        //debugCamera.update();
        //GUIManager.resize(width, height);
        GameManager.mainViewport().update(width, height, true); //todo figure out if center camera is always correct
        GameManager.guiViewport().update(width, height);
        GUIManager.resize(width, height);

        gui.resizeNonstatic(width, height);
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

    /**
     * A touch event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON. PRESSED is whether it was pressed (true) or released (false).
     */
    @Override
    public void touchEvent(int screenX, int screenY, int pointer, int button, boolean pressed) {

    }

    /**
     * A key event involving key KEY mapped to ControlType CONTROL.
     * PRESSED is whether it was pressed (true) or released (false).
     */
    @Override
    public void keyEvent(MyInputProcessor.ControlType control, int key, boolean pressed) {

    }
}
