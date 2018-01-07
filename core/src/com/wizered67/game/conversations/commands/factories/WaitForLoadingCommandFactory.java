package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.loading.WaitForLoadingCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a WaitForLoadingCommand from an XML element.
 *
 * @author Adam Victor
 */
public class WaitForLoadingCommandFactory implements ConversationCommandFactory<WaitForLoadingCommand> {
    private final static WaitForLoadingCommandFactory INSTANCE = new WaitForLoadingCommandFactory();

    public static WaitForLoadingCommandFactory getInstance() {
        return INSTANCE;
    }

    private WaitForLoadingCommandFactory() {
    }

    @Override
    public WaitForLoadingCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        return new WaitForLoadingCommand();
    }
}