package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * A ConversationCommand that inserts a pause before the next
 * ConversationCommand is executed.
 * @author Adam Victor
 */
public class DelayCommand implements ConversationCommand {
    /** How long to delay before executing the next command. */
    private float delayTime;
    /** Whether this DelayCommand has finished executing and the delay is over. */
    private boolean done;
    /** Creates a new DelayCommand that delays for TIME seconds when executed. */
    public DelayCommand(float time) {
        delayTime = time;
        done = false;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        done = false;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                done = true;
            }
        }, delayTime);
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

    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static DelayCommand makeCommand(XmlReader.Element element) {
        float time = element.getFloatAttribute("time", 0f);
        return new DelayCommand(time);
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
