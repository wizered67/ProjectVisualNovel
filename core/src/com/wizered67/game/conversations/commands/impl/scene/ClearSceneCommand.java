package com.wizered67.game.conversations.commands.impl.scene;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

/**
 * ConversationCommand that clear SceneImages and/or SceneCharacters from the scene.
 * @author Adam Victor
 */
public class ClearSceneCommand implements ConversationCommand {
    /** Whether all images in the scene should be cleared. */
    private boolean clearImages;
    /** Whether all characters in the scene should be cleared. */
    private boolean clearCharacters;

    public ClearSceneCommand() {}
    public ClearSceneCommand(boolean clearImages, boolean clearCharacters) {
        this.clearImages = clearImages;
        this.clearCharacters = clearCharacters;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        if (clearCharacters) {
            conversationController.currentSceneManager().removeAllCharacters();
        }
        if (clearImages) {
            conversationController.currentSceneManager().removeAllImages();
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
}
