package com.wizered67.game.conversations.scene.interpolations;

import com.badlogic.gdx.math.Vector2;

/**
 * Interpolates a position vector with 2 dimensions.
 * @author Adam Victor
 */
public class PositionInterpolation extends UpdatedInterpolation<Vector2> {
    private final Vector2 result = new Vector2();

    public PositionInterpolation() {}
    public PositionInterpolation(String type, Vector2 start, Vector2 end, float length) {
        super(type, start, end, length);
    }

    /** Updates the interpolation and returns the interpolated version. */
    @Override
    public Vector2 update(float delta) {
        float alpha = super.updateInterpolation(delta);
        float newX = initialValue.x + (endValue.x - initialValue.x) * alpha;
        float newY = initialValue.y + (endValue.y - initialValue.y) * alpha;
        result.set(newX, newY);
        return result;
    }
}
