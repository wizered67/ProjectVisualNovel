package com.wizered67.game.scripting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.wizered67.game.GameManager;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A ScriptManager that manages and loads Groovy scripts.
 * @author Adam Victor
 */
public  class GroovyScriptManager implements ScriptManager {
    private GroovyShell groovyShell;
    private Binding binding;
    static final String SCRIPT_DIRECTORY = "Scripts/";
    private HashMap<String, GameScript> savedScripts;
    /** Initializes GroovyShell and Binding. */
    public GroovyScriptManager() {
        binding = new Binding();
        groovyShell = new GroovyShell(binding);
        savedScripts = new HashMap<>();
    }
    /** Returns the name of the language used for this ScriptManager. */
    @Override
    public String name() {
        return "Groovy";
    }

    /**
     * Loads and returns the GameScript SCRIPT. If ISFILE it loads it from the filed named SCRIPT.
     */
    @Override
    public GameScript load(String script, boolean isFile) {
        Script groovyScript = null;
        if (!isFile) {
            groovyScript = groovyShell.parse(script);
        } else {
            FileHandle fileHandle = Gdx.files.internal(SCRIPT_DIRECTORY + script);
            try {
                groovyScript = groovyShell.parse(fileHandle.file());
            } catch (IOException e) {
                Gdx.app.error("Script", e.getMessage());
            }
        }
        GameScript gs = new GroovyScript(groovyScript, this);
        gs.language = "Groovy";
        gs.isFile = isFile;
        gs.script = script;
        return gs;
    }

    /**
     * Whether 'return' is required when getting the value of an expression in this language.
     */
    @Override
    public boolean requiresReturn() {
        return false;
    }

    /**
     * Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public boolean objectToBoolean(Object o) {
        if (o instanceof Boolean) {
            return (Boolean) o;
        } else {
            return false;
        }
    }

    /**
     * Returns the string value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public String objectToString(Object o) {
        return o.toString();
    }

    /**
     * Returns the integer value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public int objectToInteger(Object o) {
        if (o instanceof Integer) {
            return (Integer) o;
        } else {
            GameManager.error("Tried to convert non integer object to integer.");
            return 0;
        }
    }

    /**
     * Returns the double value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public double objectToDouble(Object o) {
        if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Float) {
            return (double) ((Float) o);
        } else {
            GameManager.error("Tried to convert non double object to double.");
            return 0;
        }
    }

    /**
     * Returns the value of VAR as type TYPE.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String var, Class<T> type) {
        Object languageValue = getLanguageValue(var);
        if (type == Double.class) {
            return (T) Double.valueOf(languageValue.toString());
        } else if (type == Float.class) {
            return (T) Float.valueOf(languageValue.toString());
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(languageValue.toString());
        } else if (type == String.class) {
            return (T) languageValue.toString();
        } else if (type == Boolean.class) {
            return (T) Boolean.valueOf(languageValue.toString());
        } else {
            return (T) languageValue;
        }
    }

    /**
     * Returns the value of variable VAR in a language specific object type.
     */
    @Override
    public Object getLanguageValue(String var) {
        return binding.getProperty(var);
    }

    /**
     * Returns the value of executing expression EXPR in a language specific object type.
     * Caches the script created.
     */
    @Override
    public Object getExpressionValue(String expr) {
        return getExpressionValue(expr, true);
    }

    /**
     * Returns the value of executing expression EXPR in a language specific object type.
     * If cache, stores the script result for later use.
     */
    @Override
    public Object getExpressionValue(String expr, boolean cache) {
        if (savedScripts.containsKey(expr)) {
            return savedScripts.get(expr).execute();
        }
        com.wizered67.game.scripting.GameScript script = load(expr, false);
        if (cache) {
            savedScripts.put(expr, script);
        }
        return script.execute();
    }

    /**
     * Returns the value of variable VAR as a Java integer.
     */
    @Override
    public int getIntegerValue(String var) {
        return objectToInteger(getLanguageValue(var));
    }

    /**
     * Returns the value of variable VAR as a Java double.
     */
    @Override
    public double getDoubleValue(String var) {
        return objectToDouble(getLanguageValue(var));
    }

    /**
     * Returns the value of variable VAR as a Java String.
     */
    @Override
    public String getStringValue(String var) {
        return objectToString(getLanguageValue(var));
    }

    /**
     * Returns the value of variable VAR as a Java boolean.
     */
    @Override
    public boolean getBooleanValue(String var) {
        return objectToBoolean(getLanguageValue(var));
    }

    /**
     * Returns whether a variable named VAR has been defined.
     */
    @Override
    public boolean isDefined(String var) {
        return binding.hasVariable(var);
    }

    /**
     * Returns a GameScript that, when executed, assigns variable named VAR to VALUE.
     */
    @Override
    public GameScript createSetScript(String var, String value) {
        return load(var + " = " + value, false);
    }

    /**
     * Returns a map of variable names to variable values for non default variables. Used for saving.
     */
    @Override
    public Map<String, Object> saveMap() {
        return binding.getVariables();
    }

    /**
     * Reloads variables from the save map between variable name and value MAP.
     */
    @Override
    public void reload(Map<String, Object> map) {
        for (String key : map.keySet()) {
            binding.setProperty(key, map.get(key));
        }
    }
    /** Assign variable of name NAME to be equal to VALUE. */
    @Override
    public void setValue(String name, Object value) {
        binding.setVariable(name, value);
    }
}
