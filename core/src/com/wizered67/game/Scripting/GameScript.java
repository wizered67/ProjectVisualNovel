package com.wizered67.game.Scripting;

/**
 * Interface for all GameScripts. Scripts of a specific language should
 * implement this interface.
 * @author Adam Victor
 */
public abstract class GameScript {
    /** Language used for this script. Used to save and reload. */
    public String language;
    /** Script or filename of script. Used to save and reload. */
    public String script;
    /** Whether the script is a filename. */
    public boolean isFile;
    /** Executes the contents of this script and returns the result. */
    public abstract Object execute();
}
