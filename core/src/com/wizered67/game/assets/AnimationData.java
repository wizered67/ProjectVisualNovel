package com.wizered67.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.GameManager;

/**
 * Stores the information necessary to create a specific Animation of a TextureAtlas.
 * @author Adam Victor
 */
public class AnimationData {
    private String atlasName;
    private String animationName;
    private float frameDuration;
    private Animation.PlayMode playMode;

    public AnimationData() {}
    public AnimationData(String atlas, String animation, float duration, Animation.PlayMode mode) {
        atlasName = atlas;
        animationName = animation;
        frameDuration = duration;
        playMode = mode;
    }

    public String getAtlasAnimation() {
        return atlasName + "_" + animationName;
    }

    public Animation createAnimation() {
        if (!GameManager.assetManager().isLoaded(atlasName)) {
            GameManager.error("Atlas " + atlasName + " was not loaded yet.");
            return null;
        }
        TextureAtlas atlas = GameManager.assetManager().get(atlasName);
        Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
        return new Animation(frameDuration, region, playMode);
    }

    public Animation createAnimation(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
        return new Animation(frameDuration, region, playMode);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AnimationData)) {
            return false;
        } else {
            AnimationData otherData = (AnimationData) other;
            return otherData.atlasName.equals(atlasName) && otherData.animationName.equals(animationName);
        }
    }

    @Override
    public int hashCode() {
        return animationName.hashCode();
    }
}
