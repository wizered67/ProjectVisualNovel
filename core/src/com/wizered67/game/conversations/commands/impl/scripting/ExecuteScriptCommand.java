package com.wizered67.game.conversations.commands.impl.scripting;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.scripting.GameScript;

/**
 * ConversationCommand for executing lines of code or a file containing lines of code
 * in a scripting language.
 * @author Adam Victor
 */
public class ExecuteScriptCommand implements ConversationCommand {
    /** GameScript object representing the code to be executed. */
    private GameScript gameScript;
    /** Script to be executed - either the contents or name of the file. */
    private String script;
    /** Whether the script is a file. */
    private boolean isFile;
    /** The scripting language the script is in. */
    private String language;

    /** No arguments constructor. */
    public ExecuteScriptCommand() {
        gameScript = null;
        script = "";
        isFile = false;
        language = "";
    }
    /** Loads gameScript by calling the load method of the ScriptManager for
     * scripting language LANG. If FILE, it loads the script contained in
     * the file named SCRIPT. Otherwise it loads the lines of code in SCRIPT.
     */
    public ExecuteScriptCommand(String script, boolean file, String lang) {
        this.script = script;
        isFile = file;
        language = lang;
        loadScript();
    }
    /** Load the script to be executed. */
    public void loadScript() {
        gameScript = ConversationController.scriptManager(language).load(script, isFile);
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        gameScript.execute();
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
}
