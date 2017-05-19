package com.wizered67.game.inputs;

import com.badlogic.gdx.InputAdapter;
import com.wizered67.game.GameManager;

/**
 * Sends input events to a Controllable object to be processed. Sends control types with keyDown and keyUp.
 * @author Adam Victor
 */
public class ControlInputAdapter extends InputAdapter {
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
    public boolean keyDown(int keycode) {
        return controllable.keyDown(GameManager.getControls().getControlType(keycode), keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return controllable.keyUp(GameManager.getControls().getControlType(keycode), keycode);
    }
}
