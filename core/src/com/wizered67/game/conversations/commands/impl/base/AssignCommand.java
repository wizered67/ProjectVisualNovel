package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

import java.util.Map;

/**
 * ConversationCommand used to assign a mapping of strings to ConversationCommands
 * for use later in the conversation.
 * @author Adam Victor
 */
public class AssignCommand implements ConversationCommand {
    /** Mapping of Strings to ConversationCommands. */
    private Map<String, ConversationCommand> assignments;

    /** No arguments constructor. */
    public AssignCommand() {
        assignments = null;
    }
    /** Create an AssignCommand with assignments map of MAP. */
    public AssignCommand(Map<String, ConversationCommand> map) {
        assignments = map;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        for (String key : assignments.keySet()) {
            conversationController.conversation().addAssignment(key, assignments.get(key));
        }
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
}
