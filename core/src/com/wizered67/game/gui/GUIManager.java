package com.wizered67.game.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.wizered67.game.Constants;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.ConversationController;
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

    /** Skin used by all GUI elements. */
	private Skin skin;
    /** Label for the main textbox. Displays text when spoken by characters. */
	private TypingLabel textboxLabel;
    /** Label to display the name of the current speaker. */
    private Label speakerLabel;
    /** Array containing TextButtons to be displayed when the player is offered a choice. */
	private TextButton[] choiceButtons;
    /** The stage to which the GUI elements are added. Part of Scene2D. */
	private Stage stage;
    /** Constant denoting space between left side of the textbox and text. */
    private final int TEXTBOX_LEFT_PADDING = 10;
    /** Message Window that updates the GUI elements as a Conversation proceeds. */
    private ConversationController conversationController;
    /** Default font used for text. */
    private BitmapFont defaultFont;


    /** Initializes all of the GUI elements and adds them to the Stage ST. Also
     * initializes ConversationController with the elements it will update.
     */
    public GUIManager(Stage st) {
		stage = st;

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

 		skin = new Skin(new TextureAtlas(Gdx.files.internal("Skins/uiskin.atlas")));
 		// A bit of a workaround here. We want to use the generated font but also need to define a font in uiskin for testing.
        // Make sure to remove the font from uiskin once the skin is done. This will override the skin to add a font.
        initFont();
        skin.load(Gdx.files.internal("Skins/uiskin.json"));
        Drawable dialogueDrawable = skin.getDrawable("dialogue-drawable-offset");
        dialogueDrawable.setLeftWidth(TEXTBOX_LEFT_PADDING);
        dialogueDrawable.setRightWidth(TEXTBOX_LEFT_PADDING);
        dialogueDrawable.setTopHeight(TEXTBOX_LEFT_PADDING / 2);
        dialogueDrawable.setBottomHeight(TEXTBOX_LEFT_PADDING / 2);

        DialogueElementsUI dialogueElementsUI = new DialogueElementsUI(this, skin);
        addUIComponent(dialogueElementsUI);
        textboxLabel = dialogueElementsUI.getTextboxLabel();
        speakerLabel = dialogueElementsUI.getSpeakerLabel();
        choiceButtons = dialogueElementsUI.getChoiceButtons();
        conversationController = new ConversationController(textboxLabel, speakerLabel, choiceButtons);
        setTextboxShowing(false);

        TextInputUI textInputUI = new TextInputUI(this, skin);
        addUIComponent(textInputUI);

        TranscriptUI transcriptUI = new TranscriptUI(this, skin, conversationController.getTranscript());
        addUIComponent(transcriptUI);

        DebugMenuUI debugMenuUI = new DebugMenuUI(this, skin);
        addUIComponent(debugMenuUI);
	}
    public GUIManager() {
        conversationController = new ConversationController();
    }

    /** Initializes the font that will be used. */
    private void initFont() {
        // Store the default libgdx font under the name "default".
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //float densityIndependentSize = Constants.REGULAR_FONT_SIZE * Gdx.graphics.getDensity();
        parameter.size = Constants.REGULAR_FONT_SIZE; //(int) (Constants.REGULAR_FONT_SIZE * Gdx.graphics.getHeight() / Constants.DEFAULT_HEIGHT);//Math.round(densityIndependentSize);
        defaultFont = generator.generateFont(parameter); // font size 12 pixels
        defaultFont.getData().markupEnabled = true;
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        skin.add("default-font", defaultFont);
    }

    /** Returns the Stage used to display GUI elements. */
	public Stage getStage(){
		return stage;
	}
    /** Called every frame. Updates the ConversationController. DELTA TIME is the time
     * elapsed since the last frame.
     */
	public void update(float deltaTime){
        conversationController.update(deltaTime);
        for (UIComponent uiComponent : sortedUIComponents) {
            uiComponent.update(deltaTime);
        }
	}

	/** Called every frame. Updates and draws the stage, needed for UI elements. DELTA TIME is
     * the time elapsed since the last frame. */
	public void updateAndRenderStage(float deltaTime) {
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

    /** Sets the visibility of the textbox and speaker label to SHOW. */
    public void setTextboxShowing(boolean show) {
        conversationController.setTextBoxShowing(show);
    }
    /** Returns the ConversationController. */
    public ConversationController conversationController() {
        return conversationController;
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
