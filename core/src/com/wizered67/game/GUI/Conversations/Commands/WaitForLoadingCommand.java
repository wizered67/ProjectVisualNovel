package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;
import com.wizered67.game.GameManager;

/**
 * Conversation Command that waits until all queued resources have been loaded.
 * @author Adam Victor
 */
public class WaitForLoadingCommand implements ConversationCommand {

    public WaitForLoadingCommand() {

    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {

    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return GameManager.assetManager().getQueuedAssets() != 0;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {

    }

    public static WaitForLoadingCommand makeCommand(XmlReader.Element element) {
        return new WaitForLoadingCommand();
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
