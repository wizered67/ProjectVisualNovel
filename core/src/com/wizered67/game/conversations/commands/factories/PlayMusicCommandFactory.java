package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.audio.PlayMusicCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a PlayMusicCommand from an XML element.
 *
 * @author Adam Victor
 */
public class PlayMusicCommandFactory implements ConversationCommandFactory<PlayMusicCommand> {
    private final static PlayMusicCommandFactory INSTANCE = new PlayMusicCommandFactory();

    public static PlayMusicCommandFactory getInstance() {
        return INSTANCE;
    }

    private PlayMusicCommandFactory() {
    }

    @Override
    public PlayMusicCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        int index = element.getIntAttribute("index", 0);
        if (element.getName().equals("pausemusic")) {
            return new PlayMusicCommand(PlayMusicCommand.Type.PAUSE, index);
        }
        if (element.getName().equals("resumemusic")) {
            return new PlayMusicCommand(PlayMusicCommand.Type.RESUME, index);
        }
        String id = element.getAttribute("id");
        boolean loop = element.getBooleanAttribute("loop", false);
        float volume = element.getFloatAttribute("volume", 1);
        return new PlayMusicCommand(id, loop, volume, index);
    }
}