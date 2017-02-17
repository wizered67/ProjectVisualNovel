package com.wizered67.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.Constants;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.Saving.SaveManager;

import java.util.HashMap;

/**
 * Screen shown while loading all assets needed for the game, at least initially.
 * Loads assets using an AssetManager.
 * @author Adam Victor
 */
public class LoadingScreen implements Screen {
    private Screen nextScreen;
    private ShapeRenderer debugRenderer;
    private Stage testStage;
    ProgressBar bar;
    float time = 0;

    public LoadingScreen(Screen ns) {
        nextScreen = ns;
        testStage = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        testStage.addActor(table);
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), skin.newDrawable("white", Color.GREEN));
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0, 0.8f, 0.01f, false, barStyle);
        //bar.setPosition(10, 10);
        //bar.setSize(290, bar.getPrefHeight());
        bar.setAnimateDuration(2);
        table.add(bar);
        debugRenderer = new ShapeRenderer();
        //GameManager.assetManager().loadRaw("Conversations/demonstration.conv", Conversation.class);
        //GameManager.assetManager().initResources();
        //GameManager.assetManager().loadGroup("common");
        GameManager.assetManager().loadConversation("investigationDemo.conv");
        //GameManager.assetManager().load("Edgeworth");
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        time += delta;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bar.setValue(GameManager.assetManager().getProgress());
        testStage.act(delta);
        testStage.draw();
        //System.out.println(System.nanoTime() / 1000000);
        if (GameManager.assetManager().update()) {
            //GameManager.assetManager().loadAnimation("Edgeworth");
            //GameManager.assetManager().unload("Edgeworth"); //todo remove, debug
            GameManager.game.setScreen(nextScreen); //todo deal with this loading stuff
            //GUIManager.conversationController().setConv((Conversation) GameManager.assetManager().getRaw("Conversations/demonstration.conv"));
            GUIManager.conversationController().setConv(GameManager.assetManager().getConversation("investigationDemo.conv"));
            GUIManager.conversationController().setBranch("default");
            //GameManager.assetManager().loadRaw("Conversations/super long.conv", Conversation.class);
            SaveManager.init();
        }
        System.out.println(GameManager.assetManager().getProgress());

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.circle((time * 1000) % Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, 64);
        debugRenderer.flush();
        debugRenderer.end();

    }

    @Override
    public void resize(int width, int height) {

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
        debugRenderer.dispose();
    }
}
