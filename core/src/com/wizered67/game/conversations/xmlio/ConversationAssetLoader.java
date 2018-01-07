package com.wizered67.game.conversations.xmlio;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.Conversation;

/** Extends AsynchronousAssetLoader in order to load a Conversation asynchronously. */
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
        try {
            result = GameManager.conversationController().loader().loadConversation(file);
        } catch (ConversationParsingException e) {
            throw new GdxRuntimeException("Could not load conversation " + fileName);
        }
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