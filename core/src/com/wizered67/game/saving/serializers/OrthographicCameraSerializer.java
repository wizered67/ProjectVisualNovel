package com.wizered67.game.saving.serializers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Simple serializer to save zoom and position of OrthographicCameras.
 * @author Adam Victor
 */
public class OrthographicCameraSerializer extends Serializer<OrthographicCamera> {
    /**
     * Writes the bytes for the object to the output.
     * <p>
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} write methods that accept a
     * serialier.
     *
     * @param kryo
     * @param output
     * @param object May be null if {@link #getAcceptsNull()} is true.
     */
    @Override
    public void write(Kryo kryo, Output output, OrthographicCamera object) {
        output.writeFloat(object.zoom);
        kryo.writeObject(output, object.position);
    }

    /**
     * Reads bytes and returns a new object of the specified concrete type.
     * <p>
     * Before Kryo can be used to read child objects, {@link Kryo#reference(Object)} must be called with the parent object to
     * ensure it can be referenced by the child objects. Any serializer that uses {@link Kryo} to read a child object may need to
     * be reentrant.
     * <p>
     * This method should not be called directly, instead this serializer can be passed to {@link Kryo} read methods that accept a
     * serialier.
     *
     * @param kryo
     * @param input
     * @param type
     * @return May be null if {@link #getAcceptsNull()} is true.
     */
    @Override
    public OrthographicCamera read(Kryo kryo, Input input, Class<OrthographicCamera> type) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.zoom = input.readFloat();
        camera.position.set(kryo.readObject(input, Vector3.class));
        return camera;
    }
}
