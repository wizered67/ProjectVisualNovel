package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.impl.scripting.WaitUntilVariableCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a WaitUntilVariableCommand from an XML element.
 *
 * @author Adam Victor
 */
public class WaitUntilVariableCommandFactory implements ConversationCommandFactory<WaitUntilVariableCommand> {
    private final static WaitUntilVariableCommandFactory INSTANCE = new WaitUntilVariableCommandFactory();

    public static WaitUntilVariableCommandFactory getInstance() {
        return INSTANCE;
    }

    private WaitUntilVariableCommandFactory() {
    }

    @Override
    public WaitUntilVariableCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String language = element.getAttribute("language", ConversationController.defaultScriptingLanguage());
        boolean isFile = element.getBoolean("isfile", false);
        String text = element.getAttribute("cond");//element.getChild(0).getText().trim();
        return new WaitUntilVariableCommand(text, isFile, language);
    }
}