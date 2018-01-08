package com.wizered67.game.conversations.commands.impl.scripting;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.scripting.GameScript;
import com.wizered67.game.scripting.ScriptManager;

import java.util.List;
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
        condition = scriptManager.loadConditionScript(conditionScript, file);
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
}
