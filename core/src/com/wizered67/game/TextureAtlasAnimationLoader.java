package com.wizered67.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Adam on 2/17/2017.
 */
public class TextureAtlasAnimationLoader extends TextureAtlasLoader {
    public TextureAtlasAnimationLoader(FileHandleResolver resolver) {
        super(resolver);
    }
    @Override
    public TextureAtlas load (AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter) {
        TextureAtlas atlas = super.load(assetManager, fileName, file, parameter);
        GameManager.assetManager().loadAnimation(fileName, atlas);
        return atlas;
    }
}
