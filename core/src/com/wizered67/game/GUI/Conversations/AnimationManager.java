package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;

import java.util.ArrayList;

/**
 * Created by Adam on 10/27/2016.
 */
public class AnimationManager {
    private ArrayList<CharacterSprite> characterSprites;
    private MessageWindow messageWindow;
    private SpriteBatch batch;

    public AnimationManager(MessageWindow mw) {
        messageWindow = mw;
        characterSprites = new ArrayList<CharacterSprite>();
        batch = new SpriteBatch();
    }

    public void update(float delta) {
        if (characterSprites.size() == 0) {
            CharacterSprite edgeworth = new CharacterSprite(this, GameManager.loadedAnimations().get("Edgeworth"));
            edgeworth.setPosition(new Vector2(100, GUIManager.getTextboxTop()));
            edgeworth.setCurrentAnimation("Idle");
            characterSprites.add(edgeworth);
        }
        batch.begin();
        for (CharacterSprite sprite : characterSprites) {
            sprite.updateAnimation(delta);
            sprite.draw(batch);
        }
        batch.end();
    }

    //Only debug, for now applies straight to edgeworth
    public boolean setAnimation(String anim) {
        return characterSprites.get(0).setCurrentAnimation(anim);
    }

    public void finishedAnimation(CharacterSprite characterSprite) {
        messageWindow.animationComplete(characterSprite.getAnimationName());
        System.out.println("Animation finished for " + characterSprite);
    }
}
