package com.wizered67.game.conversations.scene;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

/**
 * Effect that uses a FloatInterpolation to shake the screen.
 * @author Adam Victor
 */
public class ScreenShakeEffect {
    /** Magnitude of the shake, the maximum number of pixels in any direction the screen can shake. */
    private Vector2 magnitude;
    /** The interpolation to be used for dampening. Goes from 1 to 0 as the shake slows. */
    private FloatInterpolation damperInterpolation;
    /** Vector3 used to store the result. Final and reused to avoid garbage collection. */
    private final transient Vector3 resultVector = new Vector3();

    public ScreenShakeEffect() {

    }

    public ScreenShakeEffect(String type, Vector2 magnitude, float length) {
        damperInterpolation = new FloatInterpolation(type, 1, 0, length);
        this.magnitude = magnitude;
    }
    /** Update the screen shake by DELTATIME seconds. Based on the technique used at
     * http://unitytipsandtricks.blogspot.com/2013/05/camera-shake.html, with the interpolation
     * being used for damper. */
    public Vector3 update(float deltaTime) {
        float damper = damperInterpolation.update(deltaTime);
        float rx = MathUtils.random(-1f, 1f);
        float ry = MathUtils.random(-1f, 1f);
        resultVector.set(rx * magnitude.x * damper, ry * magnitude.y * damper, 0);
        return resultVector;
    }
    /** Returns whether the screenshake is done. */
    public boolean isDone() {
        return damperInterpolation.isDone();
    }

}
