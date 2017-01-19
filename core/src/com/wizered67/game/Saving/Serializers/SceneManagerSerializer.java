package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GUI.Conversations.SceneManager;

/**
 * Serializer used for saving and loading SceneManagers.
 * @author Adam Victor
 */
public class SceneManagerSerializer extends FieldSerializer<SceneManager> {
    public SceneManagerSerializer(Kryo kryo, Class type) {
        super(kryo, type);
    }

    public SceneManagerSerializer(Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected SceneManagerSerializer(Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }

    @Override
    public void write(Kryo kryo, Output output, SceneManager object) {
        super.write(kryo, output, object);
    }

    @Override
    public SceneManager read(Kryo kryo, Input input, Class<SceneManager> type) {
        SceneManager sceneManager = super.read(kryo, input, type);
        sceneManager.setBackground(sceneManager.getBackgroundIdentifier());
        return sceneManager;
    }
}