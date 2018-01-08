package com.wizered67.game.scripting.lua;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.scene.CharacterDefinition;
import com.wizered67.game.conversations.scene.SceneManager;
import org.luaj.vm2.LuaValue;

/**
 * Contains methods that can be called in Lua scripts using 'game:'.
 * @author Adam Victor
 */
public class LuaGameMethods {
    public int randomRange(int lowerBound, int upperBound) {
        return MathUtils.random(lowerBound, upperBound);
    }

    public void getTextInput(final String variableName, String title) {
        getTextInput(variableName, title, "", "");
    }

    public void getTextInput(final String variableName, String title, String defaultText, String hint) {
        ConversationController.scriptManager("Lua").setValue(variableName, LuaValue.NIL);
        Input.TextInputListener inputListener = new Input.TextInputListener() {
            @Override
            public void input(String result) {
                ConversationController.scriptManager("Lua").setValue(variableName, result);
            }

            @Override
            public void canceled() {
                ConversationController.scriptManager("Lua").setValue(variableName, "");
            }
        };
        //Gdx.input.getTextInput(inputListener, title, text, hint);
        GameManager.guiManager().getTextInputUI().display(title, defaultText, hint, inputListener);
    }

    public boolean isValidTextInput(String input) {
        return !input.trim().isEmpty();
    }

    public void setCharacterName(String character, String name) {
        CharacterDefinition definition = SceneManager.getCharacterDefinition(character);
        if (definition != null) {
            definition.setKnownName(name);
        }
    }
}
