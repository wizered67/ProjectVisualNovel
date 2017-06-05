package com.wizered67.game.inputs;

import com.badlogic.gdx.Input;

import java.util.HashMap;

/**
 * Created by Adam on 5/18/2017.
 */
public class Controls {
    //todo map to list of ControlType to allow one key to do multiple things?
    /** A map between a key and the ControlType it is. */
    private HashMap<Integer, ControlType> keyToControl = new HashMap<>();

    public Controls() {
        for (ControlType control : ControlType.values()) {
            int[] keys = control.keys;
            for (int key : keys) {
                keyToControl.put(key, control);
            }
        }
    }

    public ControlType getControlType(int keycode) {
        if (keyToControl.containsKey(keycode)) {
            return keyToControl.get(keycode);
        } else {
            return ControlType.OTHER;
        }
    }

    /** Represents different types of controls so that several remappable buttons can do the same thing.
     * Eg the UP control is both the W key and Up key. Each button has an array of keys passed in which are
     * the default keys mapped to that control.
     */
    public enum ControlType {
        UP(Input.Keys.W, Input.Keys.UP),
        DOWN(Input.Keys.S, Input.Keys.DOWN),
        LEFT(Input.Keys.A, Input.Keys.LEFT),
        RIGHT(Input.Keys.D, Input.Keys.RIGHT),
        CONFIRM(Input.Keys.C, Input.Keys.SPACE),
        TRANSCRIPT(Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT),
        DEBUG(Input.Keys.ENTER),
        OTHER();
        int[] keys;
        /*
        ControlType(int[] k) {
            keys = k;
        }
        */
        ControlType(int... k) {keys = k; }
    }

}
