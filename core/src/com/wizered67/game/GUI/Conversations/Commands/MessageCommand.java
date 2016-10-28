package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * Created by Adam on 10/24/2016.
 */
public class MessageCommand implements ConversationCommand {
    private String text;
    private String speaker;
    private String speakerSound;
    private boolean done;

    public MessageCommand(String s, String t) {
        this(s, t, "talksoundmale");
    }

    public MessageCommand(String s, String t, String ss) {
        speaker = s;
        text = t;
        speakerSound = ss;
        done = false;
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
        done = false;
    }

    @Override
    public boolean waitToProceed() {
        return !done;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.INPUT) {
            done = true;
        }
    }
}
