package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GUI.Conversations.scene.SceneCharacter;
import com.wizered67.game.GUI.Conversations.scene.SceneEntity;

/**
 * Created by Adam on 2/2/2017.
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
