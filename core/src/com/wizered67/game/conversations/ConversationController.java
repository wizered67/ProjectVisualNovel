package com.wizered67.game.conversations;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.wizered67.game.Constants;
import com.wizered67.game.conversations.commands.*;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.gui.GUIManager;
import com.wizered67.game.inputs.Controllable;
import com.wizered67.game.inputs.MyInputProcessor.ControlType;
import com.wizered67.game.saving.serializers.GUIState;
import com.wizered67.game.scripting.LuaScriptManager;
import com.wizered67.game.scripting.ScriptManager;

import java.util.*;

/**
 * Updates all of the GUI elements and the SceneManager by
 * executing a series of ConversationCommands.
 * @author Adam Victor
 */
public class ConversationController implements Controllable {
    /** Label for the main textbox. Displays text when spoken by characters.
     * A reference to the one in GUIManager.*/
    private transient TypingLabel textboxLabel;
    /** Label to display the name of the current speaker.
     * A reference to the one in GUIManager. */
    private transient Label speakerLabel;
    /** Array containing TextButtons to be displayed when the player is offered a choice.
     * A reference to the one in GUIManager. */
    private transient TextButton[] choiceButtons;

    /** Array containing lists of ConversationCommands that would be executed when the
     * corresponding choice is made. */
    private List<ConversationCommand>[] choiceCommands;
    /** The index of the choice that is currently being highlighted. */
    private int choiceHighlighted = -1;

    /** A LinkedList (Queue) containing the ConversationCommands in the current branch, to be
     * executed in sequence. */
    private LinkedList<ConversationCommand> currentBranch;
    /** The ConversationCommand currently being executed. */
    private ConversationCommand currentCommand;
    /** The current Conversation containing all possible branches. */
    private Conversation currentConversation;
    /** Whether the speaking sound should be played this frame. */
    private boolean playSoundNow = true;
    /** Name of the sound used for the current speaker. */
    private String currentSpeakerSound;
    /** Whether choices are currently being shown to the player. */
    private boolean choiceShowing = false;
    /** Reference to the SceneManager that contains and updates all CharacterSprites. */
    private SceneManager sceneManager;
    /** A loader used to parse XML into a Conversation. */
    private transient ConversationLoader conversationLoader;
    /** The SceneCharacter of the current speaker. */
    private SceneCharacter currentSpeaker;
    /** Whether all text should be displayed at once without slowly scrolling. */
    private boolean displayAll = false;
    /** Map used to map Strings to the ScriptManager for the language of that name. */
    private transient static Map<String, ScriptManager> scriptManagers;
    /** State of GUI elements, like labels. Used to save and load data. */
    private GUIState guiState;
    /** Stores a record of messages and their speakers. */
    private Transcript transcript;
    /** Whether the conversation controller is paused. When paused, commands don't update. The scene will render but not update. */
    private boolean paused;

    public ConversationController() {
        initScriptManagers();
    }
    /** Initializes the ConversationController with the GUI elements passed in from GUIManager.
     * Also loads and begins a default conversation for testing purposes. */
    @SuppressWarnings("unchecked")
    public ConversationController(TypingLabel textbox, Label speaker, TextButton[] choices) {
        conversationLoader = new ConversationLoader();
        textboxLabel = textbox;
        textboxLabel.setTypingListener(new ConversationTypingListener(this));
        Constants.initTextboxSettings();
        speakerLabel = speaker;
        choiceButtons = choices;
        choiceCommands = new List[choiceButtons.length];
        sceneManager = new SceneManager(this);
        initScriptManagers();
        transcript = new Transcript();
        paused = false;
        GameManager.getMainInputProcessor().register(this);
    }

    public ConversationLoader loader() {
        return conversationLoader;
    }

    public void setConv(Conversation conv) {
        currentConversation = conv;
    }

