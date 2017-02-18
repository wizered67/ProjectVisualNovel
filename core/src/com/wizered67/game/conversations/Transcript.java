package com.wizered67.game.conversations;

import java.util.LinkedList;

/**
 * Stores speaker and text of previous messages, up to a certain amount.
 * @author Adam Victor
 */

public class Transcript {
    /** The maximum number of messages to store at once. */
    private final static int MAX_STORED = 25;
    /** The list of stored messages/speakers. */
    private LinkedList<TranscriptMessage> transcriptMessages;

    public Transcript() {
        transcriptMessages = new LinkedList<>();
    }

    /** Add a new TranscriptMessage with speaker SPEAKER and text MESSAGE to the end of
     * the list of stored messages. If the size of the list is greater than the maximum,
     * remove older messages from the beginning until it is not. */
    public void addMessage(String speaker, String message) {
        transcriptMessages.addLast(new TranscriptMessage(speaker, message));
        while (transcriptMessages.size() > MAX_STORED) {
            transcriptMessages.removeFirst();
        }
    }

    public LinkedList<TranscriptMessage> getTranscriptMessages() {
        return transcriptMessages;
    }
    /** Used to store previous message data, namely speaker and message. */
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
