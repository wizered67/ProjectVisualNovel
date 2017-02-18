package com.wizered67.game.saving.serializers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Partial Serializer for LibGDX's Sprite class. Ignores some data, mostly texture related, so classes with Sprites
 * need to save texture name or other info to reload from.
 * @author Adam Victor
 */
public class SpriteSerializer extends Serializer<Sprite> {
    /**
     * Writes the bytes for the object to the output.
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} write methods that accept a
     * serialier.
     */
    @Override
    public void write(Kryo kryo, Output output, Sprite object) {
        kryo.writeObject(output, object.getColor());
        output.writeFloat(object.getX());
        output.writeFloat(object.getY());
        output.writeFloat(object.getScaleX());
        output.writeFloat(object.getScaleY());
        output.writeFloat(object.getRotation());
    }

    /**
     * Reads bytes and returns a new object of the specified concrete type.
     * Before Kryo can be used to read child objects, {@link Kryo#reference(Object)} must be called with the parent object to
     * ensure it can be referenced by the child objects. Any serializer that uses {@link Kryo} to read a child object may need to
     * be reentrant.
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} read methods that accept a
     * serializer.
     * @return May be null if {@link #getAcceptsNull()} is true.
     */
    @Override
    public Sprite read(Kryo kryo, Input input, Class<Sprite> type) {
        Sprite sprite = new Sprite();
        sprite.setColor(kryo.readObject(input, Color.class));
        sprite.setX(input.readFloat());
        sprite.setY(input.readFloat());
        sprite.setScale(input.readFloat(), input.readFloat());
        sprite.setRotation(input.readFloat());
        return sprite;
    }
}
