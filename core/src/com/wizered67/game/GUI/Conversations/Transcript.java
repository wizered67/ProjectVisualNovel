package com.wizered67.game.GUI.Conversations;

import java.util.LinkedList;

/**
 * Stores speaker and text of previous messages, up to a certain amount.
 * @author Adam Victor
 */
//todo add choices to transcript?
public class Transcript {
    private final static int MAX_STORED = 25; //todo raise to a good amount
    private LinkedList<TranscriptMessage> transcriptMessages;

    public Transcript() {
        transcriptMessages = new LinkedList<>();
    }

    public void addMessage(String speaker, String message) {
        transcriptMessages.addLast(new TranscriptMessage(speaker, message));
        while (transcriptMessages.size() > MAX_STORED) {
            transcriptMessages.removeFirst();
        }
    }

    public LinkedList<TranscriptMessage> getTranscriptMessages() {
        return transcriptMessages;
    }

    public static class TranscriptMessage {
        private String speaker;
        private String message;

        private TranscriptMessage() {}

        private TranscriptMessage(String speaker, String message) {
            this.speaker = speaker;
            this.message = message;
        }
        public String getSpeaker() {
            return speaker;
        }
        public String getMessage() {
            return message;
        }
    }
}
