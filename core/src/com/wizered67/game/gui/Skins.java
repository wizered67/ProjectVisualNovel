package com.wizered67.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.wizered67.game.Constants;

/**
 * Creates and returns singleton skins.
 * @author Adam Victor
 */
public class Skins {
    /** Constant denoting space between left side of the textbox and text. */
    private final int TEXTBOX_LEFT_PADDING = 10;
    private Skin conversationSkin;
    private BitmapFont defaultFont;


    /** Returns the default font, or initializes the font if it doesn't exist. */
    public BitmapFont getDefaultFont() {
        if (defaultFont == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            //float densityIndependentSize = Constants.REGULAR_FONT_SIZE * Gdx.graphics.getDensity();
            parameter.size = Constants.REGULAR_FONT_SIZE; //(int) (Constants.REGULAR_FONT_SIZE * Gdx.graphics.getHeight() / Constants.DEFAULT_HEIGHT);//Math.round(densityIndependentSize);
            defaultFont = generator.generateFont(parameter); // font size 12 pixels
            defaultFont.getData().markupEnabled = true;
            generator.dispose(); // don't forget to dispose to avoid memory leaks!
        }
        return defaultFont;
    }

    public Skin getConversationSkin() {
        if (conversationSkin == null) {
            conversationSkin = new Skin(new TextureAtlas(Gdx.files.internal("Skins/uiskin.atlas")));
            // A bit of a workaround here. We want to use the generated font but also need to define a font in uiskin for testing.
            // Make sure to remove the font from uiskin once the skin is done. This will override the skin to add a font.
            conversationSkin.add("default-font", getDefaultFont());
            conversationSkin.load(Gdx.files.internal("Skins/uiskin.json"));
            Drawable dialogueDrawable = conversationSkin.getDrawable("dialogue-drawable-offset");
            dialogueDrawable.setLeftWidth(TEXTBOX_LEFT_PADDING);
            dialogueDrawable.setRightWidth(TEXTBOX_LEFT_PADDING);
            dialogueDrawable.setTopHeight(TEXTBOX_LEFT_PADDING / 2);
            dialogueDrawable.setBottomHeight(TEXTBOX_LEFT_PADDING / 2);
        }
        return conversationSkin;
    }
}
