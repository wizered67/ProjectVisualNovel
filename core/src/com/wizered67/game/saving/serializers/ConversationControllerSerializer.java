package com.wizered67.game.saving.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializerConfig;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.gui.GUIManager;

/**
 * Serializer used for saving and loading ConversationControllers.
 * @author Adam Victor
 */
public class ConversationControllerSerializer extends FieldSerializer<ConversationController> {
    public ConversationControllerSerializer(Kryo kryo, Class type) {
        super(kryo, type);
    }

    public ConversationControllerSerializer(Kryo kryo, Class type, Class[] generics) {
        super(kryo, type, generics);
    }

    protected ConversationControllerSerializer(Kryo kryo, Class type, Class[] generics, FieldSerializerConfig config) {
        super(kryo, type, generics, config);
    }
    @Override
    public void write (Kryo kryo, Output output, ConversationController object) {
        object.save();
        super.write(kryo, output, object);
    }
    @Override
    public ConversationController read (Kryo kryo, Input input, Class<ConversationController> type) {
        ConversationController conversationController = super.read(kryo, input, type);
        conversationController.reload();
        return conversationController;
    }
    @Override
    public ConversationController create (Kryo kryo, Input input, Class type) {
        return GameManager.conversationController();
    }
}
