package com.wizered67.game.Scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * A ScriptManager that loads Lua scripts.
 * @author Adam Victor
 */
public class LuaScriptManager implements ScriptManager {
    static final String SCRIPT_DIRECTORY = "Scripts/";
    /** Globals used for loading and executing Lua scripts. */
    Globals globals;
    /** Initializes globals to the Standard Globals. */
    public LuaScriptManager() {
        globals = JsePlatform.standardGlobals();
    }

    @Override
    public GameScript load(String script, boolean isFile) {
        return new LuaScript(this, script, isFile);
    }
    /** Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    @Override
    public boolean objectToBoolean(Object o) {
        LuaValue value = (LuaValue) o;
        return value.toboolean();
    }
    /** Returns the string value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    @Override
    public String objectToString(Object o) {
        LuaValue value = (LuaValue) o;
        return value.toString();
    }
    /** Returns the value of variable VAR in a language specific object type. */
    @Override
    public LuaValue getValue(String var) {
        return globals.get(var);
    }
    /** Returns whether a variable named VAR has been defined. */
    @Override
    public boolean isDefined(String var) {
        return !getValue(var).isnil();
    }
    /** Returns a GameScript that, when executed, assigns variable named VAR to VALUE. */
    @Override
    public GameScript createSetScript(String var, String value) {
        return new LuaScript(this, var + " = " + value, false);
    }

    public void debugPrint() {
        for (LuaValue key : globals.keys()) {
            System.out.println("Key: " + key + ", Value: " + globals.get(key));
        }
    }
}
