package com.wizered67.game.conversations.commands.impl.scripting;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.scripting.GameScript;
import com.wizered67.game.scripting.ScriptManager;

import java.util.List;

/**
 * ConversationCommand that repeats inner commands until a condition in a script evaluates to true.
 * @author Adam Victor
 */
public class VariableWhileCommand implements ConversationCommand {
    /** List of commands to be repeated until the condition is met. */
    private List<ConversationCommand> repeatCommands;
    /** A script that returns whether a condition has been met when run. */
    private GameScript condition;
    /** The ScriptManager used to convert script result to a boolean. */
    private ScriptManager scriptManager;

    public VariableWhileCommand() {}

    public VariableWhileCommand(List<ConversationCommand> commands, String conditionScript, boolean isFile, String language) {
        scriptManager = ConversationController.scriptManager(language);
        condition = scriptManager.loadConditionScript(conditionScript, isFile);
        repeatCommands = commands;
        repeatCommands.add(this);
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
            conversationController.insertCommands(repeatCommands);
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
