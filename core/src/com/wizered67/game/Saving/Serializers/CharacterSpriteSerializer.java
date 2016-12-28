package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GUI.Conversations.CharacterSprite;

/**
 * Serializer used for saving and loading CharacterSprites.
 * @author Adam Victor
 */
public class CharacterSpriteSerializer extends FieldSerializer<CharacterSprite> {
    public CharacterSpriteSerializer (Kryo kryo, Class type) {
        super(kryo, type);
    }

    public CharacterSpriteSerializer (Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected CharacterSpriteSerializer (Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }
    @Override
    public void write (Kryo kryo, Output output, CharacterSprite object) {
        object.save();
        super.write(kryo, output, object);
    }
    @Override
    public CharacterSprite read (Kryo kryo, Input input, Class<CharacterSprite> type) {
        CharacterSprite c = super.read(kryo, input, type);
        c.reload();
        return c;
    }
}
