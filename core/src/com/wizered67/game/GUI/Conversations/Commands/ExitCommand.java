package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;

/**
 * ConversationCommand to exit the current branch, hide choices and labels, and potentially clear scene.
 * @author Adam Victor
 */
public class ExitCommand implements ConversationCommand {
    private boolean hideEntities;
    public ExitCommand() {}

    public ExitCommand(boolean remove) {
        hideEntities = remove;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        conversationController.exit(hideEntities);
    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return false;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {

    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }

    public static ExitCommand makeCommand(XmlReader.Element element) {
        boolean remove = element.getBooleanAttribute("clearScene", false);
        return new ExitCommand(remove);
    }
}
