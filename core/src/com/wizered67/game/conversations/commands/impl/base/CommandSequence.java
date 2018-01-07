package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

import java.util.LinkedList;

/**
 * A ConversationCommand that contains a sequence of ConversationCommands
 * to be executed consecutively.
 * @author Adam Victor
 */
public class CommandSequence implements ConversationCommand {
    /** A list of the ConversationCommands to be executed in sequence. */
    private LinkedList<ConversationCommand> commands;
    /** Creates a new CommandSequence with commands COM. */
    public CommandSequence(LinkedList<ConversationCommand> com) {
        commands = com;
    }

    /** Creates a new CommandSequence with an empty list of
     * ConversationCommands and then adds all of the COMS to the list.
     */
    public CommandSequence(ConversationCommand... coms) {
        this();
        for (ConversationCommand command : coms) {
            commands.addLast(command);
        }
    }

    /** Creates a new CommandSequence with an empty list of
     * ConversationCommands. */
    public CommandSequence() {
        commands = new LinkedList<ConversationCommand>();
    }

    /** Adds the ConversationCommand COMMAND to the list of ConversationCommands
     * to be executed when this CommandSequence is.
     */
    public void addCommand(ConversationCommand command) {
        commands.add(command);
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        conversationController.insertCommands(commands);
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
}
