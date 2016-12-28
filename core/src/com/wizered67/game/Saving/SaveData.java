package com.wizered67.game.Saving;

import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneManager;
import com.wizered67.game.MusicManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Stores important game information to be serialized for saving and loading.
 * @author Adam Victor
 */
public class SaveData {
    public MusicManager musicManager;
    public ConversationController conversationController;
    /** Maps scripting language names to a map of variable names to values. */
    public Map<String, Map<String, Object>> scriptingVariables = new HashMap<String, Map<String, Object>>();
}
