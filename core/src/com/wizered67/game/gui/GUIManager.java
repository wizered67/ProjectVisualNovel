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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
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
import com.wizered67.game.CustomTypingLabel;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.Transcript;
import com.wizered67.game.saving.SaveManager;

/** Contains GUI elements and the ConversationController which the GUI elements are passed into.
 * Fixes GUI elements if screen is resized.
 * @author Adam Victor
 */
public class GUIManager {
    /** Main Table that all GUI elements are added to. */
	private static Table table;
    /** Skin used by all GUI elements. */
	private static Skin skin = new Skin();
    /** Label for the main textbox. Displays text when spoken by characters. */
	private static CustomTypingLabel textboxLabel;
    /** Label to display the name of the current speaker. */
    private static Label speakerLabel;
    /** Array containing TextButtons to be displayed when the player is offered a choice. */
	private static TextButton[] choiceButtons;
    /** The stage to which the GUI elements are added. Part of Scene2D. */
	private static Stage stage;
    /** Constant Vector2 containing textbox dimensions. */
	private final static Vector2 TEXTBOX_SIZE = new Vector2(360, 50);
    /** Constant Vector2 containing choice button dimensions. */
    private final static Vector2 CHOICES_SIZE = new Vector2(300, 22);
    /** Constant denoting space between left side of the textbox and text. */
    private final static int LEFT_PADDING = 10;
    /** Message Window that updates the GUI elements as a Conversation proceeds. */
    private static ConversationController conversationController;
    /** Default font used for text. */
    private static BitmapFont defaultFont;

    //debug related variables
    /** Scrollpane used to contain debug choices -- ie loading conversations, etc. */
    private static ScrollPane debugPane;
    /** Contains choices for debug options. */
    private static HorizontalGroup debugChoices;
    /** Whether the save input is showing. */
    private static boolean saveInputShowing = false;
    /** GUI List that shows all options for debug menu. */
    private static List<String> debugSelector;

    private static ScrollPane transcriptPane;
    private static Label transcriptLabel;
    private static float transcriptScrolling = 0;

