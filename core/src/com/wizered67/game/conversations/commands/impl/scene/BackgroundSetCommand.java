package com.wizered67.game.conversations.commands.impl.scene;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneImage;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * Command to set the scene's background image.
 * @author Adam Victor
 */
public class BackgroundSetCommand implements ConversationCommand {
    private static final String BACKGROUND_IDENTIFIER = "bg";
    /** Identifier of the Texture to set as the background. */
    private String imageIdentifier;
    /** X location to show background at. */
    private float xPosition;
    /** Y location to show background at. */
    private float yPosition;

    /** Creates a BackgroundSetCommand that sets background to the texture with identifier ID. */
    public BackgroundSetCommand(String id, float xPos, float yPos) {
        imageIdentifier = id;
        xPosition = xPos;
        yPosition = yPos;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        SceneManager manager = conversationController.currentSceneManager();
        SceneImage bgImage = manager.getImage(BACKGROUND_IDENTIFIER);
        if (bgImage == null) {
            bgImage = new SceneImage(BACKGROUND_IDENTIFIER);
            manager.addImage(bgImage);
            bgImage.addToScene(manager);
        }
        bgImage.setPosition(xPosition, yPosition);
        bgImage.setDepth(manager, -99999);
        bgImage.finishVisibility(true);
        bgImage.setCurrentAnimation(imageIdentifier);
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
}
