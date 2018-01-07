package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.ScreenShakeEffect;

/**
 * ConversationCommand that initiates a screen shake of a specified length and magnitude.
 * @author Adam Victor
 */
public class ScreenShakeCommand implements ConversationCommand {
    /** The type of interpolation to be used for dampening the screen shake. */
    private String interpolation;
    /** The magnitude of the screenshake (the screen can shake at most between -magnitude and magnitude pixels). */
    private Vector2 magnitude;
    /** How long the screenshake will last. */
    private float duration;
    /** Whether to wait for the screenshake to be completed before starting the next command. */
    private boolean wait;
    /** Whether the screen shake has finished. */
    private boolean done;

    public ScreenShakeCommand() {}

    public ScreenShakeCommand(String interpolation, Vector2 magnitude, float duration, boolean wait) {
        this.interpolation = interpolation;
        this.magnitude = magnitude;
        this.duration = duration;
        this.wait = wait;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        done = !wait;
        conversationController.currentSceneManager().setScreenShakeEffect(new ScreenShakeEffect(interpolation, magnitude, duration));
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
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.SHAKE) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager == entity) {
                    done = true;
                }
            }
        }
    }
}
