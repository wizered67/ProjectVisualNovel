package com.wizered67.game.conversations.scene.interpolations;

/**
 * UpdatedInterpolation that interpolates float values.
 * @author Adam Victor
 */
public class FloatInterpolation extends UpdatedInterpolation<Float> {
    public FloatInterpolation() {}
    public FloatInterpolation(String type, float start, float end, float length) {
        super(type, start, end, length);
    }
    public Float update(float delta) {
        float alpha = super.updateInterpolation(delta);
        return initialValue + (endValue - initialValue) * alpha;
    }
}
