package com.wizered67.game.GUI.Conversations.XmlIO;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.GUIManager;


public class ConversationAssetLoader extends AsynchronousAssetLoader<Conversation, ConversationAssetLoader.ConvParameter> {

    private Conversation result;

    public ConversationAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }


    protected Conversation getLoadedConversation() {
        return result;
    }

    @Override
    public void loadAsync (AssetManager manager, String fileName, FileHandle file, ConvParameter parameter) {
        result = GUIManager.conversationController().loader().loadConversation(file);
    }

    @Override
    public Conversation loadSync (AssetManager manager, String fileName, FileHandle file, ConvParameter parameter) {
        Conversation conv = this.result;
        this.result = null;
        return conv;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, ConvParameter parameter) {
        return null;
    }

    static public class ConvParameter extends AssetLoaderParameters<Conversation> {
    }

}