package com.wizered67.game.inputs;

/**
 * Interface for all objects that should receive events when a touch or key event occur.
 * @author Adam Victor
 */
public interface Controllable {
    /** A touch down event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON. */
    boolean touchDown(int screenX, int screenY, int pointer, int button);

    /** A touch up event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON. */
    boolean touchUp(int screenX, int screenY, int pointer, int button);

    /** A key down involving key KEY mapped to ControlType CONTROL. */
    boolean keyDown(Controls.ControlType control, int key);

    /** A key up involving key KEY mapped to ControlType CONTROL. */
    boolean keyUp(Controls.ControlType control, int key);
}
