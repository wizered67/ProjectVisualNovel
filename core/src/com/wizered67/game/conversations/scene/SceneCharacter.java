package com.wizered67.game.conversations.scene;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wizered67.game.conversations.CompleteEvent;
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

    /** Whether the character this SceneCharacter represents is currently speaking. */
    private boolean isSpeaking;
    /** The sound to be used when the character is speaking. */
    private String speakingSound;
    /** The default speaking sound for all characters. */
    public static final String DEFAULT_SPEAKING_SOUND = "talksoundmale.wav";

    /** No argument constructor */
    public SceneCharacter() {
        sprite = new Sprite();
        identifier = "";
    }

    /** Creates a SceneCharacter based on the CharacterDefinition */
    public SceneCharacter(SceneManager manager, CharacterDefinition characterDefinition) {
        this(characterDefinition.getIdentifier(), manager, characterDefinition.getSpeakingSound());
    }

    /** Creates a SceneCharacter with the default speaking sound. */
    public SceneCharacter(SceneManager m) {
        this("", m, DEFAULT_SPEAKING_SOUND);
    }
    /** Creates a SceneCharacter with id ID and speaking sound SOUND. */
    public SceneCharacter(String id, SceneManager m, String sound) {
        super();
        identifier = id;
        manager = m;
        wasFinished = false;
        isSpeaking = false;
        if (sound != null) {
            speakingSound = sound;
        } else {
            speakingSound = DEFAULT_SPEAKING_SOUND;
        }
        sprite = new Sprite();
        sprite.setAlpha(0);
        removed = false;
        //sprite.setScale(2, 2);
    }
    /** Stores variables to save important information. */
    public void save() {

    }
    /** Returns the name of this SceneCharacter's speaking sound. */
    public String getSpeakingSound() {
        return speakingSound;
    }
    /** Sets this SceneCharacter's speaking sound to NEWSOUND. */
    public void setSpeakingSound(String newSound) {
        speakingSound = newSound;
    }

    public String getKnownName() {
        return SceneManager.getCharacterDefinition(identifier).getKnownName();
    }

    /** Sets speaking status to SPEAKING. */
    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    /** Sets the SceneManager for this SceneCharacter to SM and adds the SceneCharacter to that scene. */
    public void addToScene(SceneManager sm) {
        //Sets depth and also adds to sorted list!
        setDepth(sm, depth);
    }
    /** Removes this SceneCharacter from the SceneManager. */
    public void removeFromScene() {
        if (manager != null) {
            manager.removeCharacter(identifier);
            manager = null;
        }
        removed = true;
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
