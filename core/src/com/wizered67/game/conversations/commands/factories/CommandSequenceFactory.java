package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.base.CommandSequence;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

/**
 * Factory for creating a CommandSequence from an XML element.
 * @author Adam Victor
 */
public class CommandSequenceFactory implements ConversationCommandFactory<CommandSequence> {
    private final static CommandSequenceFactory INSTANCE = new CommandSequenceFactory();

    public static CommandSequenceFactory getInstance() {
        return INSTANCE;
    }

    private CommandSequenceFactory() {
    }

    @Override
    public CommandSequence makeCommand(ConversationLoader loader, XmlReader.Element element) {
        CommandSequence cs = new CommandSequence();
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element c = element.getChild(i);
            try {
                ConversationCommand command = loader.getCommand(c);
                cs.addCommand(command);
            } catch (ConversationParsingException e) {
                //if there's a parsing error, print it out and don't add this command to the sequence.
                e.printStackTrace();
            }
        }
        return cs;
    }
}