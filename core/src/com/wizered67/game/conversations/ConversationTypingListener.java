package com.wizered67.game.conversations;


import com.rafaskoberg.gdx.typinglabel.TypingListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TypingListener for use with conversations.
 * @author Adam Victor
 */
public class ConversationTypingListener implements TypingListener {
    private ConversationController conversationController;
    private static final Pattern COMMAND_EVENT_PATTERN = Pattern.compile("COMMAND_(.*)");
    private static final Pattern SCRIPTING_VARIABLE_LANGUAGE = Pattern.compile("SCRIPTING_(.*)_(.*)");
    private static final Pattern SCRIPTING_VARIABLE = Pattern.compile("SCRIPTING_(.*)");

    public ConversationTypingListener(ConversationController conversationController) {
        this.conversationController = conversationController;
    }

    /**
     * Called each time an {@code EVENT} token is processed.
     *
     * @param event Name of the event specified in the token. e.g. <tt>{EVENT=player_name}</tt> will have <tt>player_name</tt> as
     *              argument.
     */
    @Override
    public void event(String event) {
        Matcher commandMatcher = COMMAND_EVENT_PATTERN.matcher(event);
        if (commandMatcher.matches()) {
            String commandName = commandMatcher.group(1).trim();
            conversationController.setMessageSubcommand(commandName);
        }
    }

    /**
     * Called when the char progression reaches the end.
     */
    @Override
    public void end() {
        conversationController.addToTranscript();
    }

    /**
     * Called when variable tokens are replaced in text. This is an alternative method to deal with variables, other than directly
     * assigning replacement values to the label. Replacements returned by this method have priority over direct values, unless
     * {@code null} is returned.
     *
     * @param variable The variable name assigned to the <tt>{VAR}</tt> token. For example, in <tt>{VAR=townName}</tt>, the
     *                 variable will be <tt>townName</tt>
     * @return The replacement String, or {@code null} if this method should be ignored and the regular values should be used
     * instead.
     */
    @Override
    public String replaceVariable(String variable) {
        Matcher matcher = SCRIPTING_VARIABLE_LANGUAGE.matcher(variable);
        if (matcher.matches()) {
            String language = matcher.group(1);
            String var = matcher.group(2);
            return ConversationController.scriptManager(language).getStringValue(var);
        }
        matcher = SCRIPTING_VARIABLE.matcher(variable);
        if (matcher.matches()) {
            String var = matcher.group(1);
            return ConversationController.scriptManager(ConversationController.defaultScriptingLanguage()).getStringValue(var);
        }
        return null;
    }

    /**
     * Called when a new character is displayed. May be called many times per frame depending on the label configurations and text
     * speed. Useful to do a certain action each time a character is displayed, like playing a sound effect.
     *
     * @param ch
     */
    @Override
    public void onChar(Character ch) {
        if (!Character.isSpaceChar(ch)) {
            conversationController.playTextSound();
        }
    }
}
