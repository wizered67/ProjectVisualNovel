package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.scene.ClearSceneCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a ClearSceneCommand from an XML element.
 *
 * @author Adam Victor
 */
public class ClearSceneCommandFactory implements ConversationCommandFactory<ClearSceneCommand> {
    private final static ClearSceneCommandFactory INSTANCE = new ClearSceneCommandFactory();

    public static ClearSceneCommandFactory getInstance() {
        return INSTANCE;
    }

    private ClearSceneCommandFactory() {
    }

    @Override
    public ClearSceneCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        boolean clearImages = element.getBooleanAttribute("clearImages", false);
        boolean clearCharacters = element.getBooleanAttribute("clearCharacters", false);
        return new ClearSceneCommand(clearImages, clearCharacters);
    }
}