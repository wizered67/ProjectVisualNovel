package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

/**
 * ConversationCommand that changes the current conversation to the specified one,
 * ending execution of the current conversation.
 * @author Adam Victor
 */
public class ChangeConversationCommand implements ConversationCommand {
    /** The identifier of the conversation to switch to (including .conv) */
    private String newConversation;
    /** The branch to switch to in the new conversation, or 'default' if not specified. */
    private String newBranch;

    public ChangeConversationCommand() {

    }

    public ChangeConversationCommand(String conv, String branch) {
        newConversation = conv;
        newBranch = branch;
    }

    @Override
    public void execute(ConversationController conversationController) {
        try {
            conversationController.loadConversation(newConversation);
            conversationController.setBranch(newBranch);
        } catch (ConversationParsingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean waitToProceed() {
        return false;
    }

    @Override
    public void complete(CompleteEvent c) {

    }
}