    /** Initializes all of the GUI elements and adds them to the Stage ST. Also
     * initializes ConversationController with the elements it will update.
     */
    public GUIManager(Stage st) {
		stage = st;
 		// Generate a 1x1 white texture and store it in the skin named "white".
 		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
 		pixmap.setColor(Color.WHITE);
 		pixmap.fill();

 		// Store the default libgdx font under the name "default".
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float densityIndependentSize = Constants.REGULAR_FONT_SIZE * Gdx.graphics.getDensity();
        parameter.size = Math.round(densityIndependentSize);
		defaultFont = generator.generateFont(parameter); // font size 12 pixels
        defaultFont.getData().markupEnabled = true;

        skin.add("white", new Texture(pixmap));
        skin.add("default", defaultFont);
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
    	 table = new Table();
    	 table.setFillParent(true);
    	 stage.addActor(table);
	     table.setDebug(true); // This is optional, but enables debug lines for tables.
    	    // Add widgets to the table here.
	     
	     TextButtonStyle textButtonStyle = new TextButtonStyle();
			textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
			textButtonStyle.down = skin.newDrawable("white", Color.LIGHT_GRAY);
			textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
			textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
			textButtonStyle.font = skin.getFont("default");
			skin.add("default", textButtonStyle);

        choiceButtons = new TextButton[4];
        for (int i = 0; i < choiceButtons.length; i += 1) {
            TextButton tb = new TextButton("", skin);
            tb.setUserObject(i);
            tb.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    System.out.println("Clicked button " + actor.getUserObject());
                    conversationController.processChoice((Integer) actor.getUserObject());
                    event.cancel();
                    ((Button) actor).setProgrammaticChangeEvents(false);
                    ((Button) actor).setChecked(false);
                    ((Button) actor).setProgrammaticChangeEvents(true);
                }
            });
            tb.setVisible(false);
            choiceButtons[i] = tb;
            stage.addActor(tb);
        }

        Label.LabelStyle speakerLabelStyle = new Label.LabelStyle();
        speakerLabelStyle.font = skin.getFont("default");
        Drawable speakerDrawable = skin.newDrawable("white", Color.GRAY);
        speakerDrawable.setLeftWidth(5);
        //speakerDrawable.setRightWidth(5);
        //newDrawable.setRightWidth(20);
        speakerLabelStyle.background = speakerDrawable;
        skin.add("speaker", speakerLabelStyle);
        speakerLabel = new Label("Really long speaker name", skin, "speaker");
        speakerLabel.toBack();
        //speakerLabel.setStyle(speakerLabelStyle);
        speakerLabel.setAlignment(Align.center);
        stage.addActor(speakerLabel);

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = skin.getFont("default");
		Drawable newDrawable = skin.newDrawable("white", Color.DARK_GRAY);
		newDrawable.setLeftWidth(LEFT_PADDING);
        newDrawable.setLeftWidth(LEFT_PADDING);
		//newDrawable.setRightWidth(20);
		labelStyle.background = newDrawable;
		skin.add("default", labelStyle);
		textboxLabel = new CustomTypingLabel("", skin);
		textboxLabel.setAlignment(Align.topLeft);
		textboxLabel.setStyle(labelStyle);
        textboxLabel.setWrap(true);
        textboxLabel.toFront();
        stage.addActor(textboxLabel);

        conversationController = new ConversationController(textboxLabel, speakerLabel, choiceButtons);
        setTextboxShowing(false);
        //System.out.println(remainingTextNoTags);
        //remainingText = "this is a new message just so you know.";
		//textboxLabel.setText("TESTING A MESSAGE BRO");
		//textboxLabel = new Label("this is a really long test message and I want to see if word wrap is doing anything? Test MessageCommand!", labelStyle);

		//textboxLabel.setPosition(200, 400);
		//textboxLabel.setSize(350, 60);
		//textboxLabel.setFullVisible(false);
		/*
        TextTooltip.TextTooltipStyle textTooltipStyle = new TextTooltip.TextTooltipStyle();
		textTooltipStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
		textTooltipStyle.label = labelStyle;
		textTooltipStyle.wrapWidth = 300;
		skin.add("default", textTooltipStyle);
		TextTooltip textTooltip = new TextTooltip("This [GREEN]is [WHITE]a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip! This is a tooltip!", skin);
		//textTooltip.setAlways(true);
		textTooltip.setInstant(true);
		button.addListener(textTooltip);
		*/
		//Table tooltipTable = new Table(skin);
		//tooltipTable.pad(10).background("default-round");
		//tooltipTable.add(new TextButton("Fancy tooltip!", skin));
        addDebug();
        addTranscript();
	}
    public GUIManager() {
        conversationController = new ConversationController();
    }
    /** Returns the Stage used to display GUI elements. */
	public static Stage getStage(){
		return stage;
	}
	/** Returns the batch being used by the stage. */
	public static Batch getBatch() {
	    return stage.getBatch();
    }
    /** Called every frame. Updates the ConversationController. DELTA TIME is the time
     * elapsed since the last frame.
     */
	public static void update(float deltaTime){
        conversationController.update(deltaTime);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            toggleTranscript();
        }
        if (Constants.DEBUG && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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
	public static void toggleTranscript() {
        transcriptPane.setVisible(!transcriptPane.isVisible());
        conversationController.setPaused(transcriptPane.isVisible());
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
	public static void updateAndRenderStage(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }
    /** Resize all GUI elements when the screen is resized to dimensions
     * WIDTH by HEIGHT. Keeps GUI elements proportional to virtual size.
     */
	public static void resize(int width, int height){
		textboxLabel.setSize(TEXTBOX_SIZE.x * width / Constants.VIRTUAL_WIDTH,
				TEXTBOX_SIZE.y * height / Constants.VIRTUAL_HEIGHT);
		textboxLabel.setPosition((width - textboxLabel.getWidth()) / 2,
				height / 16); //actor position is from bottom left of it
        textboxLabel.invalidate();
        //numLines = textboxLabel.getGlyphLayout().runs.size;
        speakerLabel.setPosition(textboxLabel.getX(), textboxLabel.getY() + textboxLabel.getHeight());
        speakerLabel.invalidate();

        resizeChoices(width, height);
	}
    /** Resizes the TextButtons used for choices. Keeps them evenly spaced apart. */
    private static void resizeChoices(int width, int height) {
        for (int i = 0; i < choiceButtons.length; i += 1) {
            TextButton tb = choiceButtons[i];
            tb.setSize(CHOICES_SIZE.x * width / Constants.VIRTUAL_WIDTH, CHOICES_SIZE.y * height / Constants.VIRTUAL_HEIGHT);
            float textBoxTop = textboxLabel.getTop();
            float gap = (height - textBoxTop - choiceButtons.length * tb.getHeight()) / (choiceButtons.length + 1);
            float yPos = textBoxTop + (gap + tb.getHeight() / 2) * ((choiceButtons.length - i - 1) + 1);
            tb.setPosition((width - tb.getWidth()) / 2, yPos);
            tb.invalidate();
        }
    }
    /** Returns the y position of the top of the textbox label. */
    public static float getTextboxTop() {
        return textboxLabel.getTop();
    }
    /** Sets the visibility of the textbox and speaker label to SHOW. */
    public static void setTextboxShowing(boolean show) {
        conversationController.setTextBoxShowing(show);
    }
    /** Returns the ConversationController. */
    public static ConversationController conversationController() {
        return conversationController;
    }

    private static void addTranscript() {
        transcriptLabel = new Label("", skin);
        transcriptLabel.setWrap(true);
        transcriptLabel.setAlignment(Align.topLeft);
        transcriptLabel.setWidth(Gdx.graphics.getWidth() - 64);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        transcriptPane = new ScrollPane(transcriptLabel, scrollPaneStyle);
        transcriptPane.setOverscroll(false, false);
        transcriptPane.setWidth(transcriptLabel.getWidth());
        transcriptPane.setHeight(Gdx.graphics.getHeight() - 64);
        transcriptPane.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        transcriptPane.toFront();
        transcriptPane.setVisible(false);
        stage.addActor(transcriptPane);
    }

    private static void updateTranscript() {
        if (!transcriptPane.isVisible()) {
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

    public static boolean isTranscriptVisible() {
        return transcriptPane.isVisible();
    }

    public static void scrollTranscript(int direction) {
        transcriptPane.fling(1, 0, -direction * 50);
        transcriptScrolling = direction * 0.005f * transcriptPane.getHeight();
        System.out.println("Velocity set to " + transcriptPane.getVelocityY());
    }

    public static void stopTranscriptScrolling() {
        transcriptScrolling = 0;
        transcriptPane.setVelocityY(0);
    }

    private static void addDebug() {
        List.ListStyle listStyle = new List.ListStyle(skin.getFont("default"), Color.GRAY, Color.WHITE, skin.newDrawable("white", Color.LIGHT_GRAY));
        listStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        debugSelector = new List<>(listStyle);
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

    public static void toggleDebugDisplay() {
        debugChoices.setVisible(!debugChoices.isVisible());
        if (!debugChoices.isVisible()) {
            debugPane.setVisible(false);
        }
    }

    private static void debugChangePaneType(Array<String> content, DebugMode type) {
        debugSelector.setItems(content);
        debugSelector.setSelectedIndex(-1);
        debugPane.setUserObject(type);
        debugPane.setVisible(true);
    }

    private static void debugConversations() {
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("Conversations/").list();
        for(FileHandle file: files) {
            fileNames.add(file.name());
        }
        debugChangePaneType(fileNames, DebugMode.CONV);
    }

    private static void debugBranches() {
        Conversation currentConv = conversationController.conversation();
        Array<String> branches = new Array<>();
        for (String branch : currentConv.getAllBranches()) {
            branches.add(branch);
        }
        debugChangePaneType(branches, DebugMode.BRANCH);
    }

    private static void debugLoad() {
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("Saves/").list();
        for(FileHandle file: files) {
            fileNames.add(file.name());
        }
        debugChangePaneType(fileNames, DebugMode.LOAD);
    }

    private static void debugConfirmChoice() {
        int index = debugSelector.getSelectedIndex();
        if (index < 0) {
            return;
        }
        String selection = debugSelector.getItems().get(debugSelector.getSelectedIndex());
        switch ((DebugMode) debugPane.getUserObject()) {
            case CONV:
                conversationController.loadConversation(selection);
                conversationController.setBranch("default");
                break;
            case BRANCH:
                conversationController.setBranch(selection);
                break;
            case LOAD:
                SaveManager.load(Gdx.files.internal("Saves/" + selection));
                break;
        }
    }

    private enum DebugMode {
        CONV, BRANCH, LOAD
    }
}
