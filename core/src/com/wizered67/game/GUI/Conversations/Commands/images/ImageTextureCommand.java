package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.scene.SceneImage;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;

/**
 * Created by Adam on 1/27/2017.
 */
class ImageTextureCommand implements ConversationCommand {
    private String newTexture;
    private String instanceIdentifier;
    private String groupIdentifier;

    ImageTextureCommand() {}

    ImageTextureCommand(String instance, String group, String texture) {
        instanceIdentifier = instance;
        groupIdentifier = group;
        newTexture = texture;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        SceneManager manager = conversationController.sceneManager();
        manager.applyImageCommand(instanceIdentifier, groupIdentifier, new ImageAction() {
            @Override
            public void apply(SceneImage image) {
                image.setTexture(newTexture);
            }
        });
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
    /** Static method to create a new command from XML Element ELEMENT. */
    static ImageTextureCommand makeCommand(String instance, String group, XmlReader.Element element) {
        String newTexture = element.getAttribute("id");
        return new ImageTextureCommand(instance, group, newTexture);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
