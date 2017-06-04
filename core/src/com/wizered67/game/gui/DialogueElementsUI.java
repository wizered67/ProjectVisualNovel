package com.wizered67.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.wizered67.game.Constants;
import com.wizered67.game.GameManager;

/**
 * Creates and contains elements necessary for the main dialogue UI, including textboxes and choice buttons.
 * @author Adam Victor
 */
public class DialogueElementsUI {
    private GUIManager guiManager;
    private Table mainTable;
    private Table buttonTable;
    private TypingLabel textboxLabel;
    private Label speakerLabel;
    private TextButton[] choiceButtons;

    public DialogueElementsUI(GUIManager manager, Skin skin) {
        guiManager = manager;
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setDebug(Constants.DEBUG);
        /*
        Texture texture = new Texture(Gdx.files.internal("Textures/testicon.png"));
        TextureRegionDrawable icon = new TextureRegionDrawable(new TextureRegion(texture));
        Drawable iconDown = icon.tint(Color.GRAY);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = icon;
        style.imageOver = iconDown;
        ImageButton saveButton = new ImageButton(style);
        mainTable.add(saveButton).height(Value.percentHeight(.1f, mainTable));
        mainTable.row();
        */
        buttonTable = new Table();
        buttonTable.setDebug(Constants.DEBUG);
        mainTable.add(buttonTable).expandX().fillX().padBottom(Value.percentHeight(0.1f, mainTable));//.height(Value.percentHeight(0.5f, mainTable));
        mainTable.row();
        choiceButtons = new TextButton[4];
        for (int i = 0; i < choiceButtons.length; i += 1) {
            TextButton tb = new TextButton("", skin, "choice-button");
            tb.setUserObject(i);
            tb.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    System.out.println("Clicked button " + actor.getUserObject());
                    guiManager.conversationController().processChoice((Integer) actor.getUserObject());
                    event.cancel();
                    ((Button) actor).setProgrammaticChangeEvents(false);
                    ((Button) actor).setChecked(false);
                    ((Button) actor).setProgrammaticChangeEvents(true);
                }
            });
            tb.setVisible(false);
            buttonTable.add(tb).growX().padLeft(20).padRight(20).padBottom(20).height(Value.percentHeight(0.08f, mainTable));
            if (i != choiceButtons.length - 1) {
                buttonTable.row();
            }
            choiceButtons[i] = tb;
        }

        textboxLabel = new TypingLabel("", skin, "dialogue-label");
        textboxLabel.setAlignment(Align.topLeft);
        textboxLabel.setWrap(true);
        textboxLabel.toFront();

        speakerLabel = new Label("", skin, "speaker-label");
        speakerLabel.toBack();
        speakerLabel.setAlignment(Align.center);

        mainTable.add(speakerLabel).width(Value.percentWidth(0.2f, textboxLabel)).padLeft(20).left();
        mainTable.row();
        mainTable.bottom().add(textboxLabel).
                expandX().fillX().height(Value.percentHeight(0.24f, mainTable))
                .padLeft(20).padRight(20).padBottom(20).colspan(10);

    }

    public Table getMainTable() {
        return mainTable;
    }

    public void act(float delta) {

    }

    public TypingLabel getTextboxLabel() {
        return textboxLabel;
    }

    public Label getSpeakerLabel() {
        return speakerLabel;
    }

    public TextButton[] getChoiceButtons() {
        return choiceButtons;
    }

    public void resize(int newWidth, int newHeight) {
        //necessary to call this directly. It seems resizing the screen resizes the main table, but doesn't
        //resize the buttonTable. The problem is that the buttons don't recalculate their size even though the thing they're
        //based on has been changed. Calling invalidate on the button table automatically forces all button sizes to be recomputed.
        buttonTable.invalidate();
    }

}

/*
dialogueElementsTable = new Table();
        dialogueElementsTable.setFillParent(true);
        stage.addActor(dialogueElementsTable);
        dialogueElementsTable.setDebug(Constants.DEBUG); // This is optional, but enables debug lines for tables.
        // Add widgets to the table here.

        Table choiceTable = new Table();
        choiceTable.setDebug(Constants.DEBUG);
        dialogueElementsTable.add(choiceTable).top().grow().padBottom(40).padTop(40);//.expand().fill();
        dialogueElementsTable.row();
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
            Cell cell = choiceTable.top().add(tb).expand().fill().padLeft(100).padRight(100).padBottom(30).minHeight(35);
            if (i == 0) {
                cell.padTop(30);
            }
            if (i < choiceButtons.length - 1) {
                choiceTable.row();
            }
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

        //stage.addActor(speakerLabel);
        Table aboveTextboxTable = new Table();
        aboveTextboxTable.setDebug(Constants.DEBUG);
        dialogueElementsTable.add(aboveTextboxTable).expandX().fillX().colspan(3).padLeft(40).padRight(40).minHeight(40);

        aboveTextboxTable.left().add(speakerLabel).minWidth(80).maxWidth(150).minHeight(40);
        Table menuButtonsTable = new Table();
        menuButtonsTable.setDebug(Constants.DEBUG);
        aboveTextboxTable.add(menuButtonsTable).expandX().fillX();

        TextButton transcriptButton = new TextButton("T", skin);
        transcriptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
                toggleTranscript();
            }
        });
        menuButtonsTable.right().add(transcriptButton).minWidth(40).minHeight(40).padRight(20);
        TextButton saveButton = new TextButton("S", skin);
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
                SaveManager.save(Gdx.files.local("Saves/game.sav"));
            }
        });
        menuButtonsTable.add(saveButton).minWidth(40).minHeight(40).padRight(20);
        TextButton loadButton = new TextButton("L", skin);
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                event.cancel();
                SaveManager.load(Gdx.files.local("Saves/game.sav"));
            }
        });
        menuButtonsTable.add(loadButton).minWidth(40).minHeight(40);
        dialogueElementsTable.row();
		textboxLabel = new TypingLabel("", skin);
		textboxLabel.setAlignment(Align.topLeft);
		textboxLabel.setStyle(labelStyle);
        textboxLabel.setWrap(true);
        textboxLabel.toFront();
        dialogueElementsTable.bottom().add(textboxLabel).expandX().fillX().expandY().fillY().minHeight(150).padLeft(40).padRight(40).padBottom(40).colspan(3);
 */
