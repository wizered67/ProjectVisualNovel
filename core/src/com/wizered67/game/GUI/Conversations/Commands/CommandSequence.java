package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;

import java.io.IOException;
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
    /** Outputs XML to the XML WRITER for this command. */
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
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CommandSequence makeCommand(XmlReader.Element element) {
        CommandSequence cs = new CommandSequence();
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element c = element.getChild(i);
            ConversationCommand command = ConversationLoader.getCommand(c);
            cs.addCommand(command);
        }
        return cs;
    }
}
