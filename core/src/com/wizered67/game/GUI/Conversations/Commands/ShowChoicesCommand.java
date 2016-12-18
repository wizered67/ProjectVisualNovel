package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.Conversations.ConversationController;

import java.io.IOException;
import java.util.ArrayList;
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
    private final int MAX_CHOICES = 4;
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
            if (conditions[i] == null || conditions[i].conditionMet()) {
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
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("choices");
            for (int i = 0; i < choicesText.length; i += 1) {
                xmlWriter.element("choice")
                        .attribute("name", choicesText[i]);
                //Todo fix?
                //choicesCommands[i].writeXml(xmlWriter);
                xmlWriter.pop();
            }
            xmlWriter.pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static ShowChoicesCommand makeCommand(XmlReader.Element element) {
        int numChoices = element.getChildCount();
        String[] textChoices = new String[numChoices];
        @SuppressWarnings("unchecked")
        List<ConversationCommand>[] commandChoices = new List[numChoices];
        VariableConditionCommand[] conditions = new VariableConditionCommand[numChoices];
        for (int i = 0; i < numChoices; i += 1) {
            XmlReader.Element c = element.getChild(i);
            textChoices[i] = c.getAttribute("name");
            for (int j = 0; j < c.getChildCount(); j += 1) {
                ConversationCommand command = ConversationLoader.getCommand(c.getChild(j));
                if (command instanceof VariableConditionCommand) {
                    conditions[i] = (VariableConditionCommand) command;
                } else {
                    if (commandChoices[i] == null) {
                        commandChoices[i] = new ArrayList<ConversationCommand>();
                    }
                    commandChoices[i].add(command);
                }
            }
        }
        return new ShowChoicesCommand(textChoices, commandChoices, conditions);
    }


}
