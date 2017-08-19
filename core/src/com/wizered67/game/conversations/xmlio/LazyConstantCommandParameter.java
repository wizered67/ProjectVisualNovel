package com.wizered67.game.conversations.xmlio;

import com.wizered67.game.conversations.ConversationController;

/**
 * Lazily evaluated Command Parameter which has a constant value and is not a variable or expression.
 * @author Adam Victor
 */
public class LazyConstantCommandParameter<T> implements LazyCommandParameter<T> {
    private T m_value;

    public LazyConstantCommandParameter(T value) {
        m_value = value;
    }

    @Override
    public T evaluate(ConversationController conversationController) {
        return m_value;
    }
}
