package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneImage;
import com.wizered67.game.GUI.Conversations.SceneManager;

import java.util.Set;

/**
 * Created by Adam on 1/27/2017.
 */
class ImagePositionCommand implements ConversationCommand {
    private String instance;
    private String groupIdentifier;
    private Vector2 position;
    private boolean xSpecified;
    private boolean ySpecified;
    private int depth;
    private boolean depthSpecified;

    ImagePositionCommand() {}

    ImagePositionCommand(String inst, String group, Vector2 pos, int depth) {
        instance = inst;
        groupIdentifier = group;
        position = pos;
        this.depth = depth;
        xSpecified = true;
        ySpecified = true;
        depthSpecified = true;
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
                    image.setX(position.x);
                }
                if (ySpecified) {
                    image.setY(position.y);
                }
                if (depthSpecified) {
                    image.setDepth(manager, depth);
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
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        int depth = (depthString != null) ? Integer.parseInt(depthString) : 0;
        ImagePositionCommand command = new ImagePositionCommand(instance, group, new Vector2(x, y), depth);
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
