package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.scene.SceneImage;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;

/**
 * Created by Adam on 1/30/2017.
 */
class ImageGroupChangeCommand implements ConversationCommand {
    private String instanceIdentifier;
    private String groupIdentifier;
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
        final SceneManager manager = conversationController.sceneManager();
        manager.applyImageCommand(instanceIdentifier, groupIdentifier, new ImageAction() {
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
    static ImageGroupChangeCommand makeCommand(String instanceIdentifier, String groupIdentifier, XmlReader.Element element) {
        XmlReader.Element text = element.getChild(0);
        if (!text.getName().equals("text")) {
            throw new IllegalArgumentException("Group change element must have text");
        }
        String newGroup = text.getText();
        return new ImageGroupChangeCommand(instanceIdentifier, groupIdentifier, newGroup);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
