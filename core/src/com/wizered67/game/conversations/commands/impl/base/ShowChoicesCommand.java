package com.wizered67.game.conversations.commands.impl.base;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scripting.VariableConditionCommand;

import java.util.List;

/**
 * A ConversationCommand that displays choices to the player and
 * executes a ConversationCommand depending on which one is chosen.
 * @author Adam Victor
 */
public class ShowChoicesCommand implements ConversationCommand {
    /** Array of Strings containing text for choices. */
    private String[] choicesText;
    /** Array of ConversationCommands to be executed when the corresponding choice is selected. */
    private List<ConversationCommand>[] choicesCommands;
    /** Array of VariableConditionCommands. The ith choice is only added if conditions[i] is null or evaluates to true. */
    private VariableConditionCommand[] conditions;
    /** Whether this ShowChoiceCommand is done displaying. Set to false initially until choice is made. */
    private boolean done;
    /** Maximum number of choices that can be shown at once. */
    public static final int MAX_CHOICES = 4;

    /** No arguments constructor. */
    public ShowChoicesCommand() {
        choicesText = null;
        choicesCommands  = null;
        conditions = null;
        done = true;
    }
    /** Creates a new ShowChoiceCommand that shows the choices stored in TEXT
     * with corresponding COMMANDS when executed. */
    public ShowChoicesCommand(String[] text, List<ConversationCommand>[] commands, VariableConditionCommand[] cond) {
        choicesText = text;
        choicesCommands = commands;
        conditions = cond;
        done = false;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        int numAdded = 0;
        for (int i = 0; i < MAX_CHOICES; i += 1) {
            conversationController.setChoice(i, "");
        }
        for (int i = 0; i < choicesText.length; i += 1) {
            if (choicesText[i] != null && (conditions[i] == null || conditions[i].conditionMet())) {
                conversationController.setChoice(numAdded, choicesText[i]);
                conversationController.setChoiceCommand(numAdded, choicesCommands[i]);
                numAdded += 1;
            }
        }
        conversationController.setChoiceShowing(true);
        done = false;
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return !done;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.CHOICE) {
            done = true;
        }
    }
}
