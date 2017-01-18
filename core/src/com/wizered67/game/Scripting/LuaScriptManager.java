package com.wizered67.game.Scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A ScriptManager that loads Lua scripts.
 * @author Adam Victor
 */
public class LuaScriptManager implements ScriptManager {
    static final String SCRIPT_DIRECTORY = "Scripts/";
    /** Globals used for loading and executing Lua scripts. */
    Globals globals;
    private Set<LuaValue> defaultKeys;
    /** Initializes globals to the Standard Globals. */
    public LuaScriptManager() {
        globals = JsePlatform.standardGlobals();
        defaultKeys = new HashSet<LuaValue>();
        for (LuaValue key : globals.keys()) {
            defaultKeys.add(key);
        }
    }
    /** Returns the name of the language used for this ScriptManager. */
    @Override
    public String name() {
        return "Lua";
    }

    @Override
    public GameScript load(String script, boolean isFile) {
        GameScript gs = new LuaScript(this, script, isFile);
        gs.isFile = isFile;
        gs.script = script;
        gs.language = "Lua";
        return gs;
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

    /**
     * Returns the integer value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public int objectToInteger(Object o) {
        LuaValue value = (LuaValue) o;
        return value.toint();
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
    @Override
    /** Returns a Map of variables names to values for variables that aren't predefined. */
    public Map<String, Object> saveMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (LuaValue key : globals.keys()) {
            if (!defaultKeys.contains(key)) {
                map.put(key.toString(), globals.get(key));
            }
        }
        return map;
    }

    /**
     * Reloads variables from the save map between variable name and value MAP.
     */
    @Override
    public void reload(Map<String, Object> map) {
        for (String key : map.keySet()) {
            globals.set(key, (LuaValue) map.get(key));
        }
    }

}
