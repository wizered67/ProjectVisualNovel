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
 * through a ChangeBranchCommand. Conversations also have assignments, a mapping
 * of string to commands to be used as shortcuts in the conversation.
 * @author Adam Victor
 */
public class Conversation {
    /** Map that pairs a branch name with a list of ConversationCommands. */
    private Map<String, LinkedList<ConversationCommand>> branches;
    /** Mapping between text shortcuts in the form @c{string} and ConversationCommands. */
    private Map<String, ConversationCommand> assignments;
    /** The name of the file used to load this conversation. */
    private String name;

    /** Creates a new Conversation and initialize BRANCHES to an empty map. */
    public Conversation(String filename) {
        name = filename;
        branches = new HashMap<String, LinkedList<ConversationCommand>>();
        assignments = new HashMap<String, ConversationCommand>();
    }
    /** Adds a new BRANCH to BRANCHES with name NAME. */
    public void addBranch(String name, LinkedList<ConversationCommand> branch) {
        branches.put(name.toLowerCase(), branch);
    }
    /** Returns the branch with the name NAME. */
    public LinkedList<ConversationCommand> getBranch(String name) {
        return branches.get(name.toLowerCase());
    }
    /** Returns a set of String with the names of all BRANCHES. */
    public Set<String> getAllBranches() {
        return branches.keySet();
    }
    /** Return the ConversationCommand assigned to the string NAME. */
    public ConversationCommand getAssignment(String name) {
        return assignments.get(name.toLowerCase());
    }
    /** Adds the mapping of string NAME to ConversationCommand COMMAND. */
    public void addAssignment(String name, ConversationCommand command) {
        assignments.put(name.toLowerCase(), command);
    }
    /** Sets the assignment map to MAP. */
    public void setAssignments(Map<String, ConversationCommand> map) {
        assignments = map;
    }
    /** Returns the full assignments map. */
    public Map<String, ConversationCommand> getAllAssignments() {
        return assignments;
    }
    /** Returns the name of the file used to load this conversation. */
    public String getName() {
        return name;
    }
}
