package com.wizered67.game.Inputs;

/**
 * Created by Adam on 10/24/2016.
 */
public interface Controllable {
    void touchEvent(int screenX, int screenY, int pointer, int button, boolean pressed);
    void keyEvent(MyInputProcessor.ControlType control, int key, boolean pressed);
}
