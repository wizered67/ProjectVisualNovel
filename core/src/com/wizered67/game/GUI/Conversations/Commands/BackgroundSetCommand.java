package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

/**
 * Command to set the scene's background image.
 * @author Adam Victor
 */
public class BackgroundSetCommand implements ConversationCommand {
    /** Identifier of the Texture to set as the background. */
    private String imageIdentifier;

    /** Creates a BackgroundSetCommand that sets background to the texture with identifier ID. */
    public BackgroundSetCommand(String id) {
        imageIdentifier = id;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        conversationController.sceneManager().setBackground(imageIdentifier);
    }
    /**
     * Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static BackgroundSetCommand makeCommand(XmlReader.Element element) {
        String filename = element.getAttribute("id");
        return new BackgroundSetCommand(filename);
    }
    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
