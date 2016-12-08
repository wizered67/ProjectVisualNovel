package com.wizered67.game.Screens;


import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.javafx.geom.Vec4f;
import com.wizered67.game.*;
import com.wizered67.game.Entities.CircleEntity;
import com.wizered67.game.Entities.Entity;
import com.wizered67.game.Entities.PlayerEntity;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.Inputs.MyInputProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameScreen implements Screen {
    //800x600
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private OrthographicCamera debugCamera;
    private PlayerEntity player;
    private ShapeRenderer shapes;
    private BitmapFont font;

    //private int[][] staticGrid;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator = 0f;

    private TiledMap map;
    private String mapName;
    private float mapWidthInPixels;
    private float mapHeightInPixels;
    private int mapWidthInTiles;
    private int mapHeightInTiles;
    private int mapNumLayers;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CustomExtendViewport myViewport;
    private Viewport hudViewport;
    private MyInputProcessor inputProcessor;
    private Stage stage;
    private GUIManager gui;
    private final Vector3 cameraTarget = new Vector3(0, 0, 0);
    private boolean worldUpdate = false;
    private ShaderProgram outlineShader;
    private ShaderProgram blurShader;
    private Vector2 outlineAlpha = new Vector2(1, 0);
    private Vector2 stepSize = new Vector2(0, 1.5f);
    private FrameBuffer blurTargetA, blurTargetB;
    private TextureRegion fboRegion;
    private RayHandler rayHandler;
    public ArrayList<Vec4f> debugLines = new ArrayList<Vec4f>();
    private Vector2 cameraPositionFractions = new Vector2();

    public String getMapName() {
        return mapName;
    }

    Comparator<Entity> depthComparator = new Comparator<Entity>() {
        public int compare(Entity e1, Entity e2) {
            int d1 = e1.getDepth();
            int d2 = e2.getDepth();
            return d1 - d2;
        }
    };


    public GameScreen() {
        Box2D.init();
        WorldManager.init();
        initRendering();
        initShaders();
        loadMap("testmap.tmx");
        initInput();
        initLighting();
        setupGUI();
        float playerX = 300;
        float playerY = 300;

        player = new PlayerEntity("Player 1", this);
        player.setTransform(new Vector2(Constants.toMeters(playerX), Constants.toMeters(playerY)), 0);
        camera.position.set(new Vector3(Constants.toPixels(player.getX()), Constants.toPixels(player.getY()), 0));
        debugCamera.position.set(new Vector3(player.getX(), player.getY(), 0));
        camera.update();
        debugCamera.update();
        inputProcessor.addControllableEntity(player);
        //EntityManager.addEntity(player);
        EntityManager.addEntity(new CircleEntity());
    }

    public void loadMap(String mapName){
        if (mapName.equals(this.mapName))
            return;
        this.mapName = mapName;
        EntityManager.changeMap(mapName); //tell entity manager to change current map
        CustomMapLoader mapLoader = new CustomMapLoader();
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.generateMipMaps = true;
        parameters.textureMagFilter = TextureFilter.MipMapLinearLinear;
        parameters.textureMinFilter = TextureFilter.MipMapLinearLinear;
        map = mapLoader.load(mapName, parameters);
        mapWidthInTiles = map.getProperties().get("width", Integer.class);
        mapHeightInTiles = map.getProperties().get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * Constants.TILE_SIZE;
        mapHeightInPixels = mapHeightInTiles * Constants.TILE_SIZE;
        mapNumLayers = map.getLayers().getCount();

        float unitScale = 1f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale, batch);

        loadMapData();
    }

    public void loadMapData(){
        int[][] staticGrid;
        if (EntityManager.getStaticMap(mapName) != null){
            staticGrid = EntityManager.getStaticMap(mapName);
        }
        else{
            staticGrid = calculatesStaticGrid();
            EntityManager.setStaticMap(mapName, staticGrid);
        }

        if (EntityManager.getAllGroundBody(mapName) != null){
            Body ground = EntityManager.getAllGroundBody(mapName);
            EntityManager.getAllGroundBody(mapName).setActive(true);
        }
        else {
            Body ground = setupGround(staticGrid);
            EntityManager.setAllGroundBody(mapName, ground);
        }
    }

    public int[][] calculatesStaticGrid(){
        MapLayer collisionLayer = map.getLayers().get("collisions");
        int[][] staticGrid = new int[mapWidthInTiles][mapHeightInTiles];
        for (int x = 0; x < staticGrid.length; x++) {
            for (int y = 0; y < staticGrid[0].length; y++) {
                staticGrid[x][y] = 0;
            }
        }

        for (int i = 0; i < mapNumLayers; i++) {
            MapLayer layer = map.getLayers().get(i);
            if (layer.getObjects().getCount() == 0 && !layer.getName().equals("collisionLayer")) { //tile layer
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                for (int x = 0; x < mapWidthInTiles; x++) {
                    for (int y = 0; y < mapHeightInTiles; y++) {
                        Cell cell = tileLayer.getCell(x, y);
                        if (cell != null) {
                            TiledMapTile tile = cell.getTile();
                            String solid = tile.getProperties().get("Solid", String.class);
                            String custom = tile.getProperties().get("customCollisionBox", String.class);
                            if (solid != null && solid.equalsIgnoreCase("true") && (custom == null || !custom.equalsIgnoreCase("true"))) {
                                staticGrid[x][y] = 1;
                            }
                        }
                    }
                }
            } else { //not tile layer
                MapObjects objects = layer.getObjects();
            }
        }
        return staticGrid;
    }

    public void initLighting(){
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(false);
        rayHandler = new RayHandler(WorldManager.world);
        rayHandler.useCustomViewport(myViewport.getScreenX(), myViewport.getScreenY(), myViewport.getScreenWidth(), myViewport.getScreenHeight());
        rayHandler.setAmbientLight(0, 0, 0, 1f);
        rayHandler.setBlurNum(3);
        rayHandler.setBlur(true);
        rayHandler.setShadows(true);
        Light.setGlobalContactFilter(Constants.CATEGORY_LIGHT, (short) 0, Constants.MASK_LIGHT);
        //DirectionalLight directionalLight = new DirectionalLight(rayHandler, 128, new Color(1, 1, 1, 0.1f), 0);
        PointLight light = new PointLight(
                rayHandler, 128, new Color(1,0,0, 0.8f), Constants.toMeters(256), Constants.toMeters(128), Constants.toMeters(400));
        //light.setSoft(false);
        light.update();
        //rayHandler.
    }

    public void initShaders(){
        final String vertexShader = Gdx.files.internal("default_vertex.glsl").readString();
        final String outlineFragmentShader = Gdx.files.internal("outline_pixel.glsl").readString();
        final String blurFragmentShader = Gdx.files.internal("blur_pixel.glsl").readString();
        outlineShader = new ShaderProgram(vertexShader, outlineFragmentShader);
        blurShader = new ShaderProgram(vertexShader, blurFragmentShader);

        System.out.println(outlineShader.getLog());
        System.out.println(blurShader.getLog());
        ShaderProgram.pedantic = false;
        int FBO_SIZE = 1024;
        blurTargetA = new FrameBuffer(Pixmap.Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
        blurTargetB = new FrameBuffer(Pixmap.Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
        fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
        fboRegion.flip(false, true);

    }

    public void initInput(){
        inputProcessor = new MyInputProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputProcessor);
        GameManager.setMainInputProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void initRendering(){
        font = new BitmapFont(false);
        font.setColor(Color.WHITE);
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);//, Constants.VIRTUAL_WIDTH ,Constants.VIRTUAL_HEIGHT);
        //camera.zoom = cameraZoom;
        myViewport = new CustomExtendViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, mapWidthInPixels, mapHeightInPixels, camera);
        myViewport.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        myViewport.apply();

        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.
                getHeight());
        hudCamera.setToOrtho(false); //was true prior to scene2D
        hudCamera.zoom = 1;
        hudViewport = new ScreenViewport(hudCamera);

        myViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, Constants.toMeters(myViewport.getWorldWidth()), Constants.toMeters(myViewport.getWorldHeight()));
        //debugCamera.zoom = camera.zoom;

        debugRenderer = new Box2DDebugRenderer();

        stage = new Stage(hudViewport);

        shapes = new ShapeRenderer();
    }

    public void setupGUI() {
        gui = new GUIManager(stage);
    }

    public Vector2 findNewCameraPosition(float deltaTime){
        float cx = Constants.toPixels(player.getPosition().x); // + player.getWidth() / 2
        float cy = Constants.toPixels(player.getPosition().y);

        float cw = Gdx.graphics.getWidth() / myViewport.getScale();//camera.viewportWidth - myViewport.getScreenX(); //* (myViewport.getScreenWidth() / Gdx.graphics.getWidth());
        float ch = Gdx.graphics.getHeight() / myViewport.getScale();//camera.viewportHeight; //* (myViewport.getScreenHeight() / Gdx.graphics.getHeight());
        if (cw <= mapWidthInPixels && cx - (cw / 2) < 0)
            cx = cw / 2; //- (myViewport.getScreenX() / 2) * (myViewport.getScreenWidth() / Gdx.graphics.getWidth());
        else if (cx + (cw / 2) > mapWidthInPixels) {
            cx = mapWidthInPixels - cw / 2; //- (myViewport.getScreenX() / 2) * (myViewport.getScreenWidth() / Gdx.graphics.getWidth());
        }

        if (ch <= mapHeightInPixels && cy - (ch / 2) < 0)
            cy = ch / 2;
        else if (cy + (ch / 2) > mapHeightInPixels) {
            cy = mapHeightInPixels - ch / 2;
        }
        return new Vector2(cx, cy);
    }

    public void updateCameras(float deltaTime) {
        //if (!worldUpdate)
        //    return;

        Vector2 cameraPos = findNewCameraPosition(deltaTime);
        float cx = cameraPos.x;
        float cy = cameraPos.y;
        //myViewport.setMaxWorldWidth(maxWorldHeight);
        //myViewport.setMaxWorldHeight(maxWorldHeight);
        //myViewport.setScreenPosition((int)cx, (int)cy);
        int s = 1;
        //camera.position.set(Math.round(cx / s) * s, Math.round(cy / s) * s, 0);
        cameraTarget.set(Math.round(cx), Math.round(cy), 0);
        float lerp = 0.1f;
        /*
        if (camera.position.dst(cameraTarget) < 10){
            camera.position.set(cx, cy, 0);
        }
        else {
            camera.position.lerp(cameraTarget, lerp);
        }
        //


        if (debugCamera.position.dst(metersTarget) < Constants.toMeters(10)){
            debugCamera.position.set(metersTarget);
        }
        else {
            debugCamera.position.lerp(metersTarget, lerp);
        }
        */
        //debugCamera.position.set(Constants.toMeters(cx), Constants.toMeters(cy), 0);
        Vector3 metersTarget = new Vector3(Constants.toMeters(Math.round(cx)), Constants.toMeters(Math.round(cy)), 0f);
        camera.position.lerp(cameraTarget, lerp);
        if (cameraTarget.dst(camera.position) < 1e-1){
            camera.position.set(cameraTarget.cpy());
        }
        //TODO make this do something...
        cameraPositionFractions.x = (float) (camera.position.x - Math.floor(camera.position.x));
        cameraPositionFractions.y = (float) (camera.position.y - Math.floor(camera.position.y));

        debugCamera.position.lerp(metersTarget, lerp);
        camera.update();
        debugCamera.update();
    }

    public void updateInput() {
        inputProcessor.update();
    }

    @Override
    public void render(float delta) {
        // update and draw stuff
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //camera.position.set(Constants.toPixels(player.getX()) + player.getWidth() / 2, Constants.toPixels(player.getY()), 0);



        WorldManager.makeAllBodies();
        //update all entities before handling collisions
        EntityManager.update(delta);
        EntityManager.process();

        updateCameras(delta);
        hudViewport.apply(true);
        shapes.setProjectionMatrix(hudCamera.combined);
        shapes.setColor(new Color(84f / 255, 120f / 255, 226f / 255, 1));
        shapes.begin(ShapeType.Filled);
        shapes.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapes.end();
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        myViewport.apply();
        //batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        mapRenderer.setView(camera);
        mapRenderer.render();
        int layersAbovePlayer;
        Integer lap = map.getProperties().get("layersAbovePlayer", Integer.class);
        if (lap == null){
            layersAbovePlayer = mapNumLayers;
        }
        else
        {
            layersAbovePlayer = lap;
        }
        for (int i = 0; i < layersAbovePlayer; i++)
            mapRenderer.render(new int[] {i});
        if (Constants.DEBUG) {
            shapes.begin(ShapeType.Filled);
            shapes.setColor(Color.ORANGE);
            shapes.circle(Constants.toPixels(player.getX()), Constants.toPixels(player.getY()), 4);





            shapes.end();
        }

        batch.begin();
        ArrayList<Entity> allEntities = EntityManager.getEntities();
        Collections.sort(allEntities, depthComparator);
        for (Entity entity : allEntities) {
            if (entity != null) {
                if (entity.getSprite() != null && entity.getSprite().getTexture() != null) {
                    entity.getSprite().draw(batch);
                    //batch.draw(entity.getSprite(), entity.getDrawOffset().x + Constants.toPixels(entity.getX()) - entity.getWidth() / 2, Constants.toPixels(entity.getY() - entity.getBoundingHeight() / 2) + entity.getDrawOffset().y);
                }
            }
        }

        batch.end();
        for (int i = layersAbovePlayer; i < mapNumLayers; i++)
            mapRenderer.render(new int[] {i});

        /*
        outlineAlpha.x = MathUtils.lerp(outlineAlpha.x, outlineAlpha.y, 0.1f);
        System.out.println(outlineAlpha.x);
        if (outlineAlpha.x >= .99)
            outlineAlpha.y = 0;
        if (outlineAlpha.x <= 1e-2)
            outlineAlpha.y = 1;
            */
        //stepSize.x = MathUtils.lerp(stepSize.x, stepSize.y, 0.1f);
        if (stepSize.y == 0){
            stepSize.x -= 0.05;
        }
        else{
            stepSize.x += 0.05;
        }
        if (stepSize.x >= 1.75){
            stepSize.y = 0;
        }
        if (stepSize.x <= 0.25){
            stepSize.y = 1.75f;
        }
                /*
        blurTargetA.begin();

        //Clear FBO A with an opaque colour to minimize blending issues
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 0f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
*/
        //send the new projection matrix (FBO size) to the default shader
        //batch.resize(blurTargetA.getWidth(), blurTargetA.getHeight());
        //batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), blurTargetA.getHeight()));
        //now we can start our batch
        batch.begin();

        //render our scene fully to FBO A

        //batch.begin();
        //flush the batch, i.e. render entities to GPU
       /*
        batch.flush();

        //After flushing, we can finish rendering to FBO target A
        blurTargetA.end();

        batch.setShader(blurShader);

        //since we never called batch.end(), we should still be drawing
        //which means are blurShader should now be in use

        //ensure the direction is along the X-axis only
        blurShader.setUniformf("dir", 1f, 0f);

        //update blur amount based on touch input
        blurShader.setUniformf("radius", 2f);

        //our first blur pass goes to target B
        blurTargetB.begin();
        //we want to render FBO target A into target B
        fboRegion.setTexture(blurTargetA.getColorBufferTexture());
        //draw the scene to target B with a horizontal blur effect
        batch.draw(fboRegion, 0, 0);
        //flush the batch before ending the FBO
        batch.flush();
        //finish rendering target B
        blurTargetB.end();

        //now we can render to the screen using the vertical blur shader

        //update our projection matrix with the screen size
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        //update the blur only along Y-axis
        blurShader.setUniformf("dir", 0f, 1f);

        //update the Y-axis blur radius
        blurShader.setUniformf("radius", 2f);

        //draw target B to the screen with a vertical blur effect
        fboRegion.setTexture(player.getSprite().getTexture()); //blurTargetB.getColorBufferTexture()
        batch.draw(fboRegion, 0, 0);
*/
        //reset to default shader without blurs
        batch.setShader(null);

        batch.end();
        if (Constants.DEBUG) {
            debugRenderer.render(WorldManager.world, debugCamera.combined);
        }

        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(Constants.PPM), 0, 0, myViewport.getScreenWidth(), myViewport.getScreenHeight());
        rayHandler.updateAndRender();
        if (Constants.DEBUG) {
            for (Vec4f line : debugLines) {
                drawLine(line.x, line.y, line.z, line.w);
            }
        }

        doPhysicsStep(delta);
        updateGUI(delta);
        renderHud(delta);
        updateInput();
    }


    private void updateGUI(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GUIManager.update(delta);
    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        worldUpdate = false;
        WorldManager.worldStep();
        worldUpdate = true;
        for (Entity e : EntityManager.getEntities()){
            if (e.getBody() != null)
                e.getPosition().set(e.getBodyPosition());
        }
        /*
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            WorldManager.worldStep();
            for (Entity e : allEntities){
                if (e.getBody() != null)
                    e.getPosition().set(e.getBodyPosition());
            }
            accumulator -= Constants.TIME_STEP;
            worldUpdate = true;
        }
        interpolate(accumulator / Constants.TIME_STEP);
        System.out.println(player.getBodyPosition());
        System.out.println(player.getPosition());
        */
    }

    public void interpolate(float alpha) {
        for (Entity entity : EntityManager.getEntities()) {
            Transform transform = entity.getBody().getTransform();
            Vector2 bodyPosition = transform.getPosition();
            Vector2 position = entity.getPosition();
            //float angle = entity.getAngle();
            //float bodyAngle = transform.getRotation();
            position.x = bodyPosition.x * alpha + position.x * (1.0f - alpha);
            position.y = bodyPosition.y * alpha + position.x * (1.0f - alpha);
            //entity.setAngle(bodyAngle * alpha + angle * (1.0f - alpha));
        }
    }

    public void renderHud(float delta) {
        hudViewport.apply(true);
        stage.act(delta);
        stage.draw();
        if (Constants.DEBUG){
            batch.setProjectionMatrix(hudCamera.combined);
            batch.begin();

            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
            font.draw(batch, "Mouse X: " + Gdx.input.getX(), 50, 70);
            font.draw(batch, "Mouse Y: " + Gdx.input.getY(), 50, 90);
            font.draw(batch, "PX: " + player.getX() + " PY: " + player.getY(), 50, 130);
            font.draw(batch, "Player Velocity: " + player.getVelocity(), 50, 150);
            font.draw(batch, "Camera Position: " + camera.position.x + ", " + camera.position.y, 50, 170);
            double testY = player.getY() + player.getY();
            font.draw(batch, "Interpolated X: " + player.getPosition().x + " Interpolated Y: " + player.getPosition().y, 50, 190);
            font.draw(batch, "Projected Mouse: " + myViewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x + ", " +  myViewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y, 50, 210);

            font.draw(batch, "Width/Height: " + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight(), 50, 250);
            font.draw(batch, "Viewport Position: " + myViewport.getScreenX() + ", " + myViewport.getScreenY() , 50, 270);
            font.draw(batch, "Viewport WH: " + myViewport.getScreenWidth() + ", " + myViewport.getScreenHeight() , 50, 290);
            font.draw(batch, "Camera Viewport WH: " + camera.viewportWidth + ", " + camera.viewportHeight, 50,310);

            batch.end();
            batch.setProjectionMatrix(camera.combined);
         }

        myViewport.apply();
    }

    public Body setupGround(int[][] staticGrid) {
        BodyDef allGroundBodyDef = new BodyDef();
        allGroundBodyDef.position.set(new Vector2(0f, 0f));
        ChainShape chainShape = new ChainShape();
        Body allGroundBody = WorldManager.world.createBody(allGroundBodyDef);

        for (int i = 0; i < mapNumLayers; i++) {
            MapLayer layer = map.getLayers().get(i);
            if (layer.getObjects().getCount() > 0 && layer.getName().equals("collisionLayer")) { //tile layer
                MapObjects objects = layer.getObjects();
                for (MapObject object : objects){
                    FixtureDef collisionFDef = new FixtureDef();
                    collisionFDef.density = 0.0f;
                    //groundChain.friction = 0;
                    collisionFDef.filter.maskBits = Constants.MASK_SCENERY;
                    collisionFDef.filter.categoryBits = Constants.CATEGORY_SCENERY;
                    if (object instanceof EllipseMapObject){
                        EllipseMapObject mapEllipseObject = (EllipseMapObject) object;
                        Ellipse mapEllipseShape = mapEllipseObject.getEllipse();
                        CircleShape box2DCircleShape = new CircleShape();
                        box2DCircleShape.setPosition(new Vector2(Constants.toMeters(mapEllipseShape.x + mapEllipseShape.width / 2), Constants.toMeters(mapEllipseShape.y + mapEllipseShape.height / 2)));
                        box2DCircleShape.setRadius(Constants.toMeters(mapEllipseShape.width / 2));
                        collisionFDef.shape = box2DCircleShape;
                    }
                    else if (object instanceof CircleMapObject){
                        CircleMapObject mapCircleObject = (CircleMapObject) object;
                        Circle mapCircleShape = mapCircleObject.getCircle();
                        CircleShape box2DCircleShape = new CircleShape();
                        box2DCircleShape.setPosition(new Vector2(Constants.toMeters(mapCircleShape.x + mapCircleShape.radius / 2), Constants.toMeters(mapCircleShape.y + mapCircleShape.radius / 2)));
                        box2DCircleShape.setRadius(Constants.toMeters(mapCircleShape.radius / 2));
                        collisionFDef.shape = box2DCircleShape;
                    }
                    else if (object instanceof RectangleMapObject){
                        RectangleMapObject mapRectangleObject = (RectangleMapObject) object;
                        Rectangle mapRectangleShape = mapRectangleObject.getRectangle();
                        PolygonShape box2DRectangleShape = new PolygonShape();
                        Vector2 pos = new Vector2(mapRectangleShape.getX() + mapRectangleShape.getWidth() / 2, mapRectangleShape.getY() + mapRectangleShape.getHeight() / 2);
                        box2DRectangleShape.setAsBox(Constants.toMeters(mapRectangleShape.getWidth() / 2),
                                Constants.toMeters(mapRectangleShape.getHeight() / 2), Constants.toMeters(pos), 0);
                        collisionFDef.shape = box2DRectangleShape;
                    }
                    else if (object instanceof PolygonMapObject){
                        PolygonMapObject mapPolygonObject = (PolygonMapObject) object;
                        Polygon mapPolygonShape = mapPolygonObject.getPolygon();
                        PolygonShape box2DPolygonShape = new PolygonShape();

                        float[] vertices = mapPolygonShape.getVertices();
                        for (int v = 0; v < vertices.length; v += 2) {
                            vertices[v] = Constants.toMeters(mapPolygonShape.getX() + vertices[v]);
                            vertices[v+1] = Constants.toMeters(mapPolygonShape.getY() + vertices[v+1]);
                        }

                        box2DPolygonShape.set(vertices);
                        collisionFDef.shape = box2DPolygonShape;
                    }
                    else if (object instanceof PolylineMapObject){
                        PolylineMapObject mapPolylineObject = (PolylineMapObject) object;
                        Polyline mapPolylineShape = mapPolylineObject.getPolyline();
                        ChainShape box2DPolylineShape = new ChainShape();

                        float[] vertices = mapPolylineShape.getVertices();
                        Vector2[] worldVertices = new Vector2[vertices.length / 2];
                        for (int v = 0; v < vertices.length / 2; v += 1) {
                            worldVertices[v] = new Vector2();
                            worldVertices[v].x = Constants.toMeters(vertices[v * 2] + mapPolylineShape.getX());
                            worldVertices[v].y = Constants.toMeters(vertices[v * 2 + 1] + mapPolylineShape.getY());
                        }

                        box2DPolylineShape.createChain(worldVertices);
                        collisionFDef.shape = box2DPolylineShape;
                    }
                    allGroundBody.createFixture(collisionFDef);
                    collisionFDef.shape.dispose();
                }
            }
        }

        FixtureDef groundChain = new FixtureDef();
        boolean continuous = false;
        Vector2[] vertices = new Vector2[2];
        vertices[0] = null;
        vertices[1] = null;
        for (int x = 0; x < staticGrid.length + 1; x++) {
            for (int y = 0; y < staticGrid[0].length; y++) {
                if ((x < staticGrid.length && staticGrid[x][y] != 0) || (x != 0 && staticGrid[x - 1][y] != 0)) {
                    if (!continuous) {
                        chainShape = new ChainShape();
                        groundChain.shape = chainShape;
                        groundChain.density = 0.0f;
                        //groundChain.friction = 0;
                        groundChain.filter.maskBits = Constants.MASK_SCENERY;
                        groundChain.filter.categoryBits = Constants.CATEGORY_SCENERY;
                        vertices[0] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
                        continuous = true;
                    } else {
                        vertices[1] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE + Constants.TILE_SIZE));
                    }
                } else {
                    if (continuous) {
                        if (vertices[1] == null) {
                            vertices[1] = vertices[0].cpy().add(new Vector2(0, Constants.toMeters(Constants.TILE_SIZE)));
                        }
                        chainShape.createChain(vertices);
                        allGroundBody.createFixture(groundChain);
                        vertices[0] = null;
                        vertices[1] = null;
                        continuous = false;
                    }
                }
            }
            if (continuous) {
                if (vertices[1] == null) {
                    vertices[1] = vertices[0].cpy().add(new Vector2(0, Constants.toMeters(Constants.TILE_SIZE)));
                }
                chainShape.createChain(vertices);
                allGroundBody.createFixture(groundChain);
                vertices[0] = null;
                vertices[1] = null;
                continuous = false;
            }
        }


        for (int y = 0; y < staticGrid[0].length + 1; y++) {
            for (int x = 0; x < staticGrid.length; x++) {
                if ((y < staticGrid[0].length && staticGrid[x][y] != 0) || (y != 0 && staticGrid[x][y - 1] != 0)) {
                    if (!continuous) {
                        chainShape = new ChainShape();
                        groundChain.shape = chainShape;
                        groundChain.density = 0.0f;
                        groundChain.filter.maskBits = Constants.MASK_SCENERY;
                        groundChain.filter.categoryBits = Constants.CATEGORY_SCENERY;
                        //groundChain.friction = 0;
                        vertices[0] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
                        continuous = true;
                    } else {
                        vertices[1] = new Vector2(Constants.toMeters(x * Constants.TILE_SIZE + Constants.TILE_SIZE), Constants.toMeters(y * Constants.TILE_SIZE));
                    }
                } else {
                    if (continuous) {
                        if (vertices[1] == null) {
                            vertices[1] = vertices[0].cpy().add(new Vector2(Constants.toMeters(Constants.TILE_SIZE), 0));
                        }
                        chainShape.createChain(vertices);
                        allGroundBody.createFixture(groundChain);
                        vertices[0] = null;
                        vertices[1] = null;
                        continuous = false;
                    }
                }
            }
            if (continuous) {
                if (vertices[1] == null) {
                    vertices[1] = vertices[0].cpy().add(new Vector2(Constants.toMeters(Constants.TILE_SIZE), 0));
                }
                chainShape.createChain(vertices);
                Fixture f = allGroundBody.createFixture(groundChain);
                //f.setUserData(new FixtureData(Fixtures.GROUND));
                vertices[0] = null;
                vertices[1] = null;
                continuous = false;
            }
        }
        chainShape.dispose();
        return allGroundBody;
    }

    @Override
    public void resize(int width, int height) {
        myViewport.update(width, height);
        rayHandler.useCustomViewport(myViewport.getScreenX(), myViewport.getScreenY(), myViewport.getScreenWidth(), myViewport.getScreenHeight());
        hudViewport.update(width, height);
        debugCamera.viewportWidth = Constants.toMeters(myViewport.getWorldWidth());//Constants.toMeters(width / myViewport.getScale());
        debugCamera.viewportHeight = Constants.toMeters(myViewport.getWorldHeight());//Constants.toMeters(height / myViewport.getScale());
        debugCamera.update();
        GUIManager.resize(width, height);
    }

    public boolean inWorld(Rectangle rect) {
        Rectangle worldRect = new Rectangle(0, 0, mapWidthInPixels, mapHeightInPixels);
        return worldRect.overlaps(rect);
    }

    public boolean inWorld(Vector2 pt) {
        Rectangle worldRect = new Rectangle(0, 0, mapWidthInPixels, mapHeightInPixels);
        return worldRect.contains(pt);
    }

    @Override
    public void show() {
        // called when this screen is set as the screen with game.setScreen();
    }


    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
    }


    @Override
    public void pause() {
    }


    @Override
    public void resume() {
    }

    public void drawLine(float x1, float y1, float x2, float y2){
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.ORANGE);
        shapes.line(x1, y1, x2, y2);
        shapes.end();
    }

    public Vector3 unproject(float x, float y){
       return myViewport.unproject(new Vector3(x, y, 0));
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        batch.dispose();
        debugRenderer.dispose();
        shapes.dispose();
        map.dispose();
        rayHandler.dispose();
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }

    public void setDebugRendererDrawInactive(boolean draw){
        debugRenderer.setDrawInactiveBodies(draw);
    }

    public boolean getDebugRendererDrawInactive(){
        return debugRenderer.isDrawInactiveBodies();
    }
}