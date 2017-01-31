package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.SceneImage;
import com.wizered67.game.GUI.Conversations.SceneManager;

/**
 * Created by Adam on 1/27/2017.
 */
class ImageVisibilityCommand implements ConversationCommand {
    private String instanceIdentifier;
    private String groupIdentifier;
    /** Whether the SceneImage should be visible. */
    private boolean show;
    /** How long to fade in or out the SceneImage. */
    private float fadeTime;
    /** Whether to wait for the fade to complete before going on to the next command. */
    private boolean wait;
    /** Whether the command is completed and the next one can be executed. */
    private boolean done;

    ImageVisibilityCommand(String instance, String group, boolean visible, float time, boolean w) {
        instanceIdentifier = instance;
        groupIdentifier = group;
        show = visible;
        fadeTime = time;
        wait = w;
        done = !wait;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        done = !wait;
        final SceneManager manager = conversationController.sceneManager();
        boolean result = manager.applyImageCommand(instanceIdentifier, groupIdentifier, new ImageAction() {
            @Override
            public void apply(SceneImage image) {
                if (fadeTime == 0) {
                    image.setFullVisible(show);
                    done = true;
                } else {
                    float factor = show ? 1f : -1f;
                    float fadePerSecond = factor / fadeTime;
                    image.setFade(fadePerSecond);
                }
                if (show) {
                    image.addToScene(manager);
                }
            }
        });
        if (!result) { //applying failed because there is nothing of that name
            done = true;
        }
    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return !done;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) { //todo add data for fade to make sure this is the correct thing that has finished fading
        if (c.type == CompleteEvent.Type.FADE_END) {
            done = true;
        }
    }

    static ImageVisibilityCommand makeCommand(String instance, String group, XmlReader.Element element) {
        boolean show = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fadeTime", 0);
        boolean wait = element.getBooleanAttribute("wait", true);
        return new ImageVisibilityCommand(instance, group, show, fade, wait);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
