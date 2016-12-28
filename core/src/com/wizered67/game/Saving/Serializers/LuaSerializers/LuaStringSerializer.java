package com.wizered67.game.Saving.Serializers.LuaSerializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;

/**
 * Serializer used for saving and loading LuaStrings.
 * @author Adam Victor
 */
public class LuaStringSerializer extends Serializer<LuaString> {
    public void write (Kryo kryo, Output output, LuaString object) {
        output.writeString(object.toString());
    }

    public LuaString read (Kryo kryo, Input input, Class<LuaString> type) {
        return LuaValue.valueOf(input.readString());
    }
}
