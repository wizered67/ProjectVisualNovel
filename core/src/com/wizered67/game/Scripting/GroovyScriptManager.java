package com.wizered67.game.Scripting;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.wizered67.game.GameManager;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.IOException;
import java.util.Map;

/**
 * A ScriptManager that manages and loads Groovy scripts.
 * @author Adam Victor
 */
public class GroovyScriptManager implements ScriptManager {
    private GroovyShell groovyShell;
    private Binding binding;
    static final String SCRIPT_DIRECTORY = "Scripts/";
    /** Initializes GroovyShell and Binding. */
    public GroovyScriptManager() {
        binding = new Binding();
        groovyShell = new GroovyShell(binding);
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
     * Returns the boolean value of Object O, where O is assumed to be some
     * type specific to the scripting language.
     */
    @Override
    public boolean objectToBoolean(Object o) {
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
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
     * Returns the value of variable VAR in a language specific object type.
     */
    @Override
    public Object getValue(String var) {
        return binding.getProperty(var);
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
}
