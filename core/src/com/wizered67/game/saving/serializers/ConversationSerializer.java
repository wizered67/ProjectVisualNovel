package com.wizered67.game.saving.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.gui.GUIManager;

import java.util.HashMap;

/**
 * Serializer used for saving and loading Conversations. Only stores the
 * assignments map and name of conversation and uses these to reload.
 * @author Adam Victor
 */
public class ConversationSerializer extends Serializer<Conversation> {

    @Override
    public void write (Kryo kryo, Output output, Conversation object) {
        output.writeString(object.getName());
        kryo.writeObjectOrNull(output, object.getAllAssignments(), HashMap.class);
    }
    @Override
    public Conversation read (Kryo kryo, Input input, Class<Conversation> type) {
        String filename = input.readString();
        HashMap assignments = kryo.readObjectOrNull(input, HashMap.class);
        Conversation conversation = GameManager.conversationController().loadConversation(filename);
        conversation.setAssignments(assignments);
        return conversation;
    }
}
