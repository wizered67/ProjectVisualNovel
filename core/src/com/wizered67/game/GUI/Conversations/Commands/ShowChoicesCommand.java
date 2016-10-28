package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * Created by Adam on 10/27/2016.
 */
public class ShowChoicesCommand implements ConversationCommand {

    private String[] choicesText;
    private ConversationCommand[] choicesCommands;
    private boolean done;

    public ShowChoicesCommand(String[] text, ConversationCommand[] commands) {
        choicesText = text;
        choicesCommands = commands;
        done = false;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        for (int i = 0; i < choicesText.length; i += 1) {
            messageWindow.setChoice(i, choicesText[i]);
            messageWindow.setChoiceCommand(i, choicesCommands[i]);
        }
        messageWindow.setChoiceShowing(true);
        done = false;
    }

    @Override
    public boolean waitToProceed() {
        return !done;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.CHOICE) {
            done = true;
        }
    }
}
