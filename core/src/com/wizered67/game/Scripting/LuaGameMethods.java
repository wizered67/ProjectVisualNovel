package com.wizered67.game.Scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.GUIManager;
import org.luaj.vm2.LuaValue;

/**
 * Contains methods that can be called in Lua scripts using 'game:'.
 * @author Adam Victor
 */
public class LuaGameMethods {
    public static int randomRange(int lowerBound, int upperBound) {
        return MathUtils.random(lowerBound, upperBound);
    }

    public static void printAll(int[] nums) {
        for (int i : nums) {
            System.out.println(i);
        }
    }

    public static void addToTranscript(String speaker, String phrase) {
        GUIManager.conversationController().getTranscript().addMessage(speaker, phrase);
    }

    public static void openTranscript() {
        GUIManager.toggleTranscript();
    }

    public static void getInput(final String variableName) {
        ConversationController.scriptManager("Lua").setValue(variableName, LuaValue.NIL);
        Input.TextInputListener inputListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                ConversationController.scriptManager("Lua").setValue(variableName, text);
            }

            @Override
            public void canceled() {
                ConversationController.scriptManager("Lua").setValue(variableName, -1);
            }
        };
        Gdx.input.getTextInput(inputListener, "Test input", "", "");
    }
}
