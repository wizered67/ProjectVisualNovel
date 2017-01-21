package com.wizered67.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.Constants;
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

    public LoadingScreen(Screen ns) {
        nextScreen = ns;
        //GameManager.assetManager().loadResources();
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (GameManager.assetManager().update()) {
            GameManager.assetManager().loadAnimations();
            GameManager.game.setScreen(nextScreen);
            SaveManager.init();
            if (Constants.LOAD) { //todo fixme
                SaveManager.load(Gdx.files.local("Saves/test2.bin"));
            }
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

    }
}
