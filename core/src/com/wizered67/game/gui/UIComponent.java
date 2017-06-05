package com.wizered67.game.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wizered67.game.inputs.Controllable;

/**
 * Interface for all UIComponents providing methods all should have.
 */
public interface UIComponent extends Controllable {
    int getPriority();
    boolean isVisible();
    void update(float deltaTime);
    void resize(int newWidth, int newHeight);
    String getId();
    Actor getMainActor();
}
