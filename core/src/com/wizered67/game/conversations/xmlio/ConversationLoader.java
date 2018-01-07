package com.wizered67.game.conversations.xmlio;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.factories.ConversationCommandFactory;

/**
 * Created by Adam on 1/6/2018.
 */
public interface ConversationLoader {
    /** Returns the Conversation created by parsing the XML file
     * with the name NAME. */
    Conversation loadConversation(String name) throws ConversationParsingException;
    /** Returns the Conversation created by parsing the XML file with FileHandle FILE. */
    Conversation loadConversation(FileHandle file) throws ConversationParsingException;
    /** Adds a new mapping between element name NAME
     * and a factory to create that command. */
    void setCommandTagMapping(String name, ConversationCommandFactory factory);

    ConversationCommand getCommand(XmlReader.Element root) throws ConversationParsingException;
}
