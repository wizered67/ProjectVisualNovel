package com.wizered67.game.conversations.xmlio;

import com.wizered67.game.conversations.ConversationController;

/**
 * Lazily evaluated Command Parameter that gets its value from evaluating an expression in a ScriptManager.
 * @author Adam Victor
 */
public class LazyVariableCommandParameter<T> implements LazyCommandParameter<T> {

    private String m_scriptingLanguage;
    private String m_expression;
    private Class<T> m_type;
    private T m_defaultValue;

    public LazyVariableCommandParameter(String scriptingLanguage, String expression, Class<T> type, T defaultValue) {
        m_scriptingLanguage = scriptingLanguage;
        m_expression = expression;
        m_type = type;
        m_defaultValue = defaultValue;
    }

    @Override
    public T evaluate(ConversationController conversationController) {
        //todo this will need to be changed if/when script managers are made non static.
        try {
            String language = m_scriptingLanguage != null ? m_scriptingLanguage : ConversationController.defaultScriptingLanguage();
            T value = m_type.cast( ConversationController.scriptManager(language).getExpressionValue(m_expression, m_type));
            if (value != null) {
                return value;
            } else {
                return m_defaultValue;
            }
        } catch (ClassCastException e) {
            return m_defaultValue;
        }
    }
}
