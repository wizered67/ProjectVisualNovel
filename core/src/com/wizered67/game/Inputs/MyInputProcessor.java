package com.wizered67.game.Inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import java.util.*;


/** InputProcessor that dispatches input events to any registered objects that implement Controllable.
 * @author Adam Victor
 */
public class MyInputProcessor implements InputProcessor {
    /** A Set of all Controllable objects that have been registered. Whenever an input event happens, it is sent to
     * these objects. */
    private Set<Controllable> registeredControllableObjects = new HashSet<Controllable>();
    //todo map to list of ControlType to allow one key to do multiple things?
    /** A map between a key and the ControlType it is. */
    private HashMap<Integer, ControlType> keyToControl = new HashMap<>();

    public MyInputProcessor(){
    	for (ControlType control : ControlType.values()) {
    		int[] keys = control.keys;
    		for (int key : keys) {
    			keyToControl.put(key, control);
			}
		}
    }
    //todo should registered objects be saved?
    /** Adds the Controllable object C to to set of registered controllable objects. */
    public void register(Controllable c) {
        if (!registeredControllableObjects.contains(c)) {
            registeredControllableObjects.add(c);
        }
    }
    //todo remove?
    public void update(){

    }
    /** Called when a key is pressed or unpressed. Sends the event to all registered objects.
     * KEY is the key and PRESSED is whether it was pressed (true) or released (false). */
    private void fireKey(int key, boolean pressed) {
		for (Controllable controllable : registeredControllableObjects) {
			ControlType control = keyToControl.get(key);
			if (control == null) {
				control = ControlType.OTHER;
			}
			controllable.keyEvent(control, key, pressed);
		}
	}

    /** Called when a mouse button is pressed or unpressed. Sends the event to all registered objects.
     * SCREENX, SCREENY are the coordinates of the touch, POINTER is the id of the pointer, BUTTON
     * is the mouse button, and PRESSED is whether it was pressed (true) or released (false).
     */
	private void fireTouch(int screenX, int screenY, int pointer, int button, boolean pressed) {
    	for (Controllable controllable : registeredControllableObjects) {
    		controllable.touchEvent(screenX, screenY, pointer, button, pressed);
		}
	}
    /** Overrides InputProcessor's keyDown to fire events when a key with KEYCODE is pressed. */
    @Override
	public boolean keyDown(int keycode) {
		fireKey(keycode, true);
		return true;
	}
    /** Overrides InputProcessor's keyUp to fire events when a key with KEYCODE is released. */
	@Override
	public boolean keyUp(int keycode) {
    	fireKey(keycode, false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
    /** Overrides InputProcessor's touchDown to fire events when a mouse button is pressed.
     * SCREENX, SCREENY is the position of the touch, POINTER is the id of the pointer, and
     * BUTTON is the mouse button. */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	fireTouch(screenX, screenY, pointer, button, true);
        return true;
	}
    /** Overrides InputProcessor's touchUp to fire events when a mouse button is released.
     * SCREENX, SCREENY is the position of the touch, POINTER is the id of the pointer, and
     * BUTTON is the mouse button. */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	fireTouch(screenX, screenY, pointer, button, false);
        return true;
	}
	

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

    /** Represents different types of controls so that several remappable buttons can do the same thing.
     * Eg the UP control is both the W key and Up key. Each button has an array of keys passed in which are
     * the default keys mapped to that control.
     */
	public enum ControlType {
		UP(new int[] {Keys.W, Keys.UP}),
		DOWN(new int[] {Keys.S, Keys.DOWN}),
		LEFT(new int[] {Keys.A, Keys.LEFT}),
		RIGHT(new int[] {Keys.D, Keys.RIGHT}),
		CONFIRM(new int[] {Keys.C, Keys.SPACE}),
		OTHER(new int[] {});
    	int[] keys;
    	ControlType(int[] k) {
    		keys = k;
		}
	}

}
