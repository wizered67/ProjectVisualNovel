package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.audio.PlaySoundCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a PlaySoundCommand from an XML element.
 *
 * @author Adam Victor
 */
public class PlaySoundCommandFactory implements ConversationCommandFactory<PlaySoundCommand> {
    private final static PlaySoundCommandFactory INSTANCE = new PlaySoundCommandFactory();

    public static PlaySoundCommandFactory getInstance() {
        return INSTANCE;
    }

    private PlaySoundCommandFactory() {
    }

    @Override
    public PlaySoundCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String id = element.getAttribute("id");
        return new PlaySoundCommand(id);
    }
}