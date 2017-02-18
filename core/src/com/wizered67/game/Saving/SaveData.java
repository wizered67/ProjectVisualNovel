package com.wizered67.game.saving;

import com.wizered67.game.assets.Assets;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.MusicManager;

import java.util.HashMap;
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
    public Assets assets;
}
