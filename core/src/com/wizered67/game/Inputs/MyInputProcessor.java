package com.wizered67.game.Inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import java.util.*;



public class MyInputProcessor implements InputProcessor {
	

    private Set<Controllable> registeredControllableObjects = new HashSet<Controllable>();
    //todo map to list of ControlType to allow one key to do multiple things?
    private HashMap<Integer, ControlType> keyToControl = new HashMap<>();

    public MyInputProcessor(){
    	for (ControlType control : ControlType.values()) {
    		int[] keys = control.keys;
    		for (int key : keys) {
    			keyToControl.put(key, control);
			}
		}
    }

    public void register(Controllable c) {
        if (!registeredControllableObjects.contains(c)) {
            registeredControllableObjects.add(c);
        }
    }
    
    public void update(){

    }

    private void fireKey(int key, boolean pressed) {
		for (Controllable controllable : registeredControllableObjects) {
			controllable.keyEvent(keyToControl.get(key), key, pressed);
		}
	}

	private void fireTouch(int screenX, int screenY, int pointer, int button, boolean pressed) {
    	for (Controllable controllable : registeredControllableObjects) {
    		controllable.touchEvent(screenX, screenY, pointer, button, pressed);
		}
	}

    @Override
	public boolean keyDown(int keycode) {
		fireKey(keycode, true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
    	fireKey(keycode, false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	fireTouch(screenX, screenY, pointer, button, true);
        return true;
	}

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

	public enum ControlType {
		UP(new int[] {Keys.W, Keys.UP}),
		DOWN(new int[] {Keys.S, Keys.DOWN}),
		LEFT(new int[] {Keys.A, Keys.LEFT}),
		RIGHT(new int[] {Keys.D, Keys.RIGHT}),
		CONFIRM(new int[] {Keys.C, Keys.SPACE});
    	int[] keys;
    	ControlType(int[] k) {
    		keys = k;
		}
	}

}
