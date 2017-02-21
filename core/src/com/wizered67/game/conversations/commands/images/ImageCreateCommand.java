package com.wizered67.game.conversations.commands.images;

import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.scene.SceneImage;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * Creates a new image. When executed it instantiates a new SceneImage
 * with the specified instance identifier and adds it to the SceneManager.
 * @author Adam Victor
 */
class ImageCreateCommand implements ConversationCommand {
    /** The instance identifier the created image will have. Should be unique. */
    private String instance;

    ImageCreateCommand() {}
    ImageCreateCommand(String inst) {
        instance = inst;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        SceneManager manager = conversationController.sceneManager();
        if (manager.getImage(instance) == null) {
            manager.addImage(new SceneImage(instance));
        }

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
}
