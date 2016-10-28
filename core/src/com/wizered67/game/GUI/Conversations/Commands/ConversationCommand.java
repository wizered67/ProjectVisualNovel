package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * Created by Adam on 10/24/2016.
 */
public interface ConversationCommand {
    void execute(MessageWindow messageWindow);
    boolean waitToProceed();
    void complete(CompleteEvent c);
}
