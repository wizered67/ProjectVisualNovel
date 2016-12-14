package com.wizered67.game.Scripting;

/**
 * Interface for all GameScripts. Scripts of a specific language should
 * implement this interface.
 * @author Adam Victor
 */
public interface GameScript {
    /** Executes the contents of this script and returns the result. */
    Object execute();
}
