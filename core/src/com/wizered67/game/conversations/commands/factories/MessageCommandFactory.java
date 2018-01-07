package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.base.MessageCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a MessageCommand from an XML element.
 *
 * @author Adam Victor
 */
public class MessageCommandFactory implements ConversationCommandFactory<MessageCommand> {
    private final static MessageCommandFactory INSTANCE = new MessageCommandFactory();

    public static MessageCommandFactory getInstance() {
        return INSTANCE;
    }

    private MessageCommandFactory() {
    }

    @Override
    public MessageCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        return MessageCommand.makeCommand(element.getText());
    }
}