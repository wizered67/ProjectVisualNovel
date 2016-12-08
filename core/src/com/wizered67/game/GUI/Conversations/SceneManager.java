package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Scene with a set of characters that are updated
 * and drawn each frame. Alerts the MessageWindow when an Animation is
 * completed.
 * @author Adam Victor
 */
public class SceneManager {
    /** List of all CharacterSprites in this scene. */
    private ArrayList<CharacterSprite> characterSprites;
    /** Reference to the current MessageWindow so that it can be alerted of
     * Animations being completed.
     */
    private MessageWindow messageWindow;
    /** SpriteBatch used to draw Sprites for each CharacterSprite. */
    private SpriteBatch batch;
    /** Maps character names to their corresponding CharacterSprite. */
    private HashMap<String, CharacterSprite> allCharacters;
    /** Creates a new SceneManager with MessageWindow MW and no CharacterSprites. */
    public SceneManager(MessageWindow mw) {
        messageWindow = mw;
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
            CharacterSprite newCharacter = new CharacterSprite(this, GameManager.loadedAnimations().get(animations), speakingSound);
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

    /** Alerts the MessageWindow that CharacterSprite CHARACTER SPRITE has finished
     * its current animation.
     */
    public void finishedAnimation(CharacterSprite characterSprite) {
        messageWindow.animationComplete(characterSprite.getAnimationName());
        System.out.println("Animation finished for " + characterSprite);
    }
}