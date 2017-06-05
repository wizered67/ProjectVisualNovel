package com.wizered67.game.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.wizered67.game.conversations.Transcript;
import com.wizered67.game.inputs.Controls;

/**
 * UIComponent used for showing the Transcript contents.
 * @author Adam Victor
 */
public class TranscriptUI implements UIComponent {
    public static final String ID = "TranscriptUI";
    private GUIManager guiManager;
    private Transcript transcript;
    private Skin skin;
    private Table mainTable;
    private Label transcriptLabel;
    private ScrollPane transcriptPane;
    /** Whether the transcript is scrolling, and in which direction. */
    private float transcriptScrolling = 0;

    public TranscriptUI(GUIManager guiManager, Skin skin, Transcript transcript) {
        this.guiManager = guiManager;
        this.skin = skin;
        this.transcript = transcript;
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.setVisible(false);
        mainTable.toFront();

        transcriptLabel = new Label("", skin, "dialogue-label");
        transcriptLabel.setWrap(true);
        transcriptLabel.setAlignment(Align.topLeft);
        transcriptPane = new ScrollPane(transcriptLabel, skin);
        transcriptPane.setOverscroll(false, false);
        transcriptPane.toFront();

        mainTable.add(transcriptPane).expand().fill().pad(Value.percentWidth(.05f, mainTable));
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isVisible() {
        return mainTable.isVisible() && transcript != null;
    }

    @Override
    public void update(float deltaTime) {
        if (isVisible()) {
            if (transcript.isDirty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Transcript.TranscriptMessage message : transcript.getTranscriptMessages()) {
                    stringBuilder.append("[CYAN]").append(message.getSpeaker()).append(": [WHITE]\n");
                    stringBuilder.append(message.getMessage()).append("\n\n");
                }
                transcriptLabel.setText(stringBuilder);
                transcriptLabel.invalidate();
                transcriptLabel.getPrefHeight(); //fixme part of hacky solution to get size correct for first time
                transcript.setDirty(false);
            }
            if (transcriptScrolling != 0) {
                float velocity = transcriptPane.getVelocityY();
                transcriptPane.fling(1, 0, velocity);
                transcriptPane.setVelocityY(velocity + 4 * Math.signum(velocity));
            }
        }
    }

    private void toggleTranscript() {
        mainTable.setVisible(!isVisible());
        guiManager.conversationController().setPaused(isVisible());
        update(0); //todo fix. part of hacky solution to make update first time
        //transcriptPane.invalidate();
        transcriptScrolling = 0;
        transcriptPane.validate();
        transcriptPane.setScrollPercentY(1f);
        transcriptPane.updateVisualScroll();
        transcriptPane.setVelocityY(0);
        guiManager.getStage().setScrollFocus(transcriptPane);
    }

    private void scrollTranscript(int direction) {
        transcriptPane.fling(1, 0, -direction * 50);
        transcriptScrolling = direction * 0.005f * transcriptPane.getHeight();
        //System.out.println("Velocity set to " + transcriptPane.getVelocityY());
    }

    private void stopTranscriptScrolling() {
        transcriptScrolling = 0;
        transcriptPane.setVelocityY(0);
    }

    @Override
    public void resize(int newWidth, int newHeight) {

    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Actor getMainActor() {
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
        switch (control) {
            case TRANSCRIPT:
                toggleTranscript();
                return true;
            case UP:
                if (isVisible()) {
                    scrollTranscript(-1);
                    return true;
                }
                break;
            case DOWN:
                if (isVisible()) {
                    scrollTranscript(1);
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(Controls.ControlType control, int key) {
        switch (control) {
            case UP:
            case DOWN:
                if (isVisible()) {
                    stopTranscriptScrolling();
                    return true;
                }
                break;
            default:
                return false;
        }
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
