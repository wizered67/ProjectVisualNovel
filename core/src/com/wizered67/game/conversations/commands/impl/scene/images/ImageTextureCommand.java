package com.wizered67.game.conversations.commands.impl.scene.images;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scene.EntityAction;
import com.wizered67.game.conversations.scene.SceneImage;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * Command that sets the texture of an image or group of images.
 * @author Adam Victor
 */
public class ImageTextureCommand implements ConversationCommand {
    /** The resource identifier of the texture the image should be changed to. */
    private String newTexture;
    /** The instance identifier of the image to change. If empty, the command is applied to a group. */
    private String instanceIdentifier;
    /** The group of images to change, only used if instanceIdentifier is empty. */
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
        SceneManager manager = conversationController.currentSceneManager();
        manager.applyImageCommand(instanceIdentifier, groupIdentifier, new EntityAction<SceneImage>() {
            @Override
            public void apply(SceneImage image) {
                image.setCurrentAnimation(newTexture);
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
    public static ImageTextureCommand makeCommand(String instance, String group, XmlReader.Element element) {
        String newTexture = element.getAttribute("id");
        return new ImageTextureCommand(instance, group, newTexture);
    }
}
