package com.wizered67.game.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.wizered67.game.Constants;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.Transcript;
import com.wizered67.game.inputs.Controllable;
import com.wizered67.game.inputs.Controls;
import com.wizered67.game.saving.SaveManager;

/** Contains GUI elements and the ConversationController which the GUI elements are passed into.
 * Fixes GUI elements if screen is resized.
 * @author Adam Victor
 */
public class GUIManager implements Controllable {

    private DialogueElementsUI dialogueElementsUI;


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
    private final int LEFT_PADDING = 10;
    /** Message Window that updates the GUI elements as a Conversation proceeds. */
    private ConversationController conversationController;
    /** Default font used for text. */
    private BitmapFont defaultFont;

    private TextInputUI textInputUI;

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
    /** Whether the transcript is scrolling, and in which direction. */
    private float transcriptScrolling = 0;

    /** Initializes all of the GUI elements and adds them to the Stage ST. Also
     * initializes ConversationController with the elements it will update.
     */
    public GUIManager(Stage st) {
		stage = st;
 		// Generate a 1x1 white texture and store it in the skin named "white".

 		skin = new Skin(new TextureAtlas(Gdx.files.internal("Skins/uiskin.atlas")));
        initFont();
        skin.load(Gdx.files.internal("Skins/uiskin.json"));
        Drawable dialogueDrawable = skin.getDrawable("dialogue-drawable");
        dialogueDrawable.setLeftWidth(LEFT_PADDING);
        dialogueDrawable.setRightWidth(LEFT_PADDING);
        dialogueDrawable.setTopHeight(LEFT_PADDING);
        dialogueDrawable.setBottomHeight(LEFT_PADDING);
 		/*
        skin.add("white", new Texture(pixmap));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        Drawable newDrawable = skin.newDrawable("white", Color.DARK_GRAY);
        newDrawable.setLeftWidth(LEFT_PADDING);
        newDrawable.setRightWidth(LEFT_PADDING);
        newDrawable.setTopHeight(LEFT_PADDING / 2);
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
        dialogueElementsUI = new DialogueElementsUI(this, skin);
        stage.addActor(dialogueElementsUI.getMainTable());
        textboxLabel = dialogueElementsUI.getTextboxLabel();
        speakerLabel = dialogueElementsUI.getSpeakerLabel();
        choiceButtons = dialogueElementsUI.getChoiceButtons();
        dialogueElementsTable = dialogueElementsUI.getMainTable();
        conversationController = new ConversationController(textboxLabel, speakerLabel, choiceButtons);
        setTextboxShowing(false);

        textInputUI = new TextInputUI(this, skin);
        stage.addActor(textInputUI.getMainTable());

        addDebug();
        addTranscript();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT) && !textInputUI.isVisible()) {
            toggleTranscript();
        }
        if (Constants.DEBUG && Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !textInputUI.isVisible()) {
            toggleDebugDisplay();
            conversationController.setPaused(debugChoices.isVisible());
        }
        if (transcriptPane.isVisible()) {
            if (transcriptScrolling != 0) {
                float velocity = transcriptPane.getVelocityY();
                transcriptPane.fling(1, 0, velocity);
                transcriptPane.setVelocityY(velocity + 4 * Math.signum(velocity));
            }
            //transcriptPane.setScrollY(transcriptPane.getScrollY() + transcriptScrolling);
        }
        updateTranscript(); //todo remove, only do so when transcript is visible
	}
	/** Toggles whether the transcript is being shown. */
	public void toggleTranscript() {
        transcriptTable.setVisible(!transcriptTable.isVisible());
        conversationController.setPaused(transcriptTable.isVisible());
        updateTranscript(); //todo fix. part of hacky solution to make update first time
        //transcriptPane.invalidate();
        transcriptPane.validate();
        transcriptPane.setScrollPercentY(1f);
        transcriptPane.updateVisualScroll();
        transcriptPane.setVelocityY(0);
        stage.setScrollFocus(transcriptPane);
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
        dialogueElementsUI.resize(width, height);
	}

    /** Sets the visibility of the textbox and speaker label to SHOW. */
    public void setTextboxShowing(boolean show) {
        conversationController.setTextBoxShowing(show);
    }
    /** Returns the ConversationController. */
    public ConversationController conversationController() {
        return conversationController;
    }

    public TextInputUI getTextInputUI() {
        return textInputUI;
    }

    private void addTranscript() {
        transcriptTable = new Table();
        transcriptTable.setDebug(Constants.DEBUG);
        transcriptTable.setFillParent(true);
        transcriptTable.setVisible(false);
        transcriptTable.toFront();

        transcriptLabel = new Label("", skin);
        transcriptLabel.setWrap(true);
        transcriptLabel.setAlignment(Align.topLeft);
        transcriptLabel.setWidth(Gdx.graphics.getWidth() - 64);
       // ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        transcriptPane = new ScrollPane(transcriptLabel, skin);
        transcriptPane.setOverscroll(false, false);
        transcriptPane.setWidth(transcriptLabel.getWidth());
        transcriptPane.setHeight(Gdx.graphics.getHeight() - 64);
        transcriptPane.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        transcriptPane.toFront();
        //transcriptPane.setVisible(false);
        stage.addActor(transcriptTable);
        transcriptTable.add(transcriptPane).expand().fill().pad(40);
    }

    private void updateTranscript() {
        if (!transcriptTable.isVisible()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Transcript.TranscriptMessage message : conversationController.getTranscript().getTranscriptMessages()) {
            stringBuilder.append("[CYAN]").append(message.getSpeaker()).append(": [WHITE]\n");
            stringBuilder.append(message.getMessage()).append("\n\n");
        }
        transcriptLabel.setText(stringBuilder);
        transcriptLabel.invalidate();
        transcriptLabel.getPrefHeight(); //fixme part of hacky solution to get size correct for first time
    }

    public boolean isTranscriptVisible() {
        return transcriptTable.isVisible();
    }

    public void scrollTranscript(int direction) {
        transcriptPane.fling(1, 0, -direction * 50);
        transcriptScrolling = direction * 0.005f * transcriptPane.getHeight();
        System.out.println("Velocity set to " + transcriptPane.getVelocityY());
    }

    public void stopTranscriptScrolling() {
        transcriptScrolling = 0;
        transcriptPane.setVelocityY(0);
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
        return false;
    }

    /**
     * A touch up event at position SCREENX, SCREENY involving pointer POINTER, and
     * mouse button BUTTON.
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * A key down involving key KEY mapped to ControlType CONTROL.
     */
    @Override
    public boolean keyDown(Controls.ControlType control, int key) {
        return false;
    }

    /**
     * A key up involving key KEY mapped to ControlType CONTROL.
     */
    @Override
    public boolean keyUp(Controls.ControlType control, int key) {
        return false;
    }

    private enum DebugMode {
        CONV, BRANCH, LOAD
    }
}
