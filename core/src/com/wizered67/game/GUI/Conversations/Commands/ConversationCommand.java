package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

/**
 * Interface for all ConversationCommands which are executed in order
 * in ConversationController.
 * @author Adam Victor
 */
public interface ConversationCommand {
    /** Executes the command on the CONVERSATION CONTROLLER. */
    void execute(ConversationController conversationController);
    /** Whether to wait before proceeding to the next command in the branch. */
    boolean waitToProceed();
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    void complete(CompleteEvent c);
    /** Outputs XML to the XML WRITER for this command. */
    void writeXml(XmlWriter xmlWriter);
    /** All ConversationCommands also have a static makeCommand method
     * which converts from XML to a ConversationCommand.
     */
}
