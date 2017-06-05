package com.wizered67.game.inputs;

import com.badlogic.gdx.InputProcessor;
import com.wizered67.game.GameManager;

/**
 * Sends input events to a Controllable object to be processed. Sends control types with keyDown and keyUp.
 * @author Adam Victor
 */
public class ControlInputAdapter implements InputProcessor {
    private Controllable controllable;
    public ControlInputAdapter(Controllable c) {
        controllable = c;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        return controllable.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        return controllable.touchUp(x, y, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return controllable.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return controllable.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return controllable.scrolled(amount);
    }

    @Override
    public boolean keyDown(int keycode) {
        return controllable.keyDown(GameManager.getControls().getControlType(keycode), keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return controllable.keyUp(GameManager.getControls().getControlType(keycode), keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return controllable.keyTyped(character);
    }
}
