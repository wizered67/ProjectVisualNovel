package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.scene.CharacterVisibleCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a CharacterVisibleCommand from an XML element.
 *
 * @author Adam Victor
 */
public class CharacterVisibleCommandFactory implements ConversationCommandFactory<CharacterVisibleCommand> {
    private final static CharacterVisibleCommandFactory INSTANCE = new CharacterVisibleCommandFactory();

    public static CharacterVisibleCommandFactory getInstance() {
        return INSTANCE;
    }

    private CharacterVisibleCommandFactory() {
    }

    @Override
    public CharacterVisibleCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String name = element.getAttribute("id");
        boolean visible = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fadeTime", 0f);
        String fadeType = element.getAttribute("fadeType", "linear");
        boolean wait = element.getBooleanAttribute("wait", true);
        return new CharacterVisibleCommand(name, visible, fade, fadeType, wait);
    }
}