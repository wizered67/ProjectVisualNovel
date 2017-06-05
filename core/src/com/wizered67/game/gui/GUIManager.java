package com.wizered67.game.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.wizered67.game.Constants;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.inputs.Controllable;
import com.wizered67.game.inputs.Controls;
import com.wizered67.game.saving.SaveManager;

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

    /** Main Table that all GUI elements are added to. */
	private Table dialogueElementsTable;
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

    //debug related variables
    /** Scrollpane used to contain debug choices -- ie loading conversations, etc. */
    private ScrollPane debugPane;
    /** Contains choices for debug options. */
    private HorizontalGroup debugChoices;
    /** Whether the save input is showing. */
    private boolean saveInputShowing = false;
    /** GUI List that shows all options for debug menu. */
    private List<String> debugSelector;
    /** Table used to contain elements for the transcript. */
    private Table transcriptTable;
    /** Scrollpane used to contain the transcript label. */
    private ScrollPane transcriptPane;
    /** Label used for displaying transcript text. */
    private Label transcriptLabel;


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
 		/*
        skin.add("white", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        Drawable newDrawable = skin.newDrawable("white", Color.DARK_GRAY);
        newDrawable.setLeftWidth(TEXTBOX_LEFT_PADDING);
        newDrawable.setRightWidth(TEXTBOX_LEFT_PADDING);
        newDrawable.setTopHeight(TEXTBOX_LEFT_PADDING / 2);
        //newDrawable.setRightWidth(20);
        labelStyle.background = newDrawable;
        skin.add("default", labelStyle);

        Label.LabelStyle speakerLabelStyle = new Label.LabelStyle();
        speakerLabelStyle.font = skin.getFont("default");
        Drawable speakerDrawable = skin.newDrawable("white", Color.GRAY);
        speakerDrawable.setLeftWidth(5);
        //speakerDrawable.setRightWidth(5);
        //newDrawable.setRightWidth(20);
        speakerLabelStyle.background = speakerDrawable;
        skin.add("speakerStyle", speakerLabelStyle);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.cursor = textButtonStyle.over;
        textFieldStyle.selection = textButtonStyle.over;
        textFieldStyle.background = speakerDrawable;
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.BLACK;
        skin.add("default", textFieldStyle);
        */

        //todo init and add dialogue elements UI
        DialogueElementsUI dialogueElementsUI = new DialogueElementsUI(this, skin);
        addUIComponent(dialogueElementsUI);
        textboxLabel = dialogueElementsUI.getTextboxLabel();
        speakerLabel = dialogueElementsUI.getSpeakerLabel();
        choiceButtons = dialogueElementsUI.getChoiceButtons();
        dialogueElementsTable = dialogueElementsUI.getMainActor();
        conversationController = new ConversationController(textboxLabel, speakerLabel, choiceButtons);
        setTextboxShowing(false);

        TextInputUI textInputUI = new TextInputUI(this, skin);
        addUIComponent(textInputUI);

        TranscriptUI transcriptUI = new TranscriptUI(this, skin, conversationController.getTranscript());
        addUIComponent(transcriptUI);

        addDebug();
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

        if (Constants.DEBUG && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !isComponentVisible(TextInputUI.ID)) {
            toggleDebugDisplay();
            conversationController.setPaused(debugChoices.isVisible());
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
	    /*
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //float densityIndependentSize = Constants.REGULAR_FONT_SIZE * Gdx.graphics.getDensity();
        parameter.size = (int) (Constants.REGULAR_FONT_SIZE * Gdx.graphics.getHeight() / Constants.VIRTUAL_HEIGHT);//Math.round(densityIndependentSize);
        defaultFont = generator.generateFont(parameter); // font size 12 pixels
        defaultFont.getData().markupEnabled = true;
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        skin.add("default", defaultFont);
        */
	    for (UIComponent uiComponent : sortedUIComponents) {
	        uiComponent.resize(width, height);
        }
        //dialogueElementsUI.resize(width, height);
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

    public boolean isTranscriptVisible() {
        return transcriptTable.isVisible();
    }

    private void addDebug() {
        //List.ListStyle listStyle = new List.ListStyle(skin.getFont("default-font"), Color.GRAY, Color.WHITE, skin.newDrawable("white", Color.LIGHT_GRAY));
        //listStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        debugSelector = new List<>(skin);
        //debugSelector.setItems("demonstration", "test conversation");
        //debugSelector.setSelectedIndex(-1);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        debugPane = new ScrollPane(debugSelector, scrollPaneStyle);
        debugPane.setWidth(Gdx.graphics.getWidth() / 3);
        debugPane.setHeight(Gdx.graphics.getHeight() / 2);
        debugPane.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 64, Align.center);
        debugPane.toFront();
        debugPane.setUserObject(DebugMode.CONV);
        stage.addActor(debugPane);
        debugPane.setVisible(false);
        debugChoices = new HorizontalGroup();
        debugChoices.space(15);
        TextButton convChange = new TextButton("Conversations", skin);
        convChange.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugConversations();
                event.setCapture(true);
                event.cancel();
            }
        });
        TextButton branchChange = new TextButton("Branches", skin);
        branchChange.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugBranches();
                event.setCapture(true);
                event.cancel();
            }
        });
        TextButton save = new TextButton("Save", skin);
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.setCapture(true);
                event.cancel();
                Input.TextInputListener inputListener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        SaveManager.save(Gdx.files.local("Saves/" + text));
                        if (debugPane.getUserObject() == DebugMode.LOAD) {
                            debugLoad();
                        }
                        saveInputShowing = false;
                    }

                    @Override
                    public void canceled() {
                        saveInputShowing = false;
                    }
                };
                if (!saveInputShowing) {
                    Gdx.input.getTextInput(inputListener, "Save File", "new save", "");
                    saveInputShowing = true;
                }
            }
        });
        TextButton load = new TextButton("Load", skin);
        load.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugLoad();
                event.setCapture(true);
                event.cancel();
            }
        });
        TextButton confirm = new TextButton("Confirm", skin);
        confirm.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                debugConfirmChoice();
                event.setCapture(true);
                event.cancel();
            }
        });
        debugChoices.addActor(convChange);
        debugChoices.addActor(branchChange);
        debugChoices.addActor(confirm);
        debugChoices.addActor(save);
        debugChoices.addActor(load);
        debugChoices.setPosition(Gdx.graphics.getWidth() / 2 - debugChoices.getMinWidth() / 2, Gdx.graphics.getHeight() - 64, Align.center);
        debugChoices.toFront();
        debugChoices.setVisible(false);
        stage.addActor(debugChoices);
    }

    public void toggleDebugDisplay() {
        debugChoices.setVisible(!debugChoices.isVisible());
        if (!debugChoices.isVisible()) {
            debugPane.setVisible(false);
        }
    }

    private void debugChangePaneType(Array<String> content, DebugMode type) {
        debugSelector.setItems(content);
        debugSelector.setSelectedIndex(-1);
        debugPane.setUserObject(type);
        debugPane.setVisible(true);
    }

    private void debugConversations() {
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("Conversations/").list();
        for(FileHandle file: files) {
            fileNames.add(file.name());
        }
        debugChangePaneType(fileNames, DebugMode.CONV);
    }

    private void debugBranches() {
        Conversation currentConv = conversationController.conversation();
        Array<String> branches = new Array<>();
        for (String branch : currentConv.getAllBranches()) {
            branches.add(branch);
        }
        debugChangePaneType(branches, DebugMode.BRANCH);
    }

    private void debugLoad() {
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("Saves/").list();
        for(FileHandle file: files) {
            fileNames.add(file.name());
        }
        debugChangePaneType(fileNames, DebugMode.LOAD);
    }

    private void debugConfirmChoice() {
        int index = debugSelector.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String selection = debugSelector.getItems().get(debugSelector.getSelectedIndex());
        switch ((DebugMode) debugPane.getUserObject()) {
            case CONV:
                conversationController.exit();
                conversationController.loadConversation(selection);
                //conversationController.setBranch("default");
                //conversationController.nextCommand();
                break;
            case BRANCH:
                conversationController.setBranch(selection);
                conversationController.nextCommand();
                break;
            case LOAD:
                SaveManager.load(Gdx.files.internal("Saves/" + selection));
                break;
        }
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

    private enum DebugMode {
        CONV, BRANCH, LOAD
    }
}
