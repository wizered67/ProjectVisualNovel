package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.scene.CharacterNameCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a CharacterNameCommand from an XML element.
 * @author Adam Victor
 */
public class CharacterNameCommandFactory implements ConversationCommandFactory<CharacterNameCommand> {
    private final static CharacterNameCommandFactory INSTANCE = new CharacterNameCommandFactory();

    public static CharacterNameCommandFactory getInstance() {
        return INSTANCE;
    }

    private CharacterNameCommandFactory() {
    }

    @Override
    public CharacterNameCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String name = element.getAttribute("id");
        String newName = element.getAttribute("displayname");
        return new CharacterNameCommand(name, newName);
    }
}