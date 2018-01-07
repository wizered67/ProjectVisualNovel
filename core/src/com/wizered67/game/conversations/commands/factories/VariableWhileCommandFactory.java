package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scripting.VariableWhileCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

import java.util.LinkedList;
import java.util.List;

/**
 * Factory for creating a VariableWhileCommand from an XML element.
 *
 * @author Adam Victor
 */
public class VariableWhileCommandFactory implements ConversationCommandFactory<VariableWhileCommand> {
    private final static VariableWhileCommandFactory INSTANCE = new VariableWhileCommandFactory();

    public static VariableWhileCommandFactory getInstance() {
        return INSTANCE;
    }

    private VariableWhileCommandFactory() {
    }

    @Override
    public VariableWhileCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String language = element.getAttribute("language", ConversationController.defaultScriptingLanguage());
        boolean isFile = element.getBoolean("isfile", false);
        String cond = element.getAttribute("cond");
        List<ConversationCommand> repeatList = new LinkedList<>();
        for (int c = 0; c < element.getChildCount(); c += 1) {
            try {
                repeatList.add(loader.getCommand(element.getChild(c)));
            } catch (ConversationParsingException e) {
                e.printStackTrace();
            }
        }
        return new VariableWhileCommand(repeatList, cond, isFile, language);
    }
}