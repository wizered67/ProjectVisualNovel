package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.graphics.Color;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

/** ConversationCommand used to apply fading in back to the scene. Originally a private inner class of ScreenFadeCommand
 * but changed for serialization purposes.
 * @author Adam Victor
 */
class ScreenFadeInCommand implements ConversationCommand {
    /** Whether the fade in is done. */
    private boolean isDone;
    /** The name of the type of interpolation to use for fading in. */
    private String enterType;
    /** How long to spend in seconds fading in. */
    private float enterTime;
    /** The color to use for fading in. */
    private Color color;
    /** Whether to wait for the fade to complete before doing other commands. */
    private boolean wait;

    ScreenFadeInCommand() {

    }

    ScreenFadeInCommand(boolean wait, String enterType, float enterTime, Color color) {
        this.wait = wait;
        this.enterTime = enterTime;
        this.enterType = enterType;
        this.color = color;
    }
    @Override
    public void execute(ConversationController conversationController) {
        isDone = !wait;
        conversationController.currentSceneManager().setFade(new FloatInterpolation(enterType, 1, 0, enterTime), color);
    }

    @Override
    public boolean waitToProceed() {
        return !isDone;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.FADE) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager == entity) {
                    isDone = true;
                }
            }
        }
    }
}