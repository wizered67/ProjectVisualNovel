package com.wizered67.game.Saving.Serializers.LuaSerializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaValue;

/**
 * Created by Adam on 12/22/2016.
 */
public class LuaBooleanSerializer extends Serializer<LuaBoolean> {
    public void write (Kryo kryo, Output output, LuaBoolean object) {
        output.writeBoolean(object.booleanValue());
    }

    public LuaBoolean read (Kryo kryo, Input input, Class<LuaBoolean> type) {
        return LuaValue.valueOf(input.readBoolean());
    }
}
