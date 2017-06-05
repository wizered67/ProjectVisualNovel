package com.wizered67.game.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.wizered67.game.inputs.Controls;

/**
 * UI used for getting text input from the player and processing it.
 * @author Adam Victor
 */
public class TextInputUI implements UIComponent {
    public static String ID = "TextInputUI";
    private GUIManager guiManager;
    private Table inputTable;
    private Table mainTable;
    private Skin skin;
    private Label titleLabel;
    private TextField textField;
    private Input.TextInputListener callback;

    TextInputUI(GUIManager guiManager, Skin skin) {
        this.guiManager = guiManager;
        this.skin = skin;
        mainTable = new Table();
        mainTable.setFillParent(true);
        inputTable = new Table(skin);
        mainTable.add(inputTable);
        inputTable.background(skin.newDrawable("white", Color.DARK_GRAY));
        titleLabel = new Label("Title", skin);
        textField = new TextField("Hello world!", skin);
        TextButton submit = new TextButton("Okay", skin);
        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
                ((Button) actor).setProgrammaticChangeEvents(false);
                ((Button) actor).setChecked(false);
                ((Button) actor).setProgrammaticChangeEvents(true);
                if (callback != null) {
                    callback.input(textField.getText());
                }
                hide();
            }
        });
        TextButton cancel = new TextButton("Cancel", skin);
        cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
                ((Button) actor).setProgrammaticChangeEvents(false);
                ((Button) actor).setChecked(false);
                ((Button) actor).setProgrammaticChangeEvents(true);
                if (callback != null) {
                    callback.canceled();
                }
                hide();
            }
        });
        Value padding = Value.percentWidth(0.02f, mainTable);
        inputTable.center().add(titleLabel).colspan(2).padLeft(padding).padRight(padding);
        inputTable.row();
        inputTable.add(textField).colspan(2).padLeft(padding).padRight(padding);
        inputTable.row();
        inputTable.add(submit).padLeft(padding).padTop(padding).padBottom(padding);
        inputTable.add(cancel).padRight(padding).padTop(padding).padBottom(padding);
        hide();
    }

    public void display(String title, String defaultText, String hint, Input.TextInputListener callback) {
        titleLabel.setText(title);
        textField.setText(defaultText);
        textField.setMessageText(hint);
        this.callback = callback;
        mainTable.setVisible(true);
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextInputUI)) {
            return false;
        } else {
            return ((TextInputUI) other).getId().equals(getId());
        }
    }

    @Override
    public boolean isVisible() {
        return mainTable.isVisible();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void resize(int newWidth, int newHeight) {

    }

    @Override
    public String getId() {
        return ID;
    }

    public void hide() {
        if (guiManager.getStage().getKeyboardFocus() == textField) {
            guiManager.getStage().setKeyboardFocus(null);
        }
        mainTable.setVisible(false);
    }
    @Override
    public Table getMainActor() {
        return mainTable;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(Controls.ControlType control, int key) {
        return false;
    }

    @Override
    public boolean keyUp(Controls.ControlType control, int key) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}
