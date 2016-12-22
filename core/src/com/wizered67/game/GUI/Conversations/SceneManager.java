package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wizered67.game.GameManager;
import com.wizered67.game.Saving.SaveData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a Scene with a set of characters that are updated
 * and drawn each frame. Alerts the ConversationController when
 * an Animation is completed.
 * @author Adam Victor
 */
public class SceneManager {
    /** List of all CharacterSprites in this scene. */
    private ArrayList<CharacterSprite> characterSprites;
    /** Reference to the current ConversationController so that it can be alerted of
     * Animations being completed. */
    private ConversationController conversationController;
    /** SpriteBatch used to draw Sprites for each CharacterSprite. */
    private transient SpriteBatch batch;
    /** Maps character names to their corresponding CharacterSprite. */
    private HashMap<String, CharacterSprite> allCharacters;

    /** No argument constructor. */
    public SceneManager() {
        conversationController = null;
        characterSprites = null;
        batch = new SpriteBatch();
        allCharacters = null;
    }
    /** Creates a new SceneManager with ConversationController MW and no CharacterSprites. */
    public SceneManager(ConversationController mw) {
        conversationController = mw;
        characterSprites = new ArrayList<CharacterSprite>();
        batch = new SpriteBatch();
        allCharacters = new HashMap<String, CharacterSprite>();
    }
    /** Called each frame to update the Animation of each CharacterSprite and
     * then draw them. DELTA is the amount of time that has elapsed since the
     * last frame.
     */
    public void update(float delta) {
        batch.begin();
        for (CharacterSprite sprite : characterSprites) {
            sprite.updateAnimation(delta);
            sprite.draw(batch);
        }
        batch.end();
    }
    /** Adds a new CharacterSprite with name NAME, no animations, and the
     * default speaking sound.
     */
    public void addCharacter(String name) {
        addCharacter(name, name, null);
    }

    /** Adds a new CharacterSprite with name NAME, animation set named ANIMATIONS,
     * and speaking sound SPEAKING SOUND.
     */
    public void addCharacter(String name, String animations, String speakingSound) {
        if (!allCharacters.containsKey(name)) {
            CharacterSprite newCharacter = new CharacterSprite(this, animations, speakingSound);
            newCharacter.setKnownName(name);
            allCharacters.put(name, newCharacter);
            characterSprites.add(newCharacter);
        }
    }

    /** Returns the CharacterSprite with the name NAME, or
     * outputs an error if no such character is in the same.
     */
    public CharacterSprite getCharacterByName(String name) {
        CharacterSprite character = allCharacters.get(name);
        if (character == null) {
            GameManager.error("No character of name " + name);
        }
        return character;
    }
    /** Passes the complete event to the ConversationController to be passed to the last executed command. */
    public void complete(CompleteEvent event) {
        conversationController.complete(event);
    }
    /** Saves data to SaveData to be loaded later. */
    public void save(SaveData data) {
        data.sceneManager = this;
    }
    /** Reloads data from SaveData. */
    public void reload() {//SaveData data) { //todo fix
        /*
        for (CharacterSprite characterSprite : allCharacters.values()) {
            characterSprite.reload(data);
        }
        */
    }
}
