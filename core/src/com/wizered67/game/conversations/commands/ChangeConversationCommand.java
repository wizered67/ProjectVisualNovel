package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;

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
        conversationController.loadConversation(newConversation);
        conversationController.setBranch(newBranch);
    }

    @Override
    public boolean waitToProceed() {
        return false;
    }

    @Override
    public void complete(CompleteEvent c) {

    }

    public static ChangeConversationCommand makeCommand(XmlReader.Element element) {
        String newConv = element.getAttribute("conv", null);
        String newBranch = element.getAttribute("branch", "default");
        if (newConv == null) {
            throw new GdxRuntimeException("No new conversation specified for new conversation command.");
        }
        return new ChangeConversationCommand(newConv, newBranch);
    }

    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
