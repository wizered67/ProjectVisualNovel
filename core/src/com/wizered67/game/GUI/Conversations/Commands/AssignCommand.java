package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * ConversationCommand used to assign a mapping of strings to ConversationCommands
 * for use later in the conversation.
 * @author Adam Victor
 */
public class AssignCommand implements ConversationCommand {
    /** Mapping of Strings to ConversationCommands. */
    private Map<String, ConversationCommand> assignments;

    /** No arguments constructor. */
    public AssignCommand() {
        assignments = null;
    }
    /** Create an AssignCommand with assignments map of MAP. */
    public AssignCommand(Map<String, ConversationCommand> map) {
        assignments = map;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        for (String key : assignments.keySet()) {
            conversationController.conversation().addAssignment(key, assignments.get(key));
        }
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
    /** Static method to create a new command from XML Element ELEMENT. */
    public static AssignCommand makeCommand(XmlReader.Element element) {
        String name = "";
        Map<String, ConversationCommand> map = new HashMap<String, ConversationCommand>();
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element child = element.getChild(i);
            if (i % 2 == 0) {
                name = child.getText().trim();
            } else {
                map.put(name, ConversationLoader.getCommand(child));
            }
        }
        return new AssignCommand(map);
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        //todo add this?
    }
}
