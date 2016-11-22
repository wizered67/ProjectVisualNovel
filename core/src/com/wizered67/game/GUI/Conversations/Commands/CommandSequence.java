package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;
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

    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("sequence");
            for (ConversationCommand command : commands) {
                command.writeXml(xmlWriter);
            }
            xmlWriter.pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CommandSequence makeCommand(Conversation conversation, XmlReader.Element element) {
        CommandSequence cs = new CommandSequence();
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element c = element.getChild(i);
            ConversationCommand command = ConversationLoader.getCommand(conversation, c);
            cs.addCommand(command);
        }
        return cs;
    }
}
