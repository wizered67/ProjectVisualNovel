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
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.Inputs.Controllable;
import com.wizered67.game.Inputs.MyInputProcessor;
import com.wizered67.game.Saving.SaveManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Game Screen for initializing and updating GUIManager.
 * @author Adam Victor
 */
public class MainGameScreen implements Screen, Controllable {
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

    TiledMapRenderer mapRenderer;
    Map<Shape2D, String> clickableShapes = new HashMap<>();

    public MainGameScreen() {
        initRendering();
        initInput();
        setupGUI();
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        //parameters.flipY = false;
        TiledMap map = new TmxMapLoader().load("investigation demo.tmx", parameters);
        MapObjects objects = map.getLayers().get("clickables").getObjects();
        for (MapObject shape : objects) {
            String branch = shape.getProperties().get("branch", String.class);
            if (shape instanceof PolygonMapObject) {
                clickableShapes.put(((PolygonMapObject) shape).getPolygon(), branch);
            } else if (shape instanceof RectangleMapObject) {
                clickableShapes.put(((RectangleMapObject) shape).getRectangle(), branch);
            }
        }
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        GUIManager.conversationController().loadConversation(map.getProperties().get("conversation", String.class));
        GUIManager.conversationController().setBranch("default");
        inputProcessor.register(this);

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCameras(delta);
        mapRenderer.setView(hudCamera);
        mapRenderer.render();
        updateGUI(delta);
        updateInput();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        for (Shape2D shape : clickableShapes.keySet()) {
            if (shape instanceof Polygon) {
                shapes.polygon(((Polygon) shape).getVertices());
            } else if (shape instanceof Rectangle) {
                shapes.rect(((Rectangle) shape).getX(), ((Rectangle) shape).getY(), ((Rectangle) shape).getWidth(), ((Rectangle) shape).getHeight());
            }
        }
        shapes.end();
    }

    private void updateCameras(float delta) {

    }

    private void updateGUI(float delta){
        hudViewport.apply(true);
        GUIManager.update(delta);
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

    /**
     * A touch event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON. PRESSED is whether it was pressed (true) or released (false).
     */
    @Override
    public void touchEvent(int screenX, int screenY, int pointer, int button, boolean pressed) {
        if (pressed) {
            screenY = Gdx.graphics.getHeight() - 1 - screenY;
            for (Shape2D shape : clickableShapes.keySet()) {
                if (shape.contains(screenX, screenY)) {
                    GUIManager.conversationController().setBranch(clickableShapes.get(shape));
                    break;
                }
            }
        }
    }

    /**
     * A key event involving key KEY mapped to ControlType CONTROL.
     * PRESSED is whether it was pressed (true) or released (false).
     */
    @Override
    public void keyEvent(MyInputProcessor.ControlType control, int key, boolean pressed) {

    }
}
