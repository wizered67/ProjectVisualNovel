package com.wizered67.game.Scripting;

import groovy.lang.Script;

/**
 * Stores a loaded Groovy script to be executed later. Implements the GameScript Interface.
 * @author Adam Victor
 */
public class GroovyScript extends GameScript {
    /** Groovy Script object containing the script to execute. */
    Script gameScript;
    /** The GroovyScriptManager used to load this script. */
    GroovyScriptManager scriptManager;

    /** Sets gameScript to the loaded script SCRIPT and sets the scriptManager to MANAGER. */
    public GroovyScript(Script script, GroovyScriptManager manager) {
        gameScript = script;
        scriptManager = manager;
    }
    /**
     * Executes the contents of this script and returns the result.
     */
    @Override
    public Object execute() {
        return gameScript.run();
    }
}
