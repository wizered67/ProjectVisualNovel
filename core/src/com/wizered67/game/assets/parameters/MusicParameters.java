package com.wizered67.game.assets.parameters;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;

/**
 * Parameters for loading music, used to store volume to be accessed during playback.
 * @author Adam Victor
 */
public class MusicParameters extends MusicLoader.MusicParameter {
    public float volume;
    public MusicParameters(final float volume) {
        this.volume = volume;
        loadedCallback = new LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                Music loaded = (Music) assetManager.get(fileName, type);
                loaded.setVolume(volume);
            }
        };
    }
}
