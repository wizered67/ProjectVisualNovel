package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * Created by Adam on 10/24/2016.
 */
public class MessageCommand implements ConversationCommand {
    private String text;
    private String speaker;
    private String speakerSound;

    public MessageCommand(String s, String t) {
        this(s, t, "talksoundmale");
    }

    public MessageCommand(String s, String t, String ss) {
        speaker = s;
        text = t;
        speakerSound = ss;
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
        messageWindow.setCurrentSpeakerSound(speakerSound);
    }

    @Override
    public boolean waitForInput() {
        return true;
    }
}
