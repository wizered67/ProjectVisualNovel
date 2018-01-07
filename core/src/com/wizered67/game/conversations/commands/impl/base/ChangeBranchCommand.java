package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

/**
 * A ConversationCommand that changes the current branch to a new one.
 * @author Adam Victor
 */
public class ChangeBranchCommand implements ConversationCommand {
    /** The name of the new branch to switch to. */
    private String newBranch;
    /** No arguments constructor. */
    public ChangeBranchCommand() {
        newBranch = "";
    }
    /** Creates a new ChangeBranchCommand with Conversation C that
     * changes the branch to BRANCH when executed. */
    public ChangeBranchCommand(String branch) {
        newBranch = branch;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        conversationController.setBranch(newBranch);
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
    /** A string describing this command. */
    public String toString() {
        return "Change branch to " + newBranch + ".";
    }
}