    /** Saves the GUI state. */
    public void save() {
        guiState = new GUIState();
        guiState.speakerLabelText = speakerLabel.getText().toString();
        guiState.speakerLabelVisible = speakerLabel.isVisible();
        guiState.textboxLabelText = textboxLabel.getOriginalText().toString();//textboxLabel.getText().toString();
        guiState.textboxLabelVisible = textboxLabel.isVisible();
        String[] choiceText = new String[choiceButtons.length];
        for (int i = 0; i < choiceText.length; i += 1) {
            if (choiceShowing) {
                choiceText[i] = choiceButtons[i].getText().toString();
            } else {
                choiceText[i] = "";
            }
        }
        guiState.choiceButtonText = choiceText;
    }
    /** Loads the GUI state and Conversation. */
    public void reload() {
        if (guiState == null) {
            return;
        }
        setSpeakerName(guiState.speakerLabelText);
        speakerLabel.setVisible(guiState.speakerLabelVisible);
        //textboxLabel.setText(guiState.textboxLabelText);
        textboxLabel.restart(guiState.textboxLabelText);
        textboxLabel.setVisible(guiState.textboxLabelVisible);
        for (int i = 0; i < choiceButtons.length; i += 1) {
            setChoice(i, guiState.choiceButtonText[i]);
        }

        if (choiceHighlighted != -1) {
            choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(false);
            choiceButtons[choiceHighlighted].setChecked(true);
            choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(true);
        }
    }
    /** Returns the current Conversation. */
    public Conversation conversation() {
        return currentConversation;
    }
    /** Returns the SceneManager being used update and draw CharacterSprites. */
    public SceneManager sceneManager() {
        return sceneManager;
    }
    /** Initializes all script managers. */
    private void initScriptManagers() {
        scriptManagers = new HashMap<>();
        scriptManagers.put("Lua", new LuaScriptManager());
        //scriptManagers.put("Groovy", new GroovyScriptManager());
    }
    /** Returns the default scripting language to be used if none is specified. */
    public static String defaultScriptingLanguage() {
        return "Lua";
    }
    /** Returns the ScriptManager for LANGUAGE. */
    public static ScriptManager scriptManager(String language) {
        return scriptManagers.get(language);
    }
    /** Returns map between names and ScriptManagers. */
    public static Map<String, ScriptManager> allScriptManagers() {
        return scriptManagers;
    }
    /** Loads the Conversation with filename FILENAME. */
    public Conversation loadConversation(String fileName) {
        currentConversation = conversationLoader.loadConversation(fileName);
        return currentConversation;
    }
    public void setPaused(boolean pause) {
        paused = pause;
    }
    public boolean isPaused() {
        return paused;
    }
    /** Called every frame and updates the GUI elements by executing commands.
     * If waitToProceed is false, it continues to go onto the next command within this frame. Otherwise
     * it waits for the current one to be completed. Also calls the SceneManager's update method.
     * DELTA TIME is the time elapsed since the previous frame.
     */
    public void update(float deltaTime) {
        if (GameManager.assetManager().getQueuedAssets() != 0) {
            GameManager.assetManager().update();
            System.out.println(GameManager.assetManager().getProgress());
        }
        if (currentConversation != null) {
            if (!paused) {
                //Keep going to next command as long as there is at least one command left in this branch and there is either no
                //current command or the current command does not require waiting.
                while ((currentCommand == null || !currentCommand.waitToProceed()) && currentBranch.size() > 0) {
                    nextCommand();
                    displayAll = false;
                }
                if (currentBranch.size() == 0 && currentCommand != null && !currentCommand.waitToProceed()) {
                    exit();
                    currentCommand = null;
                }
            }
            updateText(deltaTime);
        }
        sceneManager.update(deltaTime);
    }
    /**If there is currently a message being displayed, updates the text timer.
     * While text timer is high enough or if all text should be displayed at once, it
     * displays the next character of the message. If a command is encountered in
     * the middle of the message, it executes it and returns if the update should
     * wait for the command to complete. DELTA TIME is the time elapsed since the
     * previous frame.
     */
    private void updateText(float deltaTime) {
        if (doneSpeaking()) {
            if (currentCommand != null) {
                currentCommand.complete(CompleteEvent.text());
            }
            return;
        }

        if (currentSpeaker != null && !currentSpeaker.getKnownName().equals(speakerLabel.getText().toString())) {
            setSpeakerName(currentSpeaker.getKnownName());
        }

        if (currentCommand != null && currentCommand instanceof MessageCommand) {
            MessageCommand messageCommand = (MessageCommand) currentCommand;

            if (messageCommand.shouldUpdate() && !doneSpeaking() && !paused) {
                if (displayAll) {
                    textboxLabel.skipToTheEnd(false, false);
                }
                textboxLabel.resume();
            } else {
                textboxLabel.pause();
                textboxLabel.cancelSkipping();
            }
        }
    }

