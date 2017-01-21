package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Scene with a set of characters that are updated
 * and drawn each frame. Alerts the ConversationController when
 * an Animation is completed.
 * @author Adam Victor
 */
public class SceneManager {
    /** Set of all CharacterSprites in this scene. */
    private Set<CharacterSprite> characterSprites;
    /** Reference to the current ConversationController so that it can be alerted of
     * Animations being completed. */
    private ConversationController conversationController;
    /** SpriteBatch used to draw Sprites for each CharacterSprite. */
    private transient SpriteBatch batch;
    /** Maps character names to their corresponding CharacterSprite. */
    private static HashMap<String, CharacterSprite> allCharacters = new HashMap<String, CharacterSprite>();
    /** Texture to draw as background. */
    private transient Texture background;
    /** Identifier used for background texture. */
    private String backgroundIdentifier;

    /** No argument constructor. Needed for serialization.*/
    public SceneManager() {
        conversationController = null;
        characterSprites = null;
        batch = new SpriteBatch();
        allCharacters = null;
        background = null;
    }
    /** Creates a new SceneManager with ConversationController MW and no CharacterSprites. */
    public SceneManager(ConversationController mw) {
        conversationController = mw;
        characterSprites = new HashSet<>();
        batch = new SpriteBatch();
        backgroundIdentifier = "";
    }
    /** Called each frame to draw the background, update the Animation of each CharacterSprite, and
     * then draw them. DELTA is the amount of time that has elapsed since the
     * last frame.
     */
    public void update(float delta) {
        batch.begin();
        if (background != null) {
            batch.draw(background, 0, 0);
        }
        for (CharacterSprite sprite : characterSprites) {
            //System.out.println("Updating " + sprite.getKnownName());
            sprite.updateAnimation(delta);
            sprite.draw(batch);
        }
        batch.end();
    }
   /** Adds the CharacterSprite with identifier IDENTIFIER to this scene. */
    public void addCharacter(String identifier) {
        characterSprites.add(allCharacters.get(identifier.toLowerCase()));
    }
    /** Removes the CharacterSprite with identifier IDENTIFIER from the scene. */
    public void removeCharacter(String identifier) {
        characterSprites.remove(allCharacters.get(identifier.toLowerCase()));
    }

    public static void createCharacter(String identifier, String name, String speakingSound) {
        if (!allCharacters.containsKey(identifier.toLowerCase())) {
            CharacterSprite newCharacter = new CharacterSprite(identifier.toLowerCase(), null, speakingSound);
            newCharacter.setKnownName(name);
            allCharacters.put(identifier.toLowerCase(), newCharacter);
        }
    }
    /** Returns the CharacterSprite with the identifier IDENTIFIER, or
     * outputs an error if no such character is in the scene.
     */
    public CharacterSprite getCharacterByIdentifier(String identifier) {
        CharacterSprite character = allCharacters.get(identifier.toLowerCase());
        if (character == null) {
            GameManager.error("No character of name " + identifier.toLowerCase());
        }
        return character;
    }
    /** Sets the background to the Texture with identifier IMAGEIDENTIFIER. */
    public void setBackground(String imageIdentifier) {
        if (!backgroundIdentifier.equals(imageIdentifier)) {
            backgroundIdentifier = imageIdentifier;
            if (!GameManager.assetManager().isLoaded(imageIdentifier)) {
                GameManager.error("Background with filename " + imageIdentifier + " is not loaded.");
            }
            background = GameManager.assetManager().get(imageIdentifier);
        }
    }
    /** Returns the identifier of the current background. */
    public String getBackgroundIdentifier() {
        return backgroundIdentifier;
    }
    /** Passes the complete event to the ConversationController to be passed to the last executed command. */
    public void complete(CompleteEvent event) {
        conversationController.complete(event);
    }
}
