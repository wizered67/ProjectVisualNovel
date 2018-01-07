package com.wizered67.game.conversations.commands.impl.scene.images;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.scene.EntityAction;
import com.wizered67.game.conversations.scene.SceneImage;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

/**
 * Command that changes the visibility of an image or group of images. Can instantly change
 * or fade in/out using interpolation.
 * @author Adam Victor
 */
public class ImageVisibilityCommand implements ConversationCommand {
    /** The instance identifier of the image to change the visibility of.
     * If empty, the groupIdentifier is used to apply it to a group. */
    private String instanceIdentifier;
    /** The identifier of the group of images to apply this command to. */
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
        final SceneManager manager = conversationController.currentSceneManager();
        boolean result = manager.applyImageCommand(instanceIdentifier, groupIdentifier, new EntityAction<SceneImage>() {
            @Override
            public void apply(SceneImage image) {
                if (show) {
                    image.addToScene(manager);
                }
                if (fadeTime == 0) {
                    image.finishVisibility(show);
                    done = true;
                } else {
                    int end = show ? 1 : 0;
                    image.setFade(new FloatInterpolation(interpolation, image.getSprite().getColor().a, end, fadeTime));
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
        //if it is a fade end event, extract data about event to see if the ended Entity
        //matches the instance associated with the identifier of this command. If applied
        //to a group, this is completed as soon as the first one is done.
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.FADE) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (instanceIdentifier != null && !instanceIdentifier.isEmpty()) {
                    SceneImage image = manager.getImage(instanceIdentifier);
                    if (image != null && image.equals(entity)) {
                        done = true;
                    }
                } else if (groupIdentifier != null && !groupIdentifier.isEmpty()) {
                    if (manager.getImagesByGroup(groupIdentifier).contains(entity)) {
                        done = true;
                    }
                }
            }
        }
    }

    public static ImageVisibilityCommand makeCommand(String instance, String group, XmlReader.Element element) {
        boolean show = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fadeTime", 0);
        String fadeType = element.getAttribute("fadeType", "linear");
        boolean wait = element.getBooleanAttribute("wait", true);
        return new ImageVisibilityCommand(instance, group, show, fade, fadeType, wait);
    }
}
