package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.base.DebugCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a DebugCommand from an XML element.
 * @author Adam Victor
 */
public class DebugCommandFactory implements ConversationCommandFactory<DebugCommand> {
    private final static DebugCommandFactory INSTANCE = new DebugCommandFactory();

    public static DebugCommandFactory getInstance() {
        return INSTANCE;
    }

    private DebugCommandFactory() {
    }

    @Override
    public DebugCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String text = element.getAttribute("message");
        return new DebugCommand(text);
    }
}