package com.wizered67.game.conversations.commands.impl.scene.images;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scene.EntityAction;
import com.wizered67.game.conversations.scene.SceneImage;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * A command for changing the group of a single image or a group of images, depending
 * on whether an instanceIdentifier was specified or left empty. Executing this command
 * invokes the changeGroup method on specified image(s).
 * @author Adam Victor
 */
public class ImageGroupChangeCommand implements ConversationCommand {
    /** The instance identifier of the image to change, or empty if an
     * entire group should be changed. */
    private String instanceIdentifier;
    /** The group identifier of the set of images to change, used only if
     * instanceIdentifier is empty. */
    private String groupIdentifier;
    /** The new group that the image or set of images should be assigned to. */
    private String newGroup;

    ImageGroupChangeCommand() {}

    ImageGroupChangeCommand(String instance, String group, String nGroup) {
        instanceIdentifier = instance;
        groupIdentifier = group;
        newGroup = nGroup;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        final SceneManager manager = conversationController.currentSceneManager();
        manager.applyImageCommand(instanceIdentifier, groupIdentifier, new EntityAction<SceneImage>() {
            @Override
            public void apply(SceneImage image) {
                image.changeGroup(manager, newGroup);
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
    public static ImageGroupChangeCommand makeCommand(String instanceIdentifier, String groupIdentifier, XmlReader.Element element) {
        XmlReader.Element text = element.getChild(0);
        if (!text.getName().equals("text")) {
            throw new IllegalArgumentException("Group change element must have text");
        }
        String newGroup = text.getText();
        return new ImageGroupChangeCommand(instanceIdentifier, groupIdentifier, newGroup);
    }
}
