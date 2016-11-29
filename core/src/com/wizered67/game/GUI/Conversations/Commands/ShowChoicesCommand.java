package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * A ConversationCommand that displays choices to the player and
 * executes a ConversationCommand depending on which one is chosen.
 * @author Adam Victor
 */
public class ShowChoicesCommand implements ConversationCommand {
    /** Array of Strings containing text for choices. */
    private String[] choicesText;
    /** Array of ConversationCommands to be executed when the corresponding choice is selected. */
    private ConversationCommand[] choicesCommands;
    /** Whether this ShowChoiceCommand is done displaying. Set to false initially until choice is made. */
    private boolean done;
    /** Creates a new ShowChoiceCommand that shows the choices stored in TEXT
     * with corresponding COMMANDS when executed. */
    public ShowChoicesCommand(String[] text, ConversationCommand[] commands) {
        choicesText = text;
        choicesCommands = commands;
        done = false;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        for (int i = 0; i < choicesText.length; i += 1) {
            messageWindow.setChoice(i, choicesText[i]);
            messageWindow.setChoiceCommand(i, choicesCommands[i]);
        }
        messageWindow.setChoiceShowing(true);
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
                choicesCommands[i].writeXml(xmlWriter);
                xmlWriter.pop();
            }
            xmlWriter.pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT that is part of CONVERSATION. */
    public static ShowChoicesCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        int numChoices = element.getChildCount();
        String[] textChoices = new String[numChoices];
        ConversationCommand[] commandChoices = new ConversationCommand[numChoices];
        for (int i = 0; i < numChoices; i += 1) {
            XmlReader.Element c = element.getChild(i);
            textChoices[i] = c.getAttribute("name");
            ConversationCommand command = ConversationLoader.getCommand(conversation, c.getChild(0));
            commandChoices[i] = command;
        }
        return new ShowChoicesCommand(textChoices, commandChoices);
    }


}
