package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

import java.io.IOException;

/**
 * A ConversationCommand for displaying messages for
 * @author Adam Victor
 */
public class DebugCommand implements ConversationCommand {
    /** The debug message to show. */
    private String message;
    /** No arguments constructor. */
    public DebugCommand() {
        message = "";
    }
    /** Creates a new DebugCommand that prints out M when executed. */
    public DebugCommand(String m) {
        message = m;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        System.out.println(message);
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
            xmlWriter.element("debug")
                    .attribute("message", message)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static DebugCommand makeCommand(XmlReader.Element element) {
        String text = element.getAttribute("message");
        return new DebugCommand(text);
    }
}
