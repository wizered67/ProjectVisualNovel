package com.wizered67.game.Saving.Serializers.LuaSerializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaValue;

/**
 * Serializer used for saving and loading LuaIntegers.
 * @author Adam Victor
 */
public class LuaIntegerSerializer extends Serializer<LuaInteger> {
    public void write (Kryo kryo, Output output, LuaInteger object) {
        output.writeInt(object.toint());
    }

    public LuaInteger read (Kryo kryo, Input input, Class<LuaInteger> type) {
        return LuaValue.valueOf(input.readInt());
    }
}
