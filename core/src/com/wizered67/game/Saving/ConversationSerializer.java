package com.wizered67.game.Saving;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.MusicManager;

/**
 * Created by Adam on 12/21/2016.
 */
public class ConversationSerializer extends FieldSerializer<ConversationController> {
    public ConversationSerializer (Kryo kryo, Class type) {
        super(kryo, type);
    }

    public ConversationSerializer (Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected ConversationSerializer (Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }
    @Override
    public void write (Kryo kryo, Output output, ConversationController object) {
        Gdx.app.log("Serialization", "Wrote conversation.");
        super.write(kryo, output, object);
    }
    @Override
    public ConversationController read (Kryo kryo, Input input, Class<ConversationController> type) {
        Gdx.app.log("Serialization", "Read conversation.");
        ConversationController conversationController = super.read(kryo, input, type);
        conversationController.reload();
        //kryo.reference(conversationController);
        return conversationController;
    }
    @Override
    public ConversationController create (Kryo kryo, Input input, Class type) {
        return GUIManager.conversationController();
    }
}
