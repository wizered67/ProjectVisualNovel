package com.wizered67.game.GUI.Conversations;

import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Adam on 10/24/2016.
 */
public class Conversation {
    private Map<String, LinkedList<ConversationCommand>> branches;

    public Conversation() {
        branches = new HashMap<String, LinkedList<ConversationCommand>>();
    }

    public void addBranch(String name, LinkedList<ConversationCommand> branch) {
        branches.put(name, branch);
    }

    public LinkedList<ConversationCommand> getBranch(String name) {
        return branches.get(name);
    }
}
