package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.impl.base.DelayCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a DelayCommand from an XML element.
 *
 * @author Adam Victor
 */
public class DelayCommandFactory implements ConversationCommandFactory<DelayCommand> {
    private final static DelayCommandFactory INSTANCE = new DelayCommandFactory();

    public static DelayCommandFactory getInstance() {
        return INSTANCE;
    }

    private DelayCommandFactory() {
    }

    @Override
    public DelayCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        float time = element.getFloatAttribute("time", 0f);
        boolean canSkip = element.getBooleanAttribute("skippable", false);
        return new DelayCommand(time, canSkip);
    }
}