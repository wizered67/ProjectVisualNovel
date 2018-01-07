package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.graphics.Color;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

import java.util.List;

/**
 * ConversationCommand for fading out the screen and then fading back in. Allows setting color, time
 * for each, interpolation type for each, and whether to wait before proceeding. Once the fade out is complete,
 * commands nested in the fade element are executed. Once those are done, the fade in begins. This allows, for example,
 * removing all characters while the screen is fully colored so that the player does not see the characters dissapear.
 * @author Adam Victor
 */
public class ScreenFadeCommand implements ConversationCommand {
    private Color color;
    private float exitTime;
    private String exitType;
    private float enterTime;
    private String enterType;
    private boolean wait;
    private boolean done;
    private List<ConversationCommand> commands;

    public ScreenFadeCommand() {

    }

    public ScreenFadeCommand(List<ConversationCommand> commands, Color color, float exitTime, String exitType, float enterTime, String enterType, boolean wait) {
        this.commands = commands;
        this.color = color.cpy();
        this.exitTime = exitTime;
        this.exitType = exitType;
        this.enterTime = enterTime;
        this.enterType = enterType;
        this.wait = wait;
        done = !wait;
        this.commands.add(new ScreenFadeInCommand(wait, enterType, enterTime, this.color));
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        done = !wait;
        conversationController.currentSceneManager().setFade(new FloatInterpolation(exitType, 0, 1, exitTime), color);
    }

    private void addEnterCommands(ConversationController conversationController) {
        conversationController.insertCommands(commands);
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
            if (data[2] == CompleteEvent.InterpolationEventType.FADE) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                if (manager == entity) {
                    done = true;
                    addEnterCommands(manager.conversationController());
                }
            }
        }
    }
}
