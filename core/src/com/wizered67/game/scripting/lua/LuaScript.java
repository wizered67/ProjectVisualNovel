package com.wizered67.game.scripting.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.wizered67.game.scripting.GameScript;
import org.luaj.vm2.LuaValue;

/**
 * Stores loaded scripts in Lua to be executed later. Implements GameScript interface.
 * @author Adam Victor
 */
public class LuaScript extends GameScript {
    private LuaValue loadedScript;
    /** Loads the LuaScript SCRIPT in MANAGER. If ISFILE, loads the script contained in the file named SCRIPT. */
    public LuaScript(com.wizered67.game.scripting.lua.LuaScriptManager manager, String script, boolean isFile) {
       if (isFile) {
           FileHandle file = Gdx.files.internal(com.wizered67.game.scripting.lua.LuaScriptManager.SCRIPT_DIRECTORY + script);
           loadedScript = manager.globals.load(file.reader(), "script");
       } else {
           loadedScript = manager.globals.load(script);
       }
    }
    /** Executes the contents of this script and returns the result. */
    @Override
    public Object execute() {
        return loadedScript.call();
    }

    //TODO: execute functions with arguments.
    /*
    @Override
    public void execute(String function)
    */
}
