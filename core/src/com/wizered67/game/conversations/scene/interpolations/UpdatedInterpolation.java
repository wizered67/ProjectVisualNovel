package com.wizered67.game.conversations.scene.interpolations;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store progress, type, and other information about an interpolation.
 * Classes wishing to utilize an interpolation can just have a fade field, initialize it,
 * and then update each frame. Has specific subtypes for different objects to interpolate.
 * @author Adam Victor
 */
public abstract class UpdatedInterpolation<T> {
    /** The total time this interpolation has been updated. */
    protected float progress;
    /** The type of interpolation to use. */
    protected String interpolationType;
    /** The time to spend doing this interpolation in total. */
    protected float totalTime;
    /** The initial value to start with. */
    protected T initialValue;
    /** The value to end with. */
    protected T endValue;
    /** Mapping between Strings with interpolation names and the Interpolation class used for it. */
    private transient static Map<String, Interpolation> interpolationTypes;

    public UpdatedInterpolation() {}

    public UpdatedInterpolation(String type, T start, T end, float length) {
        initialValue = start;
        endValue = end;
        interpolationType = type;
        totalTime = length;
    }
    /** Update progress and apply the interpolation, returning the result. */
    protected float updateInterpolation(float delta) {
        progress = MathUtils.clamp(progress + delta / totalTime, 0, 1);
        Interpolation interpolation = interpolationTypes.get(interpolationType);
        return interpolation.apply(progress);
    }
    /** Updates the interpolation and returns the interpolated version. */
    public abstract T update(float delta);

    /** Whether this interpolation has been completed. */
    public boolean isDone() {
        return progress >= 1;
    }

    static {
        interpolationTypes = new HashMap<>();
        interpolationTypes.put("linear", Interpolation.linear);
        interpolationTypes.put("smooth", Interpolation.smooth);
        interpolationTypes.put("smooth2", Interpolation.smooth2);
        interpolationTypes.put("smoother", Interpolation.smoother);
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
