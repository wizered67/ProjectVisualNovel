package com.wizered67.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.wizered67.game.GameManager;

/**
 * Screen shown while loading all assets needed for the game, at least initially.
 * Loads assets using an AssetManager.
 * @author Adam Victor
 */
public class LoadingScreen implements Screen {
    private Stage stage;
    private ProgressBar bar;
    private LoadResult loadResult;

    public LoadingScreen(LoadResult loadResult) {
        this.loadResult = loadResult;
        stage = new Stage();
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), skin.newDrawable("white", Color.GREEN));
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0, 1, 0.01f, false, barStyle);
        bar.setVisualInterpolation(Interpolation.fade);
        bar.setAnimateDuration(0.01f);
        table.add(bar);
    }

    public void reset(LoadResult result) {
        loadResult = result;
        bar.setValue(0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bar.setValue(GameManager.assetManager().getProgress());
        stage.act(delta);
        stage.draw();
        if (GameManager.assetManager().update()) {
            loadResult.finishLoading();
        }
        System.out.println(GameManager.assetManager().getProgress());

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
        stage.dispose();
    }

    public interface LoadResult {
        void finishLoading();
    }
}
