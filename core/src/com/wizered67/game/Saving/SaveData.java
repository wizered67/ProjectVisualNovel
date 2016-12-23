package com.wizered67.game.Saving;

import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneManager;
import com.wizered67.game.MusicManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Adam on 12/20/2016.
 */
public class SaveData {
    public MusicManager musicManager;
    //public String conversation;
    //public LinkedList<ConversationCommand> branch;
    public transient SceneManager sceneManager;
    public ConversationController conversationController;
    public Map<String, Map<String, Object>> scriptingVariables = new HashMap<String, Map<String, Object>>();
}
