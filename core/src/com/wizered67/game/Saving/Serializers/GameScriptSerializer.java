package com.wizered67.game.Saving.Serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.Scripting.GameScript;
import com.wizered67.game.Scripting.ScriptManager;

/**
 * Used to Serialize GameScripts for saving and loading. Stores language, whether it is a file,
 * and contents or filename. To read back it simply calls load method of the ScriptManager for
 * the corresponding language.
 * @author Adam Victor
 */
public class GameScriptSerializer extends Serializer<GameScript> {
    @Override
    public void write(Kryo kryo, Output output, GameScript object) {
        output.writeString(object.language);
        output.writeBoolean(object.isFile);
        output.writeString(object.script);
    }

    @Override
    public GameScript read(Kryo kryo, Input input, Class<GameScript> type) {
        String language = input.readString();
        boolean isFile = input.readBoolean();
        String script = input.readString();
        ScriptManager manager = ConversationController.scriptManager(language);
        return manager.load(script, isFile);
    }
}