    public void setMessageSubcommand(String subcommand) {
        if (currentCommand instanceof MessageCommand) {
            MessageCommand messageCommand = (MessageCommand) currentCommand;
            messageCommand.setSubcommand(subcommand);
            if (!messageCommand.shouldUpdate()) {
                textboxLabel.pause();
                textboxLabel.cancelSkipping();
            }
        } else {
            GameManager.error("Trying to set subcommand of non message command.");
        }
    }

    public void endText() {
        textboxLabel.restart();
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void addToTranscript() {
        if (textboxLabel.getText() != null && !textboxLabel.getText().toString().isEmpty()) {
            transcript.addMessage(currentSpeaker.getKnownName(), textboxLabel.getText().toString());
        }
    }

    /** Set the sound to be played for the current speaker to the sound named SOUND. */
    public void setCurrentSpeakerSound(String sound) {
        currentSpeakerSound = sound;
    }
    /** If the text sounds should be played, it gets it from the AssetManager and plays it.
     * Toggles whether the sound should be played next frame. */
    public void playTextSound() {
        if (!displayAll && playSoundNow) {
            Sound s = GameManager.assetManager().get(currentSpeakerSound, Sound.class);
            s.play();
        }
        playSoundNow = !playSoundNow;
    }

    /** If there are more commands, execute the next one. */
    private void nextCommand() {
        ConversationCommand command = currentBranch.remove();
        command.execute(this);
        //System.out.println("Executed command: " + command.toString());
        currentCommand = command;
    }
    /** Sets the current branch to a copy of the list of ConversationCommands
     * corresponding to the branch in Conversation named BRANCH. */
    @SuppressWarnings("unchecked")
    public void setBranch(String branchName) {
        LinkedList<ConversationCommand> branch = currentConversation.getBranch(branchName);
        Object b = branch.clone();
        if (b instanceof LinkedList) {
            //exit();
            currentBranch = (LinkedList<ConversationCommand>) b;
        }
    }
    /** Exits the current conversation. Clears command queue, choices, and text. */
    public void exit() {
        currentCommand = null;
        setTextBoxShowing(false);
        setChoiceShowing(false);
    }
    /** Adds the ConversationCommands in COMMANDS to the
     * front of the queue of commands to be executed for this branch.
     */
    public void insertCommands(List<ConversationCommand> commands) {
        currentBranch.addAll(0, commands);
    }
    /** Passes the CompleteEvent EVENT to the current command. */
    public void complete(CompleteEvent event) {
        if (currentCommand != null) {
            currentCommand.complete(event);
        }
    }
    /** Returns whether there is no more text to display. */
    public boolean doneSpeaking() {
        return textboxLabel.hasEnded();
    }
    /** Sets the remaining text to be displayed to TEXT. */
    public void setText(String text){
        textboxLabel.restart(text);
    }
    /** Sets the current speaking character to the one represented by CHARACTER. */
    public void setSpeaker(SceneCharacter character) {
        currentSpeaker = character;
    }
    /** Updates the speakerLabel to TEXT. */
    private void setSpeakerName(String text){
        if (text.isEmpty()) {
            speakerLabel.setVisible(false);
        } else {
            speakerLabel.setVisible(true);
            speakerLabel.setText(text + "  ");
            //speakerLabel.setSize(speakerLabel.getPrefWidth(), speakerLabel.getPrefHeight());
            //speakerLabel.invalidate();
        }
    }

    /** Sets whether the textbox and speaker label should SHOW. */
    public void setTextBoxShowing(boolean show){
        textboxLabel.setVisible(show);
        speakerLabel.setVisible(show);
    }
    /** Returns whether text is currently showing. */
    public boolean isTextShowing() {
        return textboxLabel.isVisible();
    }
    /** Set whether to display all text to DISPLAY. */
    public void setDisplayAll(boolean display) {
        displayAll = display;
    }

    /* Choices Code */

    /** Sets whether choices should SHOW for the player. */
    public void setChoiceShowing(boolean show) {
        choiceHighlighted = -1;
        choiceShowing = show;
        if (!show) {
            for (TextButton b : choiceButtons) {
                b.setVisible(false);
            }
        }
    }
    /** Sets choice number CHOICE to CHOICE NAME. */
    public void setChoice(int choice, String choiceName) {
        choiceButtons[choice].setVisible(!choiceName.isEmpty());
        choiceButtons[choice].setText(choiceName);
        choiceButtons[choice].setProgrammaticChangeEvents(false);
        choiceButtons[choice].setChecked(false);
        choiceButtons[choice].setProgrammaticChangeEvents(true);
    }
    //todo store value so method doesn't need to be recalled?
    /** The number of visible choices to choose from. */
    private int numChoices() {
        int i;
        for (i = 0; i < choiceButtons.length; i += 1) {
            if (!choiceButtons[i].isVisible()) {
                break;
            }
        }
        return i;
    }
    /** Sets choice number CHOICE to execute the list of ConversationCommands COMMANDS. */
    public void setChoiceCommand(int choice, List<ConversationCommand> commands) {
        choiceCommands[choice] = commands;
    }
    /** Adds to the front of the command queue the list of commands corresponding to CHOICE
     * and send a CompleteEvent to the current command. */
    public void processChoice(int choice) {
        setChoiceShowing(false);
        List<ConversationCommand> commands = choiceCommands[choice];
        insertCommands(commands);
        currentCommand.complete(CompleteEvent.choice());
    }

    /** Called when the left mouse button is clicked or a confirm key is pressed. If there's a
     * choice being shown, choose the selected one. If there's text being shown, proceed through it
     * and fire an input CompleteEvent.
     */
    private void inputConfirm() {
        if (choiceShowing) {
            if (choiceHighlighted != -1) {
                choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(false);
                choiceButtons[choiceHighlighted].setChecked(false);
                choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(true);
                choiceButtons[choiceHighlighted].setChecked(true);
            }
        } else {
            if (!doneSpeaking()) {
                setDisplayAll(true);
            }
            if (currentCommand != null) {
                currentCommand.complete(CompleteEvent.input());
            }
        }
    }

    /** Change the currently selected choice by AMOUNT. If it would be greater than the
     * number of choices or less than 0, loop around.
     */
    private void changeChoice(int amount) {
        if (!choiceShowing) {
            return;
        }
        if (choiceHighlighted == -1) {
            choiceHighlighted = 0;
        } else {
            choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(false);
            choiceButtons[choiceHighlighted].setChecked(false);
            choiceHighlighted += amount;
            int num = numChoices();
            if (choiceHighlighted >= num) {
                choiceHighlighted = choiceHighlighted % num;
            } else if (choiceHighlighted < 0) {
                choiceHighlighted += num;
            }
        }
        choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(false);
        choiceButtons[choiceHighlighted].setChecked(true);
        choiceButtons[choiceHighlighted].setProgrammaticChangeEvents(true);
    }

    /** Handles a touch (or click) on the screen and passes an Input CompleteEvent to the current
     * ConversationCommand. If someone is currently speaking, instead set displayAll to true
     * first. */
    @Override
    public void touchEvent(int screenX, int screenY, int pointer, int button, boolean pressed) {
        if (button == Input.Buttons.LEFT && pressed) {
            inputConfirm();
        }
    }
    /** Handles a key event by calling methods depending on the ControlType. */
    @Override
    public void keyEvent(ControlType control, int key, boolean pressed) {
        if (pressed) {
            switch (control) {
                case CONFIRM:
                    inputConfirm();
                    break;
                case UP:
                    if (GUIManager.isTranscriptVisible()) {
                        GUIManager.scrollTranscript(-1);
                    } else if (choiceShowing) {
                        changeChoice(-1);
                    }
                    break;
                case DOWN:
                    if (GUIManager.isTranscriptVisible()) {
                        GUIManager.scrollTranscript(1);
                    } else if (choiceShowing) {
                        changeChoice(1);
                    }
                    break;
                default:
                    break;
            }
        } else { //released
            switch (control) {
                case UP:
                    GUIManager.stopTranscriptScrolling();
                    break;
                case DOWN:
                    GUIManager.stopTranscriptScrolling();
                    break;
            }
        }
    }
}
