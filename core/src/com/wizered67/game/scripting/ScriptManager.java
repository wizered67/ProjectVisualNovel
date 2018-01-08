package com.wizered67.game.scripting;

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

    /** Loads and returns the GameScript SCRIPT as a condition script.
     * If ISFILE it loads it from the filed named SCRIPT. The script may be modified
     * to make it work as a condition. */
    GameScript loadConditionScript(String script, boolean isFile);

    /** Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    boolean objectToBoolean(Object o);
    /** Returns the string value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    String objectToString(Object o);
    /** Returns the integer value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    int objectToInteger(Object o);
    /** Returns the double value of Object O, where O is assumed to be some
     * type specific to the scripting language. */
    double objectToDouble(Object o);

    /** Returns the value of VAR as type TYPE. */
    <T> T getValue(String var, Class<T> type);
    /** Returns the value of variable VAR in a language specific object type. */
    Object getLanguageValue(String var);

    /** Returns the value of executing expression EXPR in a language specific object type.
     * Caches the script created.
     */
    Object getExpressionValue(String expr);
    /** Returns the value of executing expression EXPR in a language specific object type.
     * If cache, stores the script result for later use. */
    Object getExpressionValue(String expr, boolean cache);

    /** Returns the value of variable VAR as a Java integer. */
    int getIntegerValue(String var);
    /** Returns the value of variable VAR as a Java double. */
    double getDoubleValue(String var);
    /** Returns the value of variable VAR as a Java String. */
    String getStringValue(String var);
    /** Returns the value of variable VAR as a Java boolean. */
    boolean getBooleanValue(String var);

    /** Returns whether a variable named VAR has been defined. */
    boolean isDefined(String var);
    /** Returns a GameScript that, when executed, assigns variable named VAR to VALUE. */
    GameScript createSetScript(String var, String value);
    /** Returns a map of variable names to variable values for non default variables. Used for saving. */
    Map<String, Object> saveMap();
    /** Reloads variables from the save map between variable name and value MAP. */
    void reload(Map<String, Object> map);
    /** Assign variable of name NAME to be equal to VALUE. */
    void setValue(String name, Object value);
}
