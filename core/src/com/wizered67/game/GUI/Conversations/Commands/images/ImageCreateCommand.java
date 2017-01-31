package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneImage;
import com.wizered67.game.GUI.Conversations.SceneManager;

/**
 * Created by Adam on 1/30/2017.
 */
class ImageCreateCommand implements ConversationCommand {
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
