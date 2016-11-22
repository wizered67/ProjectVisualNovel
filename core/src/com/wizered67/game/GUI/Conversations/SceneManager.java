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
 * Created by Adam on 10/27/2016.
 */
public class SceneManager {
    private ArrayList<CharacterSprite> characterSprites;
    private MessageWindow messageWindow;
    private SpriteBatch batch;
    private HashMap<String, CharacterSprite> allCharacters;

    public SceneManager(MessageWindow mw) {
        messageWindow = mw;
        characterSprites = new ArrayList<CharacterSprite>();
        batch = new SpriteBatch();
        allCharacters = new HashMap<String, CharacterSprite>();
    }

    public void update(float delta) {
        batch.begin();
        for (CharacterSprite sprite : characterSprites) {
            sprite.updateAnimation(delta);
            sprite.draw(batch);
        }
        batch.end();
    }
    
    public void addCharacter(String name) {
        addCharacter(name, name, null);
    }

    public void addCharacter(String name, String animations, String speakingSound) {
        if (!allCharacters.containsKey(name)) {
            CharacterSprite newCharacter = new CharacterSprite(this, GameManager.loadedAnimations().get(animations), speakingSound);
            newCharacter.setKnownName(name);
            allCharacters.put(name, newCharacter);
            characterSprites.add(newCharacter);
        }
    }

    public CharacterSprite getCharacterByName(String name) {
        CharacterSprite character = allCharacters.get(name);
        if (character == null) {
            GameManager.error("No character of name " + name);
        }
        return character;
    }

    public void finishedAnimation(CharacterSprite characterSprite) {
        messageWindow.animationComplete(characterSprite.getAnimationName());
        System.out.println("Animation finished for " + characterSprite);
    }
}
