package com.wizered67.game.conversations.commands;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;

/**
 * Interface for all ConversationCommands which are executed in order
 * in ConversationController.
 * @author Adam Victor
 */
public interface ConversationCommand {
    /** Executes the command on the CONVERSATION CONTROLLER. */
    void execute(ConversationController conversationController);
    /** Whether to wait before proceeding to the next command in the branch. */
    boolean waitToProceed();
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    void complete(CompleteEvent c);
}
