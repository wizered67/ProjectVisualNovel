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
    /** TextureRegion to be drawn for the current frame of animation. */
    private TextureRegion currentSprite;
    /** Animation object containing all frames of animation. */
    private Animation currentAnimation;
    /** The name of the current animation. Used to tell if the animation is being changed. */
    private String animationName;
    /** A mapping of animation names to Animation objects so that animation can be set with just the name. */
    private Map<String, Animation> allAnimations;
    /** Vector2 containing the position of the sprite to be drawn. */
    private final Vector2 position = new Vector2();
    /** Whether the Animation should loop. */
    private boolean looping;
    /** The stateTime used by Animation object to determine which frame should be displayed. */
    private float stateTime;
    /** The SceneManager containing this sprite and all others. */
    private SceneManager manager;
    /** Whether the animation being displayed has been finished. */
    private boolean wasFinished;
    /** Whether the character this CharacterSprite represents is currently speaking. */
    private boolean isSpeaking;
    /** The name to be displayed for the character. */
    private String knownName;
    /** The sound to be used when the character is speaking. */
    private String speakingSound;
    /** The Sprite object used to draw frames of animation. */
    private Sprite sprite;
    /** The default speaking sound for all characters. */
    private static final String DEFAULT_SPEAKING_SOUND = "talksoundmale";
    /** How much the sprite drawn should be scaled. */
    private int scale;
    /** How much the alpha of the sprite being drawn should change per second. */
    private float fadePerSecond;

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
        sprite.setAlpha(0);
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
     * completed it alerts the SceneManager. Also updates fading in or out the sprite.
     */
    public void updateAnimation(float deltaStateTime) {
        if (fadePerSecond != 0) {
            float alpha = sprite.getColor().a;
            alpha += deltaStateTime * fadePerSecond;
            sprite.setAlpha(alpha);
            if (alpha <= 0) {
                setVisible(false);
            } else if (alpha >= 1) {
                setVisible(true);
            }
        }
        if (isVisible() && currentAnimation != null) {
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
        if (!isVisible() || currentSprite == null) {
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
        if (visible) {
            sprite.setAlpha(1);
        } else {
            sprite.setAlpha(0);
        }
        fadePerSecond = 0;
    }
    /** Sets speaking status to SPEAKING. */
    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }
    /** Sets the direction of the sprite to DIRECTION. */
    public void setDirection(int direction) {
        sprite.setScale(Math.abs(sprite.getScaleX()) * direction, sprite.getScaleY());
    }
    /** Sets how fast the sprite should fade in or out. FADEAMOUNT is alpha per second. */
    public void setFade(float fadeAmount) {
        fadePerSecond = fadeAmount;
    }
    /** Whether this sprite is visible (ie alpha is not 0). */
    public boolean isVisible() {
        return sprite.getColor().a > 0;
    }
}
