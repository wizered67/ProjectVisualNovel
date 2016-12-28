package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.Scripting.ScriptManager;

/**
 * Used to serialize ScriptManagers for saving and loading. Only saves language name to load from there.
 * @author Adam Victor
 */
public class ScriptManagerSerializer extends Serializer<ScriptManager> {

    @Override
    public void write(Kryo kryo, Output output, ScriptManager object) {
        output.writeString(object.name());
    }

    @Override
    public ScriptManager read(Kryo kryo, Input input, Class<ScriptManager> type) {
        return ConversationController.scriptManager(input.readString());
    }
}
