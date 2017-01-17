package com.wizered67.game.GUI;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.wizered67.game.Constants;
import com.wizered67.game.GUI.Conversations.ConversationController;
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
	private static Label textboxLabel;
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

    /** Initializes all of the GUI elements and adds them to the Stage ST. Also
     * initializes ConversationController with the elements it will update.
     */
    public GUIManager(Stage st){
		stage = st;
 		// Generate a 1x1 white texture and store it in the skin named "white".
 		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
 		pixmap.setColor(Color.WHITE);
 		pixmap.fill();
 		skin.add("white", new Texture(pixmap));
 		// Store the default libgdx font under the name "default".
		BitmapFont defaultFont;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float densityIndependentSize = Constants.REGULAR_FONT_SIZE * Gdx.graphics.getDensity();
        parameter.size = Math.round(densityIndependentSize);
		defaultFont = generator.generateFont(parameter); // font size 12 pixels
        defaultFont.getData().markupEnabled = true;
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
 		skin.add("default", defaultFont);
    	 table = new Table();
    	 table.setFillParent(true);
    	 stage.addActor(table);
	     table.setDebug(true); // This is optional, but enables debug lines for tables.
    	    // Add widgets to the table here.
	     
	     TextButtonStyle textButtonStyle = new TextButtonStyle();
			textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
			textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
			textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
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
                }
            });
            tb.setVisible(false);
            choiceButtons[i] = tb;
            stage.addActor(tb);
        }

		Label.LabelStyle labelStyle = new Label.LabelStyle();
		labelStyle.font = skin.getFont("default");
		Drawable newDrawable = skin.newDrawable("white", Color.DARK_GRAY);
		newDrawable.setLeftWidth(LEFT_PADDING);
        newDrawable.setLeftWidth(LEFT_PADDING);
		//newDrawable.setRightWidth(20);
		labelStyle.background = newDrawable;
		skin.add("default", labelStyle);
		textboxLabel = new Label("", skin);
		textboxLabel.setAlignment(Align.topLeft);
		textboxLabel.setStyle(labelStyle);
        textboxLabel.setWrap(true);
        stage.addActor(textboxLabel);

        Label.LabelStyle speakerLabelStyle = new Label.LabelStyle();
        speakerLabelStyle.font = skin.getFont("default");
        Drawable speakerDrawable = skin.newDrawable("white", Color.GRAY);
        speakerDrawable.setLeftWidth(5);
        //speakerDrawable.setRightWidth(5);
        //newDrawable.setRightWidth(20);
        speakerLabelStyle.background = speakerDrawable;
        skin.add("speaker", speakerLabelStyle);
        speakerLabel = new Label("Really long speaker name", skin, "speaker");
        //speakerLabel.setStyle(speakerLabelStyle);
        speakerLabel.setAlignment(Align.center);
        stage.addActor(speakerLabel);

        conversationController = new ConversationController(textboxLabel, speakerLabel, choiceButtons);
        setTextboxShowing(false);
        //System.out.println(remainingTextNoTags);
        //remainingText = "this is a new message just so you know.";
		//textboxLabel.setText("TESTING A MESSAGE BRO");
		//textboxLabel = new Label("this is a really long test message and I want to see if word wrap is doing anything? Test MessageCommand!", labelStyle);

		//textboxLabel.setPosition(200, 400);
		//textboxLabel.setSize(350, 60);
		//textboxLabel.setVisible(false);
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

	}
    public GUIManager() {
        conversationController = new ConversationController();
    }
    /** Returns the Stage used to display GUI elements. */
	public static Stage getStage(){
		return stage;
	}
    /** Called every frame. Updates the ConversationController. DELTA TIME is the time
     * elapsed since the last frame.
     */
	public static void update(float deltaTime){
        conversationController.update(deltaTime);
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
}
