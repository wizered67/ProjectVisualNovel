package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.util.LinkedList;

/**
 * Created by Adam on 10/26/2016.
 */
public class CommandSequence implements ConversationCommand{
    private LinkedList<ConversationCommand> commands;

    public CommandSequence(LinkedList<ConversationCommand> com) {
        commands = com;
    }

    public CommandSequence(ConversationCommand... coms) {
        this();
        for (ConversationCommand command : coms) {
            commands.addLast(command);
        }
    }

    public CommandSequence() {
        commands = new LinkedList<ConversationCommand>();
    }

    public void addCommand(ConversationCommand command) {
        commands.add(command);
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        for (ConversationCommand c : commands) {
            c.execute(messageWindow);
        }
    }

    @Override
    public boolean waitToProceed() {
        return false;
    }

    @Override
    public void complete(CompleteEvent c) {

    }
}
