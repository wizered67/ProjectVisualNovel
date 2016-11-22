package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

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
