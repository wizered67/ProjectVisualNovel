package com.wizered67.game.Scripting;

/**
 * Interface of all ScriptManagers. Has methods for loading scripts to be executed later.
 * @author Adam Victor
 */
public interface ScriptManager {
    /** Loads and returns the GameScript SCRIPT. If ISFILE it loads it from the filed named SCRIPT. */
    GameScript load(String script, boolean isFile);
    /** Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    boolean objectToBoolean(Object o);
    /** Returns the string value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    String objectToString(Object o);
    /** Returns the value of variable VAR in a language specific object type. */
    Object getValue(String var);
}
