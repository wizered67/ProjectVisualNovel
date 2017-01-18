package com.wizered67.game.Scripting;

import java.util.Map;

/**
 * Interface of all ScriptManagers. Has methods for loading scripts to be executed later.
 * @author Adam Victor
 */
public interface ScriptManager {
    /** Returns the name of the language used for this ScriptManager. */
    String name();
    /** Loads and returns the GameScript SCRIPT. If ISFILE it loads it from the filed named SCRIPT. */
    GameScript load(String script, boolean isFile);
    /** Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    boolean objectToBoolean(Object o);
    /** Returns the string value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    String objectToString(Object o);
    /** Returns the integer value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    int objectToInteger(Object o);
    /** Returns the value of variable VAR in a language specific object type. */
    Object getValue(String var);
    /** Returns whether a variable named VAR has been defined. */
    boolean isDefined(String var);
    /** Returns a GameScript that, when executed, assigns variable named VAR to VALUE. */
    GameScript createSetScript(String var, String value);
    /** Returns a map of variable names to variable values for non default variables. Used for saving. */
    Map<String, Object> saveMap();
    /** Reloads variables from the save map between variable name and value MAP. */
    void reload(Map<String, Object> map);
}
