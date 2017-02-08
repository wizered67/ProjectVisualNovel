package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.scene.SceneImage;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;

/**
 * Command that sets the position and depth of an image or group of images.
 * Any component not specified is left unchanged.
 * @author Adam Victor
 */
class ImagePositionCommand implements ConversationCommand {
    /** The instance identifier of the image to act on, or empty if it should be a group. */
    private String instance;
    /** The group of images to act on, only used if instance identifier is left empty. */
    private String groupIdentifier;
    /** The new position to set the image to, if specified. */
    private Vector2 position;
    /** Whether the x component of position has been specified and should be changed. */
    private boolean xSpecified;
    /** Whether the y component of position has been specified and should be changed. */
    private boolean ySpecified;
    /** The new depth to set the image to, if specified. */
    private int depth;
    /** Whether the depth has been specified and should be changed. */
    private boolean depthSpecified;
    /** Whether the changes should be made relative to the current position. */
    private boolean relative;

    ImagePositionCommand() {}

    ImagePositionCommand(String inst, String group, Vector2 pos, int depth, boolean rel) {
        instance = inst;
        groupIdentifier = group;
        position = pos;
        this.depth = depth;
        xSpecified = true;
        ySpecified = true;
        depthSpecified = true;
        relative = rel;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        final SceneManager manager = conversationController.sceneManager();
        manager.applyImageCommand(instance, groupIdentifier, new ImageAction() {
            @Override
            public void apply(SceneImage image) {
                if (xSpecified) {
                    float newX = position.x;
                    if (relative) {
                        newX += image.getPosition().x;
                    }
                    image.setX(newX);
                }
                if (ySpecified) {
                    float newY = position.y;
                    if (relative) {
                        newY += image.getPosition().y;
                    }
                    image.setY(newY);
                }
                if (depthSpecified) {
                    int newDepth = depth;
                    if (relative) {
                        newDepth += image.getDepth();
                    }
                    image.setDepth(manager, newDepth);
                }
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
    static ImagePositionCommand makeCommand(String instance, String group, XmlReader.Element element) {
        String xString = element.getAttribute("x", null);
        String yString = element.getAttribute("y", null);
        String depthString = element.getAttribute("depth", null);
        boolean relative = element.getBooleanAttribute("relative", false);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        int depth = (depthString != null) ? Integer.parseInt(depthString) : 0;
        ImagePositionCommand command = new ImagePositionCommand(instance, group, new Vector2(x, y), depth, relative);
        if (xString == null) {
            command.xSpecified = false;
        }
        if (yString == null) {
            command.ySpecified = false;
        }
        if (depthString == null) {
            command.depthSpecified = false;
        }
        return command;
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
