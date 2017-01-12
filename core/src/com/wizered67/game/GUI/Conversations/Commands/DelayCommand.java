package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

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
    /** Whether the command can be skipped with input. */
    private boolean canSkip;
    /** No arguments constructor. */
    public DelayCommand() {
        delayTime = 0;
        done = true;
        canSkip = false;
    }
    /** Creates a new DelayCommand that delays for TIME seconds when executed. Iff SKIP,
     * it can be skipped with input. */
    public DelayCommand(float time, boolean skip) {
        delayTime = time;
        canSkip = skip;
        done = false;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
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
        if (canSkip && c.type == CompleteEvent.Type.INPUT) {
            done = true;
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static DelayCommand makeCommand(XmlReader.Element element) {
        float time = element.getFloatAttribute("time", 0f);
        boolean canSkip = element.getBooleanAttribute("skippable", false);
        return new DelayCommand(time, canSkip);
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
