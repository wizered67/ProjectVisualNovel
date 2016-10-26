package com.wizered67.game.GUI.Conversations;

/**
 * Created by Adam on 10/24/2016.
 */
public interface ConversationCommand {
    void execute(MessageWindow messageWindow);
    boolean waitForInput();
}
