package com.wizered67.game.GUI.Conversations;

import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Represents a Conversation. Conversations are split into BRANCHES, each of
 * which has a unique name. Each branch is a list of ConversationCommands to
 * executed consecutively. A branch can also switch to another in the middle
 * through a ChangeBranchCommand.
 * @author Adam Victor
 */
public class Conversation {
    /** Map that pairs a branch name with a list of ConversationCommands. */
    private Map<String, LinkedList<ConversationCommand>> branches;
    /** Creates a new Conversation and initialize BRANCHES to an empty map. */
    public Conversation() {
        branches = new HashMap<String, LinkedList<ConversationCommand>>();
    }
    /** Adds a new BRANCH to BRANCHES with name NAME. */
    public void addBranch(String name, LinkedList<ConversationCommand> branch) {
        branches.put(name, branch);
    }
    /** Returns the branch with the name NAME. */
    public LinkedList<ConversationCommand> getBranch(String name) {
        return branches.get(name);
    }
    /** Returns a set of String with the names of all BRANCHES. */
    public Set<String> getAllBranches() {
        return branches.keySet();
    }


}
