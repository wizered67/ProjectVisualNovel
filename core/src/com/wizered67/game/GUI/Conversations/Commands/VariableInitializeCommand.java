package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.Scripting.GameScript;
import com.wizered67.game.Scripting.ScriptManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConversationCommand used to initialize a variable for any scripting language to
 * a specified initial value if the variable has not been defined by the time
 * the command executes. If it is defined, nothing happens.
 * @author Adam Victor
 */
public class VariableInitializeCommand implements ConversationCommand {
    static Pattern initPattern = Pattern.compile("(\\S+) (\\S+)");
    /** Array of scripts to be executed that initializes the variables to the specified value. */
    private GameScript[] scripts;
    /** ScriptManager used for specified language. Used to check if variable has been initialized. */
    private ScriptManager scriptManager;
    /** Names of the variables that will be initialized if undefined. */
    private String[] variables;
    /** Creates a GameScript of language LANGUAGE that initializes variable VAR
     * to VALUE if it is undefined. */
    public VariableInitializeCommand(List<String> vars, List<String> values, String language) {
        scriptManager = ConversationController.scriptManager(language);
        scripts = new GameScript[vars.size()];
        variables = new String[vars.size()];
        for (int i = 0; i < vars.size(); i += 1) {
            scripts[i] = scriptManager.createSetScript(vars.get(i), values.get(i));
            variables[i] = vars.get(i);
        }

    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        for (int i = 0; i < variables.length; i += 1) {
            if (!scriptManager.isDefined(variables[i])) {
                scripts[i].execute();
            }
        }
    }
    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {

    }
    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static VariableInitializeCommand makeCommand(XmlReader.Element element) {
        String language = element.getAttribute("language");
        String text = element.getText();
        Matcher matcher = initPattern.matcher(text);
        List<String> vars = new ArrayList<String>();
        List<String> values = new ArrayList<String>();
        while (matcher.find()) {
            String var = matcher.group(1);
            String value = matcher.group(2);
            vars.add(var);
            values.add(value);
        }
        return new VariableInitializeCommand(vars, values, language);
    }
}
