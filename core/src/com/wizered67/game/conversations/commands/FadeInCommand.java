package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

class FadeInCommand implements ConversationCommand {
    private boolean isDone;
    private String enterType;
    private float enterTime;
    private Color color;
    private boolean wait;

    FadeInCommand() {

    }

    FadeInCommand(boolean wait, String enterType, float enterTime, Color color) {
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

    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}