package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.scripting.GameScript;
import com.wizered67.game.scripting.ScriptManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ConversationCommand that executes a sequence of conditionCommands when some condition is met.
 * The condition is a script that, when executed, returns a value that can be converted
 * by the ScriptManager to true or false.
 * @author Adam Victor
 */
public class VariableConditionCommand implements ConversationCommand {
    /** A script that returns whether a condition has been met when run. */
    private GameScript condition;
    /** The ScriptManager used to convert script result to a boolean. */
    private ScriptManager scriptManager;
    /** The commands to be executed if the condition is met. */
    private List<ConversationCommand> conditionCommands;
    /** The commands to be executed if the condition is not met. */
    private List<ConversationCommand> elseCommands;
    /** Pattern used to match scripts in brackets. */
    public transient static Pattern scriptPattern = Pattern.compile("\\{(.*?)\\}(.*)?", Pattern.DOTALL);
    /** No arguments constructor. Needed for serialization. */
    public VariableConditionCommand() {
        scriptManager = null;
        condition = null;
        conditionCommands = null;
        elseCommands = null;
    }
    /** Sets the scriptManager to the one used for LANG and then loads the CONDITION SCRIPT.
     * Iff FILE, it load the file named CONDITION SCRIPT. Also sets the commands to be
     * executed if true to RESULT, and sets the commands to be executed otherwise to ELSE RESULT.
     */
    public VariableConditionCommand(String conditionScript, boolean file, String lang,
                                    List<ConversationCommand> result, List<ConversationCommand> elseResult) {
        scriptManager = ConversationController.scriptManager(lang);
        if (!file && scriptManager.requiresReturn() && !conditionScript.matches(".*return .*")) {
            conditionScript = "return " + conditionScript;
        }
        condition = scriptManager.load(conditionScript, file);
        conditionCommands = result;
        elseCommands = elseResult;
    }
    /** Whether the condition in the script passed in has been met. */
    public boolean conditionMet() {
        return scriptManager.objectToBoolean(condition.execute());
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        if (conditionMet()) {
            conversationController.insertCommands(conditionCommands);
        } else if (!elseCommands.isEmpty()) {
            conversationController.insertCommands(elseCommands);
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
    public static VariableConditionCommand makeCommand(XmlReader.Element element) {
        String language = element.getAttribute("language");
        boolean isFile = element.getBoolean("isfile", false);
        List<ConversationCommand> commands = new ArrayList<ConversationCommand>();
        List<ConversationCommand> elseCommands = new ArrayList<>();
        String text = element.getChild(0).getText().trim();
        Matcher matcher = scriptPattern.matcher(text);
        String script = "";
        if (matcher.matches()) {
            script = matcher.group(1);
            String messages = matcher.group(2);
            if (!messages.trim().isEmpty()) {
                commands.add(MessageCommand.makeCommand(messages));
            }
        }
        for (int i = 1; i < element.getChildCount(); i += 1) {
            XmlReader.Element c = element.getChild(i);
            if (i == element.getChildCount() - 1 && c.getName().equals("else")) {
                for (int j = 0; j < c.getChildCount(); j += 1) {
                    XmlReader.Element elseC = c.getChild(j);
                    elseCommands.add(ConversationLoader.getCommand(elseC));
                }
            } else {
                ConversationCommand command = ConversationLoader.getCommand(c);
                commands.add(command);
            }
        }
        return new VariableConditionCommand(script, isFile, language, commands, elseCommands);
    }
}
