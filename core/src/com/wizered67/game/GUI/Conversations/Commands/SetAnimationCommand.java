package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;

/**
 * Created by Adam on 10/28/2016.
 */
public class SetAnimationCommand implements ConversationCommand {

    private boolean done;
    private String animation;
    private boolean wait;

    public SetAnimationCommand(String anim, boolean w) {
        wait = w;
        done = !wait;
        animation = anim;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        done = !wait;
        if (!messageWindow.animationManager().setAnimation(animation)) {
            done = true;
        }
    }

    @Override
    public boolean waitToProceed() {
        return !done;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.ANIMATION_END) {
            if (c.data.equals(animation)) {
                done = true;
            }

        }
    }
}
