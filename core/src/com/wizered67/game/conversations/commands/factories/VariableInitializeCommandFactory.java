package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.impl.scripting.VariableInitializeCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Factory for creating a VariableInitializeCommand from an XML element.
 *
 * @author Adam Victor
 */
public class VariableInitializeCommandFactory implements ConversationCommandFactory<VariableInitializeCommand> {
    private final static VariableInitializeCommandFactory INSTANCE = new VariableInitializeCommandFactory();
    private static final Pattern INIT_PATTERN = Pattern.compile("(\\S+) +(.+)");

    public static VariableInitializeCommandFactory getInstance() {
        return INSTANCE;
    }

    private VariableInitializeCommandFactory() {
    }

    @Override
    public VariableInitializeCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        String language = element.getAttribute("language", ConversationController.defaultScriptingLanguage());
        XmlReader.Element textElement = element.getChild(0);
        String text = textElement != null ? textElement.getText() : "";
        List<String> vars = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        /*
        while (matcher.find()) {
            String var = matcher.group(1);
            String value = matcher.group(2);
            vars.add(var);
            values.add(value);
        }
        */
        text = text.replaceAll("\r", "");
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            Matcher matcher = INIT_PATTERN.matcher(line);
            if (matcher.matches()) {
                vars.add(matcher.group(1));
                values.add(matcher.group(2));
            }
        }
        return new VariableInitializeCommand(vars, values, language);
    }
}