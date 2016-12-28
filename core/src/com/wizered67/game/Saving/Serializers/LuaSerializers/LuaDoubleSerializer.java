package com.wizered67.game.Saving.Serializers.LuaSerializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.luaj.vm2.LuaDouble;
import org.luaj.vm2.LuaValue;

/**
 * Serializer used for saving and loading LuaDoubles.
 * @author Adam Victor
 */
public class LuaDoubleSerializer extends Serializer<LuaDouble> {
    public void write (Kryo kryo, Output output, LuaDouble object) {
        output.writeDouble(object.todouble());
    }

    public LuaDouble read (Kryo kryo, Input input, Class<LuaDouble> type) {
        return (LuaDouble) LuaValue.valueOf(input.readDouble());
    }
}
