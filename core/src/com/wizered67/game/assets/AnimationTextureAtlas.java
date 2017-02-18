package com.wizered67.game.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.wizered67.game.GameManager;

/**
 * Same behavior as regular TextureAtlases, but also unloads Animations when disposed.
 * @author Adam Victor
 */
public class AnimationTextureAtlas extends TextureAtlas {
    private String filepath;
    public AnimationTextureAtlas(TextureAtlasData data) {
        super(data);
    }
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
    @Override
    public void dispose() {
        super.dispose();
        GameManager.assetManager().unloadAnimations(filepath);
    }
}
