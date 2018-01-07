package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Interface for all ConversationCommandFactory classes.
 * Each class must have a makeCommand method that takes an XML Element and returns an instance of
 * the correct ConversationCommand class.
 * @author Adam Victor
 */
public interface ConversationCommandFactory<T extends ConversationCommand> {
    T makeCommand(ConversationLoader loader, XmlReader.Element element);
}
