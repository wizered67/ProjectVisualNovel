package com.wizered67.game.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Stores the information necessary to create a specific Animation of a TextureAtlas.
 * @author Adam Victor
 */
public class AnimationData {
    private String atlasIdentifierName;
    private String animationName;
    private float frameDuration;
    private Animation.PlayMode playMode;

    public AnimationData() {}
    public AnimationData(String atlasIdentifier, String animation, float duration, Animation.PlayMode mode) {
        atlasIdentifierName = atlasIdentifier;
        animationName = animation;
        frameDuration = duration;
        playMode = mode;
    }

    public String getAtlasAnimation() {
        return atlasIdentifierName + "_" + animationName;
    }
    /*
    public Animation<TextureRegion> createAnimation() {
        if (!GameManager.assetManager().isLoaded(atlasIdentifierName)) {
            GameManager.error("Atlas " + atlasIdentifierName + " was not loaded yet.");
            return null;
        }
        TextureAtlas atlas = GameManager.assetManager().get(atlasIdentifierName);
        Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
        return new Animation<TextureRegion>(frameDuration, region, playMode);
    }
    */
    public Animation<TextureRegion> createAnimation(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> region = atlas.findRegions(animationName);
        return new Animation<TextureRegion>(frameDuration, region, playMode);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AnimationData)) {
            return false;
        } else {
            AnimationData otherData = (AnimationData) other;
            return otherData.atlasIdentifierName.equals(atlasIdentifierName) && otherData.animationName.equals(animationName);
        }
    }

    @Override
    public int hashCode() {
        return animationName.hashCode();
    }
}
