package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;

/**
 * ConversationCommand that changes the current conversation to the specified one,
 * ending execution of the current conversation.
 * @author Adam Victor
 */
public class ChangeConversationCommand implements ConversationCommand {
    private String newConversation;
    public ChangeConversationCommand(String conv) {
        newConversation = conv;
    }

    @Override
    public void execute(ConversationController conversationController) {
        conversationController.loadConversation(newConversation);
        conversationController.setBranch("default");
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
        if (newConv == null) {
            throw new GdxRuntimeException("No new conversation specified for new conversation command.");
        }
        return new ChangeConversationCommand(newConv);
    }

    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
