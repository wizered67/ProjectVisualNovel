package com.wizered67.game.conversations.scene;

/**
 * Defines a base of a character that SceneCharacters can be created from.
 * @author Adam Victor
 */
public class CharacterDefinition {
    /** Identifier for this character. */
    private String identifier;
    /** The name the player knows this character as. */
    private String knownName;
    /** Identifier of the sound used when this character speaks. */
    private String speakingSound;
    /** The default speaking sound for all characters. */
    public static final String DEFAULT_SPEAKING_SOUND = "talksoundmale.wav";

    public CharacterDefinition(String identifier, String knownName, String speakingSound) {
        this.identifier = identifier;
        this.knownName = knownName;
        this.speakingSound = speakingSound != null ? speakingSound : DEFAULT_SPEAKING_SOUND;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getKnownName() {
        return knownName;
    }

    public String getSpeakingSound() {
        return speakingSound;
    }

    public void setKnownName(String name) {
        knownName = name;
    }
}
