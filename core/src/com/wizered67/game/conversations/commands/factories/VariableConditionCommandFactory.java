package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scripting.VariableConditionCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating a VariableConditionCommand from an XML element.
 *
 * @author Adam Victor
 */
public class VariableConditionCommandFactory implements ConversationCommandFactory<VariableConditionCommand> {
    private final static VariableConditionCommandFactory INSTANCE = new VariableConditionCommandFactory();

    public static VariableConditionCommandFactory getInstance() {
        return INSTANCE;
    }

    private VariableConditionCommandFactory() {
    }

    @Override
    public VariableConditionCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String language = element.getAttribute("language", ConversationController.defaultScriptingLanguage());
        boolean isFile = element.getBoolean("isfile", false);
        List<ConversationCommand> commands = new ArrayList<ConversationCommand>();
        List<ConversationCommand> elseCommands = new ArrayList<>();
        //String text = element.getChild(0).getText().trim();
        //Matcher matcher = scriptPattern.matcher(text);
        //String script = "";
        String script = element.getAttribute("cond");
        /*
        if (matcher.matches()) {
            script = matcher.group(1);
            String messages = matcher.group(2);
            if (!messages.trim().isEmpty()) {
                commands.add(MessageCommand.makeCommand(messages));
            }
        }
        */
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element c = element.getChild(i);
            if (i == element.getChildCount() - 1 && c.getName().equals("else")) {
                for (int j = 0; j < c.getChildCount(); j += 1) {
                    XmlReader.Element elseC = c.getChild(j);
                    try {
                        elseCommands.add(loader.getCommand(elseC));
                    } catch (ConversationParsingException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    ConversationCommand command = loader.getCommand(c);
                    commands.add(command);
                } catch (ConversationParsingException e) {
                    e.printStackTrace();
                }
            }
        }
        return new VariableConditionCommand(script, isFile, language, commands, elseCommands);
    }
}