package com.wizered67.game.saving.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.conversations.scene.SceneEntity;

/**
 * Serializer used to save and load SceneEntities (characters and images). When writing, it
 * first calls the entities save method to store any important information to fields. Then, field
 * serializer saves all the fields. When reading, field serializer reads all fields and then calls
 * reload to restore state.
 * @author Adam Victor
 */

public class SceneEntitySerializer extends FieldSerializer<SceneEntity> {
    public SceneEntitySerializer (Kryo kryo, Class type) {
        super(kryo, type);
    }

    public SceneEntitySerializer (Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected SceneEntitySerializer (Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }
    @Override
    public void write (Kryo kryo, Output output, SceneEntity object) {
        object.save();
        super.write(kryo, output, object);
    }
    @Override
    public SceneEntity read (Kryo kryo, Input input, Class<SceneEntity> type) {
        SceneEntity c = super.read(kryo, input, type);
        c.reload();
        return c;
    }
}
