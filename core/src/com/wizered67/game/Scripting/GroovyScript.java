package com.wizered67.game.Scripting;

import groovy.lang.Script;

/**
 * Created by Adam on 12/22/2016.
 */
public class GroovyScript extends GameScript {
    Script gameScript;
    GroovyScriptManager scriptManager;
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
