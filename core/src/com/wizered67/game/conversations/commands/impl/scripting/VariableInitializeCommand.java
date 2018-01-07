package com.wizered67.game.conversations.commands.impl.scripting;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.scripting.GameScript;
import com.wizered67.game.scripting.ScriptManager;

import java.util.List;

/**
 * ConversationCommand used to initialize a variable for any scripting language to
 * a specified initial value if the variable has not been defined by the time
 * the command executes. If it is defined, nothing happens.
 * @author Adam Victor
 */
public class VariableInitializeCommand implements ConversationCommand {
    /** Array of scripts to be executed that initializes the variables to the specified value. */
    private GameScript[] scripts;
    /** ScriptManager used for specified language. Used to check if variable has been initialized. */
    private ScriptManager scriptManager;
    /** Lust of names of the variables that will be initialized if undefined. */
    private List<String> variables;
    /** List of values for each corresponding variable to be assigned to. */
    private List<String> values;
    /** Language used for all scripts. */
    private String language;

    /** No arguments constructor. */
    public VariableInitializeCommand() {
        language = "";
        variables = null;
        values = null;
    }
    /** Creates a GameScript of language LANG that initializes variable VAR
     * to VALS if it is undefined. */
    public VariableInitializeCommand(List<String> vars, List<String> vals, String lang) {
        language = lang;
        variables = vars;
        values = vals;
        createScripts();
    }

    /** Create the GameScripts to be executed. */
    public void createScripts() {
        scriptManager = ConversationController.scriptManager(language);
        scripts = new GameScript[variables.size()];
        for (int i = 0; i < variables.size(); i += 1) {
            scripts[i] = scriptManager.createSetScript(variables.get(i), values.get(i));
        }
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        for (int i = 0; i < variables.size(); i += 1) {
            if (!scriptManager.isDefined(variables.get(i))) {
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
}
