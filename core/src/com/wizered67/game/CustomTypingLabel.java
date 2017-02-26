package com.wizered67.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

/**
 * Extends TypingLabel and overrides the act method to cancel anything from happening.
 * To trigger the normal act method, controlledAct must be called. This allows more control
 * over when the actor acts, rather than always being once per frame.
 * @author Adam Victor
 */
public class CustomTypingLabel extends TypingLabel {
    public CustomTypingLabel(CharSequence text, LabelStyle style) {
        super(text, style);
    }

    public CustomTypingLabel(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
    }

    public CustomTypingLabel(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
    }

    public CustomTypingLabel(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public CustomTypingLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    @Override
    public void act (float delta) {

    }

    public void controlledAct(float delta) {
        super.act(delta);
    }
}
