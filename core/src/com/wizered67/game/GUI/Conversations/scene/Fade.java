package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store progress, type, and other information about an interpolation.
 * Classes wishing to utilize an interpolation can just have a fade field, initialize it,
 * and then update each frame.
 * @author Adam Victor
 */
public class Fade {
    private float progress;
    private String interpolationType;
    private float totalTime;
    /** Direction of the fade, positive for going from 0->1 and negative for 1->0.*/
    private int direction;
    private float initialValue;
    private float remaining;
    private transient static Map<String, Interpolation> interpolationTypes;

    public Fade(String type, float start, float length, int dir) {
        interpolationType = type;
        totalTime = length;
        direction = dir;
        initialValue = start;
        if (direction > 0) {
            remaining = 1 - initialValue;
        } else {
            remaining = initialValue;
        }
    }

    public float update(float delta) {
        progress += (delta / totalTime);
        Interpolation interpolation = interpolationTypes.get(interpolationType);
        return initialValue + remaining * (interpolation.apply(progress) * Math.signum(direction));
    }

    static {
        interpolationTypes = new HashMap<>();
        interpolationTypes.put("linear", Interpolation.linear);
        interpolationTypes.put("fade", Interpolation.fade);

        interpolationTypes.put("pow2", Interpolation.pow2);
        interpolationTypes.put("pow2In", Interpolation.pow2In);
        interpolationTypes.put("pow2Out", Interpolation.pow2Out);
        interpolationTypes.put("pow2InInverse", Interpolation.pow2InInverse);
        interpolationTypes.put("pow2OutInverse", Interpolation.pow2OutInverse);

        interpolationTypes.put("pow3", Interpolation.pow3);
        interpolationTypes.put("pow3In", Interpolation.pow3In);
        interpolationTypes.put("pow3Out", Interpolation.pow3Out);
        interpolationTypes.put("pow3InInverse", Interpolation.pow3InInverse);
        interpolationTypes.put("pow3OutInverse", Interpolation.pow3OutInverse);

        interpolationTypes.put("pow4", Interpolation.pow4);
        interpolationTypes.put("pow4In", Interpolation.pow4In);
        interpolationTypes.put("pow4Out", Interpolation.pow4Out);

        interpolationTypes.put("pow5", Interpolation.pow5);
        interpolationTypes.put("pow5In", Interpolation.pow5In);
        interpolationTypes.put("pow5Out", Interpolation.pow5Out);

        interpolationTypes.put("sine", Interpolation.sine);
        interpolationTypes.put("sineIn", Interpolation.sineIn);
        interpolationTypes.put("sineOut", Interpolation.sineOut);

        interpolationTypes.put("exp10", Interpolation.exp10);
        interpolationTypes.put("exp10In", Interpolation.exp10In);
        interpolationTypes.put("exp10Out", Interpolation.exp10Out);

        interpolationTypes.put("exp5", Interpolation.exp5);
        interpolationTypes.put("exp5In", Interpolation.exp5In);
        interpolationTypes.put("exp5Out", Interpolation.exp5Out);

        interpolationTypes.put("circle", Interpolation.circle);
        interpolationTypes.put("circleIn", Interpolation.circleIn);
        interpolationTypes.put("circleOut", Interpolation.circleOut);

        interpolationTypes.put("elastic", Interpolation.elastic);
        interpolationTypes.put("elasticIn", Interpolation.elasticIn);
        interpolationTypes.put("elasticOut", Interpolation.elasticOut);

        interpolationTypes.put("swing", Interpolation.swing);
        interpolationTypes.put("swingIn", Interpolation.swingIn);
        interpolationTypes.put("swingOut", Interpolation.swingOut);

        interpolationTypes.put("bounce", Interpolation.bounce);
        interpolationTypes.put("bounceIn", Interpolation.bounceIn);
        interpolationTypes.put("bounceOut", Interpolation.bounceOut);
    }
}
