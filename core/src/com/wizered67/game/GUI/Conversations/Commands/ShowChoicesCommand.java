package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.Gdx;
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
    private static final int MAX_CHOICES = 4;

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
        int numChoices = MAX_CHOICES;
        String[] textChoices = new String[numChoices];
        @SuppressWarnings("unchecked")
        List<ConversationCommand>[] commandChoices = new List[numChoices];
        for (int i = 0; i < numChoices; i += 1) {
            commandChoices[i] = new ArrayList<ConversationCommand>();
        }
        VariableConditionCommand[] conditions = new VariableConditionCommand[numChoices];
        int choiceNum = -1;
        boolean afterChoice = false;
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element elem = element.getChild(i);
            if (elem.getName().equalsIgnoreCase("text")) { //block of text. Iterate through to separate into message/choices
                //also handle case where condition directly follows choice
                String text = elem.getText().replaceAll("\\r", "");
                String[] lines = text.split("\\n");
                String messages = "";
                for (String line : lines) {
                    line = line.trim();
                    if (line.charAt(line.length() - 1) == ':' && line.charAt(line.length() - 2) != '\\') {
                        if (!messages.isEmpty()) {
                            addMessageCommand(messages, choiceNum, commandChoices);
                        }
                        messages = "";
                        choiceNum += 1;
                        textChoices[choiceNum] = line.substring(0, line.length() - 1);
                        afterChoice = true;
                    } else {
                        line = line.replaceAll("\\\\:", ":");
                        messages += (" " + line + "\n");
                        afterChoice = false;
                    }
                }
                if (!messages.isEmpty()) {
                    addMessageCommand(messages, choiceNum, commandChoices);
                }
            } else {
                ConversationCommand c = ConversationLoader.getCommand(elem);
                if (afterChoice && c instanceof VariableConditionCommand) {
                    conditions[choiceNum] = (VariableConditionCommand) c;
                } else {
                    List<ConversationCommand> commands = commandChoices[choiceNum];
                    commands.add(c);
                }
                afterChoice = false;
            }
        }
        return new ShowChoicesCommand(textChoices, commandChoices, conditions);
    }
    /** Helper method for adding a MessageCommand with content MESSAGES to command list of choice CHOICENUM. */
    private static void addMessageCommand(String messages, int choiceNum, List<ConversationCommand>[] commandChoices) {
        if (choiceNum < 0) {
            Gdx.app.error("Command Parser", "Trying to add message before choice in choices block.");
        } else {
            commandChoices[choiceNum].add(MessageCommand.makeCommand(messages));
        }
    }

}
