package com.wizered67.game.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Adam on 5/27/2017.
 */
public class TextInputUI {
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
                mainTable.setVisible(false);
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
                mainTable.setVisible(false);
            }
        });
        Value padding = Value.percentWidth(0.02f, mainTable);
        inputTable.center().add(titleLabel).colspan(2).padLeft(padding).padRight(padding);
        inputTable.row();
        inputTable.add(textField).colspan(2).padLeft(padding).padRight(padding);
        inputTable.row();
        inputTable.add(submit).padLeft(padding).padTop(padding).padBottom(padding);
        inputTable.add(cancel).padRight(padding).padTop(padding).padBottom(padding);
        mainTable.setVisible(false);
    }

    public void display(String title, String defaultText, String hint, Input.TextInputListener callback) {
        titleLabel.setText(title);
        textField.setText(defaultText);
        textField.setMessageText(hint);
        this.callback = callback;
        mainTable.setVisible(true);
    }

    public boolean isVisible() {
        return mainTable.isVisible();
    }

    public void hide() {
        mainTable.setVisible(false);
    }

    public Table getMainTable() {
        return mainTable;
    }
}
