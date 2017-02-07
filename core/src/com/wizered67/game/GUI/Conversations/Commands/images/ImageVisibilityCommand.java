package com.wizered67.game.GUI.Conversations.Commands.images;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.scene.Fade;
import com.wizered67.game.GUI.Conversations.scene.SceneImage;
import com.wizered67.game.GUI.Conversations.scene.SceneManager;

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
    /** The name of the interpolation to use for fading in and out. */
    private String interpolation = "linear";

    ImageVisibilityCommand() {}

    ImageVisibilityCommand(String instance, String group, boolean visible, float time, String fadeType, boolean w) {
        instanceIdentifier = instance;
        groupIdentifier = group;
        show = visible;
        fadeTime = time;
        interpolation = fadeType;
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
                if (show) {
                    image.addToScene(manager);
                }
                if (fadeTime == 0) {
                    image.finishVisibility(show);
                    done = true;
                } else {
                    int direction = show ? 1 : -1;
                    image.setFade(new Fade(interpolation, image.getSprite().getColor().a, fadeTime, direction));
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
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.FADE_END) {
            Object[] data = (Object[]) c.data;
            SceneManager manager = (SceneManager) data[0];
            Object entity = data[1];
            if (instanceIdentifier != null && !instanceIdentifier.isEmpty()) {
                if (manager.getImage(instanceIdentifier).equals(entity)) {
                    done = true;
                }
            } else if (groupIdentifier != null && !groupIdentifier.isEmpty()) {
                if (manager.getImagesByGroup(groupIdentifier).contains(entity)) {
                    done = true;
                }
            }
        }
    }

    static ImageVisibilityCommand makeCommand(String instance, String group, XmlReader.Element element) {
        boolean show = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fadeTime", 0);
        String fadeType = element.getAttribute("fadeType", "linear");
        boolean wait = element.getBooleanAttribute("wait", true);
        return new ImageVisibilityCommand(instance, group, show, fade, fadeType, wait);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
