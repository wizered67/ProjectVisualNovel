package com.wizered67.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.wizered67.game.GameManager;

/**
 * Created by Adam on 10/26/2016.
 */
public class LoadingScreen implements Screen {
    private AssetManager assetManager;
    private Screen nextScreen;

    public LoadingScreen(AssetManager am, Screen ns) {
        assetManager = am;
        nextScreen = ns;
        loadDefault();
    }

    public void loadDefault() {
        assetManager.load("Sounds/talksoundmale.wav", Sound.class);
        assetManager.load("Sounds/talksoundfemale.wav", Sound.class);
        assetManager.load("Sounds/sfxblipmale.wav", Sound.class);
        assetManager.load("Sounds/intense.wav", Sound.class);
        assetManager.load("Music/crossexamination.mp3", Music.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(assetManager.update()) {
            GameManager.game.setScreen(nextScreen);
        }
        System.out.println(assetManager.getProgress());
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
