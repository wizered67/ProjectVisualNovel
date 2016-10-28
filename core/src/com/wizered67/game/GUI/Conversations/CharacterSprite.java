package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam on 10/27/2016.
 */
public class CharacterSprite {
    private TextureRegion currentSprite;
    private Animation currentAnimation;
    private String animationName;
    private Map<String, Animation> allAnimations;
    private final Vector2 position = new Vector2();
    private boolean isVisible;
    private boolean looping;
    private float stateTime;
    private AnimationManager manager;
    private float scale;
    private boolean wasFinished;

    public CharacterSprite(AnimationManager m) {
        this(m, new HashMap<String, Animation>());
    }

    public CharacterSprite(AnimationManager m, Map<String, Animation> animations) {
        allAnimations = animations;
        manager = m;
        isVisible = true;
        looping = false;
        stateTime = 0;
        scale = 2f;
        wasFinished = false;
    }

    public void addAnimation(String name, Animation anim) {
        if (!allAnimations.containsKey(name)) {
            allAnimations.put(name, anim);
        }
    }

    public void setCurrentSprite(TextureRegion sprite) {
        currentSprite = sprite;
    }

    public boolean setCurrentAnimation(String name) {
        if (animationName != name) {
            stateTime = 0;
            animationName = name;
        }
        currentAnimation = allAnimations.get(name);
        wasFinished = false;
        if (currentAnimation == null) {
            System.out.println("No animation found: " + name);
        }
        return currentAnimation != null;
    }

    public String getAnimationName() {
        return animationName;
    }

    public void updateAnimation(float deltaStateTime) {
        if (isVisible && currentAnimation != null) {
            stateTime += deltaStateTime;
            currentSprite = currentAnimation.getKeyFrame(stateTime, looping);

            if (currentAnimation.isAnimationFinished(stateTime) && !wasFinished) {
                manager.finishedAnimation(this);
                wasFinished = true;
            }

        }
    }

    public void draw(Batch batch) {
        if (!isVisible || currentSprite == null) {
            return;
        }
        batch.draw(currentSprite, position.x, position.y, currentSprite.getRegionWidth() * scale,
                currentSprite.getRegionHeight() * scale);
    }

    public void setPosition(Vector2 newPosition){
        position.set(newPosition);
    }

    public void setPosition(float newX, float newY) {
        position.set(newX, newY);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
