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
import com.wizered67.game.GUI.Conversations.MessageWindow;

public class GUIManager {
	private static Table table;
	private static Skin skin = new Skin();
	private static Label textboxLabel;
    private static Label speakerLabel;
	private static Stage stage;
	private final static Vector2 TEXTBOX_SIZE = new Vector2(360, 50);
    private final static int LEFT_PADDING = 10;
    private static MessageWindow messageWindow;

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
        int fontSize = Math.round(densityIndependentSize );
        parameter.size = fontSize;
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
			textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
			textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
			textButtonStyle.font = skin.getFont("default");
			skin.add("default", textButtonStyle);
			final TextButton button = new TextButton("Click me!", skin);
			button.setPosition(40, 40);
			button.setSize(60, 60);
			//table.add(button).pad(20).expand().bottom().left();
			stage.addActor(button);
			// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
			// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
			// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
			// revert the checked state.
			button.addListener(new ChangeListener() {
				public void changed (ChangeEvent event, Actor actor) {
					System.out.println("Clicked! Is checked: " + ((Button)actor).isChecked());
					((TextButton)actor).setText("Good job!");
				}
			});

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

        messageWindow = new MessageWindow(textboxLabel, speakerLabel);

        //System.out.println(remainingTextNoTags);
        //remainingText = "this is a new message just so you know.";
		//textboxLabel.setText("TESTING A MESSAGE BRO");
		//textboxLabel = new Label("this is a really long test message and I want to see if word wrap is doing anything? Test Message!", labelStyle);

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

	public static Stage getStage(){
		return stage;
	}

	public static void setTextBoxShowing(boolean show){
		messageWindow.setTextBoxShowing(show);
	}

	public static void update(float deltaTime){
        messageWindow.update(deltaTime);
	}

    public static void setTextTimer(int delay){
        messageWindow.setTextTimer(delay);
    }

	public static void resize(int width, int height){
		textboxLabel.setSize(TEXTBOX_SIZE.x * width / Constants.VIRTUAL_WIDTH,
				TEXTBOX_SIZE.y * height / Constants.VIRTUAL_HEIGHT);
		textboxLabel.setPosition((width - textboxLabel.getWidth()) / 2,
				height / 16); //actor position is from bottom left of it
        textboxLabel.invalidate();
        //numLines = textboxLabel.getGlyphLayout().runs.size;
        speakerLabel.setPosition(textboxLabel.getX(), textboxLabel.getY() + textboxLabel.getHeight());
        speakerLabel.invalidate();
	}

	public static void setRemainingText(String text){
		messageWindow.setRemainingText(text);
	}

    public static void setSpeaker(String text){
        messageWindow.setSpeaker(text);
    }
}
