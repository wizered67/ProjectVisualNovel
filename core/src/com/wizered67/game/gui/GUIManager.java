package com.wizered67.game.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.wizered67.game.GameManager;
import com.wizered67.game.inputs.Controllable;
import com.wizered67.game.inputs.Controls;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

/** Contains GUI elements and the ConversationController which the GUI elements are passed into.
 * Fixes GUI elements if screen is resized.
 * @author Adam Victor
 */
public class GUIManager implements Controllable {

    private HashMap<String, UIComponent> idToUIComponent;
    private TreeSet<UIComponent> sortedUIComponents;
    /** The stage to which the GUI elements are added. Part of Scene2D. */
	private Stage stage;
	private Skin skin;


    /** Initializes all of the GUI elements and adds them to the Stage ST. Also
     * initializes ConversationController with the elements it will update.
     */
    public GUIManager(Stage st, Skin skin) {
		stage = st;
        this.skin = skin;
		idToUIComponent = new HashMap<>();
		sortedUIComponents = new TreeSet<>(new Comparator<UIComponent>() {
            @Override
            public int compare(UIComponent o1, UIComponent o2) {
                int p1 = o1.getPriority();
                int p2 = o2.getPriority();
                //negative for reverse order. Highest should be first.
                return -((p1 < p2) ? -1 : ((p1 == p2) ? 0 : 1));
            }
        });
	}
    public GUIManager() {

    }

    public Skin getSkin() {
        return skin;
    }

    /** Returns the Stage used to display GUI elements. */
	public Stage getStage(){
		return stage;
	}
    /** Called every frame. Updates the ConversationController. DELTA TIME is the time
     * elapsed since the last frame.
     */
	public void update(float deltaTime){
        //conversationController.update(deltaTime);
        for (UIComponent uiComponent : sortedUIComponents) {
            uiComponent.update(deltaTime);
        }
	}

	/** Called every frame. Updates and draws the stage, needed for UI elements. DELTA TIME is
     * the time elapsed since the last frame. */
	public void updateAndRenderStage(float deltaTime) {
	    update(deltaTime);
        GameManager.guiViewport().apply(true);
        stage.act(Math.min(1 / 30f, deltaTime));
        stage.draw();
    }
    /** Resize all GUI elements when the screen is resized to dimensions
     * WIDTH by HEIGHT. Keeps GUI elements proportional to virtual size.
     */
	public void resize(int width, int height) {
	    for (UIComponent uiComponent : sortedUIComponents) {
	        uiComponent.resize(width, height);
        }
	}

	public void addUIComponent(UIComponent uiComponent) {
	    sortedUIComponents.add(uiComponent);
	    idToUIComponent.put(uiComponent.getId(), uiComponent);
	    Actor mainActor = uiComponent.getMainActor();
	    if (mainActor != null) {
	        stage.addActor(mainActor);
        }
    }

    public UIComponent getUIComponent(String id) {
	    return idToUIComponent.get(id);
    }

    public boolean isComponentVisible(String id) {
	    UIComponent uiComponent = getUIComponent(id);
	    return uiComponent != null && uiComponent.isVisible();
    }

    /**
     * A touch down event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.touchDown(screenX, screenY, pointer, button);
            if (handled) return true;
        }
        return false;
    }

    /**
     * A touch up event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON.
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.touchUp(screenX, screenY, pointer, button);
            if (handled) return true;
        }
        return false;
    }

    /**
     * A key down involving key KEY mapped to ControlType CONTROL.
     */
    @Override
    public boolean keyDown(Controls.ControlType control, int key) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.keyDown(control, key);
            if (handled) return true;
        }
        return false;
    }

    /**
     * A key up involving key KEY mapped to ControlType CONTROL.
     */
    @Override
    public boolean keyUp(Controls.ControlType control, int key) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.keyUp(control, key);
            if (handled) return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.mouseMoved(screenX, screenY);
            if (handled) return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.scrolled(amount);
            if (handled) return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.touchDragged(screenX, screenY, pointer);
            if (handled) return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (UIComponent uiComponent : sortedUIComponents) {
            boolean handled = uiComponent.keyTyped(character);
            if (handled) return true;
        }
        return false;
    }
}
