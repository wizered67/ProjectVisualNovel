package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a character in the game. Contains information about the
 * character's sprite, animation, name, and speaking sound.
 * @author Adam Victor
 */
public class CharacterSprite {
    private TextureRegion currentSprite;
    private Animation currentAnimation;
    private String animationName;
    private Map<String, Animation> allAnimations;
    private final Vector2 position = new Vector2();
    private boolean looping;
    private float stateTime;
    private boolean isVisible;
    private SceneManager manager;
    private boolean wasFinished;
    private boolean isSpeaking;
    private String knownName;
    private String speakingSound;
    private Sprite sprite;
    private static final String DEFAULT_SPEAKING_SOUND = "talksoundmale";
    int scale;
    /** Creates a CharacterSprite with the no animations and default speaking sound. */
    public CharacterSprite(SceneManager m) {
        this(m, new HashMap<String, Animation>(), DEFAULT_SPEAKING_SOUND);
    }
    /** Creates a CharacterSprite with animations ANIMATIONS. */
    public CharacterSprite(SceneManager m, Map<String, Animation> animations) {
        this(m, animations, DEFAULT_SPEAKING_SOUND);
    }
    /** Creates a CharacterSprite with animations ANIMATIONS and speaking sound SOUND. */
    public CharacterSprite(SceneManager m, Map<String, Animation> animations, String sound) {
        allAnimations = animations;
        manager = m;
        isVisible = true;
        looping = false;
        stateTime = 0;
        wasFinished = false;
        isSpeaking = false;
        animationName = "";
        if (sound != null) {
            speakingSound = sound;
        } else {
            speakingSound = DEFAULT_SPEAKING_SOUND;
        }
        sprite = new Sprite();
        scale = 2;
        //sprite.setScale(2, 2);
    }
    /** Returns the name of this CharacterSprite's speaking sound. */
    public String getSpeakingSound() {
        return speakingSound;
    }
    /** Sets this CharacterSprite's speaking sound to NEWSOUND. */
    public void setSpeakingSound(String newSound) {
        speakingSound = newSound;
    }
    /** Returns the display name of the character represented by this CharacterSprite. */
    public String getKnownName() {
        return knownName;
    }
    /** Sets the display name of this character to NEWNAME. */
    public void setKnownName(String newName) {
        knownName = newName;
    }
    /** Adds the Animation ANIM to this CharacterSprite under the name NAME. */
    public void addAnimation(String name, Animation anim) {
        if (!allAnimations.containsKey(name)) {
            allAnimations.put(name, anim);
        }
    }
    /** Sets this CharacterSprite's current sprite to the TextureRegion TEXTURE. */
    public void setCurrentSprite(TextureRegion texture) {
        currentSprite = texture;
        sprite.setTexture(currentSprite.getTexture());
        sprite.setRegion(currentSprite);
        sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth(), currentSprite.getRegionHeight());
        sprite.setOrigin(sprite.getWidth() / 2, 0);
    }
    /** Switches this CharacterSprite's animation to the one named NAME. */
    public boolean setCurrentAnimation(String name) {
        if (!animationName.equalsIgnoreCase(name)) {
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
    /** Returns the name of this CharacterSprite's current Animation. */
    public String getAnimationName() {
        return animationName;
    }

    /** Updates the stateTime of the current Animation by DELTA STATE TIME and
     * updates the sprite to the Animation's current sprite. If the Animation is
     * completed it alerts the SceneManager.
     */
    public void updateAnimation(float deltaStateTime) {
        if (isVisible && currentAnimation != null) {
            stateTime += deltaStateTime;
            currentSprite = currentAnimation.getKeyFrame(stateTime, looping);
            sprite.setTexture(currentSprite.getTexture());
            sprite.setRegion(currentSprite);
            sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth() * scale, currentSprite.getRegionHeight() * scale);
            sprite.setOrigin(sprite.getWidth() * scale / 2, 0);
            //sprite.setCenter(sprite.getWidth() * scale / 2, 0);
            if (currentAnimation.isAnimationFinished(stateTime) && !wasFinished) {
                manager.finishedAnimation(this);
                wasFinished = true;
            }

        }
    }
    /** Draws this CharacterSprite's current sprite to the BATCH. */
    public void draw(Batch batch) {
        if (!isVisible || currentSprite == null) {
            return;
        }
        sprite.draw(batch);
        /*
        batch.draw(currentSprite, position.x + (direction == -1 ? currentSprite.getRegionWidth() * scale : 0), position.y,
                currentSprite.getRegionWidth() * scale * direction,
                currentSprite.getRegionHeight() * scale);
                */
    }
    /** Sets current position to NEW POSITION. */
    public void setPosition(Vector2 newPosition){
        position.set(newPosition);
    }
    /** Sets current x to NEWX and current y to NEWY. */
    public void setPosition(float newX, float newY) {
        position.set(newX, newY);
    }
    /** Returns this CharacterSprite's current position. */
    public Vector2 getPosition() {
        return position;
    }
    /** Sets this CharacterSprite's visibility to VISIBLE. */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    /** Sets speaking status to SPEAKING. */
    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }
    /** Sets the direction of the sprite to DIRECTION. */
    public void setDirection(int direction) {
        sprite.setScale(Math.abs(sprite.getScaleX()) * direction, sprite.getScaleY());
    }
}
