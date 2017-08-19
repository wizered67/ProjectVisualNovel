package com.wizered67.game.conversations.xmlio;

import com.wizered67.game.conversations.ConversationController;

/**
 * Interface for Command Parameters that are lazily evaluated. Intended to be used for parameters that may be a variable or expression
 * and needs to be replaced by using a ScriptManager referenced through a Conversation Controller.
 * @author Adam Victor
 */
public interface LazyCommandParameter<T> {
    T evaluate(ConversationController conversationController);
}
