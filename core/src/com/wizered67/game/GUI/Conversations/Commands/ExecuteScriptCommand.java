package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.Scripting.GameScript;
import com.wizered67.game.Scripting.ScriptManager;

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
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }

    /** Static method to create a new command from XML Element ELEMENT. */
    public static ExecuteScriptCommand makeCommand(XmlReader.Element element) {
        XmlReader.Element textElement = element.getChild(0);
        String script = textElement != null ? textElement.getText() : "";
        boolean isFile = element.getBoolean("isfile", false);
        String language = element.getAttribute("language");
        return new ExecuteScriptCommand(script, isFile, language);
    }
}
