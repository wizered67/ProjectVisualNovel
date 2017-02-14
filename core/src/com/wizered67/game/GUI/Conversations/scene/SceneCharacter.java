package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GameManager;

/**
 * Represents a character in the game. Contains information about the
 * character's sprite, animation, name, and speaking sound.
 * Note: this class has a natural ordering that is inconsistent with equals.
 * It uses depth in compareTo for ordering and uses identifier for equals.
 * @author Adam Victor
 */
public class SceneCharacter extends SceneEntity {
    /** The unique identifier of this character. Specified in resources.xml. */
    private String identifier;
    /** Animation object containing all frames of animation. */
    private transient Animation currentAnimation;
    /** The name of the current animation. Used to tell if the animation is being changed. */
    private String animationName;
    /** Whether the Animation should loop. */
    private boolean looping;
    /** The stateTime used by Animation object to determine which frame should be displayed. */
    private float stateTime;
    /** Whether the animation being displayed has been finished. */
    private boolean wasFinished;
    /** Whether the character this SceneCharacter represents is currently speaking. */
    private boolean isSpeaking;
    /** The name to be displayed for the character. */
    private String knownName;
    /** The sound to be used when the character is speaking. */
    private String speakingSound;
    /** The default speaking sound for all characters. */
    public static final String DEFAULT_SPEAKING_SOUND = "talksoundmale.wav";

    /** No argument constructor */
    public SceneCharacter() {
        sprite = new Sprite();
        identifier = "";
    }
    /** Creates a SceneCharacter with the default speaking sound. */
    public SceneCharacter(SceneManager m) {
        this("", m, DEFAULT_SPEAKING_SOUND);
    }
    /** Creates a SceneCharacter with id ID and speaking sound SOUND. */
    public SceneCharacter(String id, SceneManager m, String sound) {
        identifier = id;
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
        //sprite.setScale(2, 2);
    }
    /** Stores variables to save important information. */
    public void save() {

    }
    /** Reloads SceneCharacter. */
    public void reload() {
        manager.allCharacters().put(identifier, this);
        currentAnimation = GameManager.assetManager().getAnimation(animationName);
        updateSprite();
    }
    /** Returns the name of this SceneCharacter's speaking sound. */
    public String getSpeakingSound() {
        return speakingSound;
    }
    /** Sets this SceneCharacter's speaking sound to NEWSOUND. */
    public void setSpeakingSound(String newSound) {
        speakingSound = newSound;
    }
    /** Returns the display name of the character represented by this SceneCharacter. */
    public String getKnownName() {
        return knownName;
    }
    /** Sets the display name of this character to NEWNAME. */
    public void setKnownName(String newName) {
        knownName = newName;
    }

    /** Switches this SceneCharacter's animation to the one named NAME. Returns true if animation is valid. */
    public boolean setCurrentAnimation(String name) {
        if (!animationName.equalsIgnoreCase(name)) {
            stateTime = 0;
            animationName = name;
        }
        currentAnimation = GameManager.assetManager().getAnimation(name);
        wasFinished = false;
        if (currentAnimation == null) {
            GameManager.error("No animation found: " + name);
        }
        return currentAnimation != null;
    }
    /** Returns the name of this SceneCharacter's current Animation. */
    public String getAnimationName() {
        return animationName;
    }

    /** Updates the stateTime of the current Animation by DELTA STATE TIME and
     * updates the sprite to the Animation's current sprite. If the Animation is
     * completed it alerts the SceneManager. Also updates fading in or out the sprite.
     */
    @Override
    public void update(float deltaStateTime) {
        super.update(deltaStateTime);
        if (currentAnimation != null) { //todo decide if should check visibility first
            stateTime += deltaStateTime;
            updateSprite();
            //sprite.setCenter(sprite.getWidth() * scale / 2, 0);
            if (currentAnimation.isAnimationFinished(stateTime) && !wasFinished) {
                manager.complete(CompleteEvent.animationEnd(animationName));
                wasFinished = true;
            }
        }
    }
    /** Updates the sprite to the correct animation frame. */
    private void updateSprite() {
        if (currentAnimation != null) {
            TextureRegion currentTexture = currentAnimation.getKeyFrame(stateTime, looping);
            sprite.setTexture(currentTexture.getTexture());
            sprite.setRegion(currentTexture);
            sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
            sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            //sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth() * scale.x, currentSprite.getRegionHeight() * scale.y);
            sprite.setOrigin(Math.abs(sprite.getWidth()) / 2, 0);
        }
    }
    /** Sets this SceneCharacter's current sprite to the TextureRegion TEXTURE. */
    public void setCurrentSprite(TextureRegion texture) {
        TextureRegion currentTexture = texture;
        sprite.setTexture(currentTexture.getTexture());
        sprite.setRegion(currentTexture);
        //sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth(), currentSprite.getRegionHeight());
        sprite.setOrigin(sprite.getWidth() / 2, 0);
    }
    /** Sets speaking status to SPEAKING. */
    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }
    /** Sets the direction of the sprite to DIRECTION. */
    public void setDirection(int direction) {
        sprite.setScale(Math.abs(sprite.getScaleX()) * direction, sprite.getScaleY());
        //scale.x = Math.abs(scale.x) * direction;
    }
    /** Sets the SceneManager for this SceneCharacter to SM and adds the SceneCharacter to that scene. */
    public void addToScene(SceneManager sm) {
        manager = sm;
        manager.addCharacter(identifier);
        inScene = true;
        setDepth(manager, depth);
        removed = false;
        //setDepth(manager, 0);
    }
    /** Removes this SceneCharacter from the SceneManager. */
    public void removeFromScene() {
        if (manager != null) {
            manager.removeCharacter(identifier);
            manager = null;
        }
        removed = true;
        inScene = false;
    }
    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SceneCharacter) {
            return ((SceneCharacter) o).identifier.equals(identifier);
        }
        return false;
    }
}
