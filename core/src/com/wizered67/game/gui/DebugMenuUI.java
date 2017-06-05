package com.wizered67.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.wizered67.game.Constants;
import com.wizered67.game.conversations.Conversation;
import com.wizered67.game.inputs.Controls;
import com.wizered67.game.saving.SaveManager;

/**
 * UIComponent for displaying a debug menu to the player, if debug mode is enabled.
 * In the debug menu the user can change conversations, change branches, save the game, and
 * load a save file.
 * @author Adam Victor
 */
public class DebugMenuUI implements UIComponent {
    public static final String ID = "DebugMenuUI";
    private GUIManager guiManager;
    private Table mainTable;
    private ScrollPane debugPane;
    private HorizontalGroup debugChoices;
    private List<String> debugSelector;
    private boolean saveInputShowing = false;
    public DebugMenuUI(GUIManager guiManager, Skin skin) {
        this.guiManager = guiManager;
        mainTable = new Table();
        mainTable.setFillParent(true);
        //mainTable.setDebug(true);

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

        debugSelector = new List<>(skin);
        debugPane = new ScrollPane(debugSelector, skin);
        debugPane.toFront();
        debugPane.setUserObject(DebugMode.CONV);
        debugPane.setVisible(false);

        mainTable.top().add(debugChoices).expandX()
                .padLeft(Value.percentWidth(.05f, mainTable))
                .padRight(Value.percentWidth(.05f, mainTable))
                .padTop(Value.percentHeight(.05f, mainTable));
        mainTable.row();

        mainTable.add(debugPane)
                .width(Value.percentWidth(.33f, mainTable))
                .height(Value.percentHeight(.5f, mainTable))
                .padTop(Value.percentHeight(.05f, mainTable));

    }

    private void toggleDebugDisplay() {
        debugChoices.setVisible(!debugChoices.isVisible());
        if (!debugChoices.isVisible()) {
            debugPane.setVisible(false);
        }
        guiManager.conversationController().setPaused(debugChoices.isVisible());
    }

    private void debugChangePaneType(Array<String> content, DebugMode type) {
        debugSelector.setItems(content);
        debugSelector.setSelectedIndex(-1);
        debugPane.setUserObject(type);
        debugPane.setVisible(true);
    }

    private void debugConversations() {
        Array<String> fileNames = new Array<>();
        FileHandle[] files = Gdx.files.local("Conversations/").list(".conv");
        for(FileHandle file: files) {
            fileNames.add(file.name());
        }
        debugChangePaneType(fileNames, DebugMode.CONV);
    }

    private void debugBranches() {
        Conversation currentConv = guiManager.conversationController().conversation();
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
                guiManager.conversationController().exit();
                guiManager.conversationController().loadConversation(selection);
                break;
            case BRANCH:
                guiManager.conversationController().setBranch(selection);
                guiManager.conversationController().nextCommand();
                break;
            case LOAD:
                SaveManager.load(Gdx.files.internal("Saves/" + selection));
                break;
        }
    }

    @Override
    public int getPriority() {
        return 2;
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
        return false;
    }

    @Override
    public boolean keyUp(Controls.ControlType control, int key) {
        if (control == Controls.ControlType.DEBUG && Constants.DEBUG) {
            toggleDebugDisplay();
            return true;
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

    private enum DebugMode {
        CONV, BRANCH, LOAD
    }
}
