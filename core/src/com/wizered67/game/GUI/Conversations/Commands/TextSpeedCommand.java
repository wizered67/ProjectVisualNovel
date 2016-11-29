package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * A ConversationCommand that sets the speed of the text being
 * displayed in the MessageWindow.
 * @author Adam Victor
 */
public class TextSpeedCommand implements ConversationCommand {
    /** The number of frames that will be waited before a text update. */
    private int textSpeed;

    /** Creates a new TextSpeedCommand that sets the number of frames to wait before a text update
     * to TIME when executed.
     */
    public TextSpeedCommand(int time) {
        textSpeed = time;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        messageWindow.setTextTimer(textSpeed);
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
    public static TextSpeedCommand makeCommand(XmlReader.Element element) {
        int time = element.getIntAttribute("time");
        return new TextSpeedCommand(time);
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        //TODO add this?
    }
}
