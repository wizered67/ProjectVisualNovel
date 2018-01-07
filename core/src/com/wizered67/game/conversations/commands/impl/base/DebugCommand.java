package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

/**
 * A ConversationCommand for displaying messages for
 * @author Adam Victor
 */
public class DebugCommand implements ConversationCommand {
    /** The debug message to show. */
    private String message;
    /** No arguments constructor. */
    public DebugCommand() {
        message = "";
    }
    /** Creates a new DebugCommand that prints out M when executed. */
    public DebugCommand(String m) {
        message = m;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        System.out.println(message);
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
