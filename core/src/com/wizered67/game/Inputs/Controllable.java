package com.wizered67.game.Inputs;

/**
 * Interface for all objects that should receive events when a touch or key event occur.
 * @author Adam Victor
 */
public interface Controllable {
    /** A touch event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON. PRESSED is whether it was pressed (true) or released (false). */
    void touchEvent(int screenX, int screenY, int pointer, int button, boolean pressed);
    /** A key event involving key KEY mapped to ControlType CONTROL.
     * PRESSED is whether it was pressed (true) or released (false). */
    void keyEvent(MyInputProcessor.ControlType control, int key, boolean pressed);
}
