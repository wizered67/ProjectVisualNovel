package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;

/**
 * ConversationCommand that changes the current scene to a different one.
 * @author Adam Victor
 */
public class SetSceneCommand implements ConversationCommand {
    /** Name of the scene to switch to. */
    private String nextScene;

    public SetSceneCommand(String sceneName) {
        nextScene = sceneName;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        conversationController.setScene(nextScene);
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

    public static SetSceneCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("name");
        return new SetSceneCommand(name);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
