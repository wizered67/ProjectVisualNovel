package com.wizered67.game.conversations.scene;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

/**
 * Created by Adam on 5/3/2017.
 */
public class ScreenShakeEffect {
    private float magnitude;
    private FloatInterpolation damperInterpolation;

    private final transient Vector3 resultVector = new Vector3();

    public ScreenShakeEffect() {

    }

    public ScreenShakeEffect(String type, float magnitude, float length) {
        damperInterpolation = new FloatInterpolation(type, 1, 0, length);
        this.magnitude = magnitude;
    }

    public Vector3 update(float deltaTime) {
        float damper = damperInterpolation.update(deltaTime);
        float rx = MathUtils.random(-1f, 1f);
        float ry = MathUtils.random(-1f, 1f);
        resultVector.set(rx * magnitude * damper, ry * magnitude * damper, 0);
        return resultVector;
    }

    public boolean isDone() {
        return damperInterpolation.isDone();
    }

}
