package com.wizered67.game.GUI.Conversations;

/**
 * Created by Adam on 10/24/2016.
 */
public class Message implements ConversationCommand {
    private String text;
    private String speaker;

    public Message(String s, String t) {
        speaker = s;
        text = t;
    }

    public String getText() {
        return text;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void execute(MessageWindow messageWindow) {
        messageWindow.setRemainingText(text);
        messageWindow.setSpeaker(speaker);
    }

    @Override
    public boolean waitForInput() {
        return true;
    }
}
