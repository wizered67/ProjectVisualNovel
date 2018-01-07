package com.wizered67.game.conversations.commands.impl.loading;

import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

/**
 * Conversation Command that waits until all queued resources have been loaded.
 * @author Adam Victor
 */
public class WaitForLoadingCommand implements ConversationCommand {

    public WaitForLoadingCommand() {

    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {

    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return GameManager.assetManager().getQueuedAssets() != 0;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {

    }
}
