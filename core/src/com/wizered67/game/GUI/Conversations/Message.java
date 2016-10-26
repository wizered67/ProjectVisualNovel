package com.wizered67.game.GUI.Conversations;

/**
 * Created by Adam on 10/24/2016.
 */
public class Message implements ConversationCommand {
    private String text;
    private String speaker;
    private String speakerSound;

    public Message(String s, String t) {
        this(s, t, "talksoundmale");
    }

    public Message(String s, String t, String ss) {
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
