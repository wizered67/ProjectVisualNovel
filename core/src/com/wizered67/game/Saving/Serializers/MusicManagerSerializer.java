package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GameManager;
import com.wizered67.game.MusicManager;

/**
 * Serializer used for saving and loading the MusicManager.
 * @author Adam Victor
 */
public class MusicManagerSerializer extends FieldSerializer<MusicManager> {
    public MusicManagerSerializer (Kryo kryo, Class type) {
        super(kryo, type);
    }

    public MusicManagerSerializer (Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected MusicManagerSerializer (Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }
    @Override
    public void write (Kryo kryo, Output output, MusicManager object) {
        super.write(kryo, output, object);
    }
    @Override
    public MusicManager read (Kryo kryo, Input input, Class<MusicManager> type) {
        MusicManager m = super.read(kryo, input, type);
        m.reload();
        return m;
    }
    @Override
    public MusicManager create (Kryo kryo, Input input, Class type) {
        return GameManager.musicManager();
    }
}
