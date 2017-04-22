package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;

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

    public static ClearSceneCommand makeCommand(XmlReader.Element element) {
        boolean clearImages = element.getBooleanAttribute("clearImages", false);
        boolean clearCharacters = element.getBooleanAttribute("clearCharacters", false);
        return new ClearSceneCommand(clearImages, clearCharacters);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
