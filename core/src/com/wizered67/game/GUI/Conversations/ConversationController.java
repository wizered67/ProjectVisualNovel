package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.wizered67.game.Constants;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.GUIManager;
import com.wizered67.game.GameManager;
import com.wizered67.game.Inputs.Controllable;
import com.wizered67.game.Saving.Serializers.GUIState;
import com.wizered67.game.Saving.SaveManager;
import com.wizered67.game.Scripting.GroovyScriptManager;
import com.wizered67.game.Scripting.LuaScriptManager;
import com.wizered67.game.Scripting.ScriptManager;

import java.util.*;

/**
 * Updates all of the GUI elements and the SceneManager by
 * executing a series of ConversationCommands.
 * @author Adam Victor
 */
public class ConversationController implements Controllable {
    /** Text left to be displayed. May include some tags. */
    private String remainingText = "";
    /** Text left to be displayed, ignoring Color tags. */
    private String remainingTextNoTags = "";
    /** Number of frames until message text is updated. */
    private int textTimer = 4;
    /** Number of lines currently in the textbox label. */
    private int numLines = 0;
    /** Padding between text and the left side of the textbox label. */
    private final int LEFT_PADDING = 10;
    /** Whether a dummy tag has just been added. */
    private boolean dummyTagAdded = false;
    /** Number of frames between each message text update. */
    private int textTimerDelay = 2;
    /** Label for the main textbox. Displays text when spoken by characters.
     * A reference to the one in GUIManager.*/
    private transient Label textboxLabel;
    /** Label to display the name of the current speaker.
     * A reference to the one in GUIManager. */
    private transient Label speakerLabel;
    /** Array containing TextButtons to be displayed when the player is offered a choice.
     * A reference to the one in GUIManager. */
    private transient TextButton[] choiceButtons;
    /** Array containing lists of ConversationCommands that would be executed when the
     * corresponding choice is made. */
    private List<ConversationCommand>[] choiceCommands;
    /** A LinkedList (Queue) containing the ConversationCommands in the current branch, to be
     * executed in sequence. */
    private LinkedList<ConversationCommand> currentBranch;
    /** The ConversationCommand currently being executed. */
    private ConversationCommand currentCommand;
    /** The current Conversation containing all possible branches. */
    private Conversation currentConversation;
    /** Filename of current conversation. */
    private String conversationName;
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
    /** The CharacterSprite of the current speaker. */
    private CharacterSprite currentSpeaker;
    /** Whether all text should be displayed at once without slowly scrolling. */
    private boolean displayAll = false;
    /** Map used to map Strings to the ScriptManager for the language of that name. */
    private transient static Map<String, ScriptManager> scriptManagers;
    /** State of GUI elements, like labels. Used to save and load data. */
    private GUIState guiState;

    public ConversationController() {
        initScriptManagers();
    }
    /** Initializes the ConversationController with the GUI elements passed in from GUIManager.
     * Also loads and begins a default conversation for testing purposes. */
    @SuppressWarnings("unchecked")
    public ConversationController(Label textbox, Label speaker, TextButton[] choices) {
        conversationLoader = new ConversationLoader();
        textboxLabel = textbox;
        speakerLabel = speaker;
        choiceButtons = choices;
        choiceCommands = new List[choiceButtons.length];
        sceneManager = new SceneManager(this);
        initScriptManagers();
        if (!Constants.LOAD) { //todo fix
            loadConversation("demonstration.conv");
            setBranch("default");
        }
        //remainingText =
       //remainingTextNoTags = removeTags(remainingText);
        GameManager.getMainInputProcessor().register(this);
    }
    /** Initializes all script managers. */
    private void initScriptManagers() {
        scriptManagers = new HashMap<String, ScriptManager>();
        scriptManagers.put("Lua", new LuaScriptManager());
        scriptManagers.put("Groovy", new GroovyScriptManager());
    }
    /** Saves the GUI state. */
    public void save() {
        guiState = new GUIState();
        guiState.speakerLabelText = speakerLabel.getText().toString();
        guiState.speakerLabelVisible = speakerLabel.isVisible();
        guiState.textboxLabelText = textboxLabel.getText().toString();
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
        textboxLabel.setText(guiState.textboxLabelText);
        textboxLabel.setVisible(guiState.textboxLabelVisible);
        for (int i = 0; i < choiceButtons.length; i += 1) {
            setChoice(i, guiState.choiceButtonText[i]);
        }
        numLines = textboxLabel.getGlyphLayout().runs.size;
    }
    /** Returns the current Conversation. */
    public Conversation conversation() {
        return currentConversation;
    }
    /** Returns the SceneManager being used update and draw CharacterSprites. */
    public SceneManager sceneManager() {
        return sceneManager;
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
        conversationName = fileName;
        return currentConversation;
    }
    /** Called every frame and updates the GUI elements by executing commands.
     * If waitToProceed is false, it continues to go onto the next command. Otherwise
     * it waits for the current one to be completed. Also calls the SceneManager's update method.
     * DELTA TIME is the time elapsed since the previous frame.
     */
    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            SaveManager.save(Gdx.files.local("Saves/test2.bin")); //todo remove
        }
        if (GameManager.assetManager().getQueuedAssets() != 0) {
            GameManager.assetManager().update();
            System.out.println(GameManager.assetManager().getProgress());
        }
        while ((currentCommand == null || !currentCommand.waitToProceed()) && currentBranch.size() != 0) {
            nextCommand();
            displayAll = false;
        }
        updateText(deltaTime);
        sceneManager.update(deltaTime);
    }
    /** If there is currently a message being displayed, updates the text timer.
     * Once the text timer reaches 0 or if all text should be displayed at once it
     * displays the next character of the message. If a command is encountered in
     * the middle of the message, it executes it and returns if the update should
     * wait for the command to complete. DELTA TIME is the time elapsed since the
     * previous frame.
     */
    public void updateText(float deltaTime) {
        if (doneSpeaking()) {
            if (currentCommand != null) {
                currentCommand.complete(CompleteEvent.text());
            }
            return;
        }

        if (currentSpeaker != null) {
            setSpeakerName(currentSpeaker.getKnownName());
        }

        MessageCommand messageCommand;
        textTimer = Math.max(textTimer - 1, 0);
        if (textTimer <= 0 || displayAll) {
            do {
                if (currentCommand != null && currentCommand instanceof MessageCommand) {
                    messageCommand = (MessageCommand) currentCommand;
                    if (!messageCommand.shouldUpdate()) {
                        return;
                    }
                } else {
                    return;
                }
                boolean textAdded = false;
                boolean tagAdded = false;
                String newText = null;
                String originalText = textboxLabel.getText().toString();
                if (dummyTagAdded) {
                    dummyTagAdded = false;
                }
                while (!textAdded) {
                    String[] words = remainingTextNoTags.split(" ");
                    String nextWord = "";
                    boolean command = true;
                    int index = 0;
                    while (command) {
                        command = false;
                        if (words.length > index) {
                            nextWord = words[index];
                            if (nextWord.matches(".*@c\\{(.*)\\}.*")) {
                                remainingTextNoTags = remainingTextNoTags.substring(nextWord.length() + 1);
                                remainingText = remainingText.substring(nextWord.length() + 1);
                                // replaceFirst("(.*)@c\\{.*\\}(.*)", "$1$2");
                                command = true;
                                messageCommand.setSubcommand(nextWord);
                                if (!messageCommand.shouldUpdate()) {
                                    return;
                                }
                            }
                        } else {
                            nextWord = "";
                        }
                        index += 1;
                    }
                    String testText = originalText + nextWord;
                    textboxLabel.setText(testText + "nn");
                    textboxLabel.layout();
                    int currentNumLines = textboxLabel.getGlyphLayout().runs.size;
                    newText = originalText;
                    String tag = getTag(remainingText);
                    if (tag == null) {
                        if (currentNumLines != 1 && currentNumLines > numLines) {
                            newText = newText + "\n";
                        }
                        if (remainingText.length() != 0) {
                            char nextChar = remainingText.charAt(0);
                            newText += nextChar;
                            if (nextChar != ' ' && !displayAll) {
                                playTextSound();
                            }
                            remainingText = remainingText.substring(1);
                            remainingTextNoTags = remainingTextNoTags.substring(1);
                        }
                        textAdded = true;

                    } else {
                        tagAdded = true;
                        if (tag.equals("[\n]")) {
                            tag = "\n";
                        }
                        newText += tag;
                        remainingText = remainingText.substring(tag.length());
                        currentNumLines += 1;
                    }
                    numLines = currentNumLines;
                    originalText = newText;
                }
                if (tagAdded) {
                    String closeTag = getTag(remainingText);
                    /*
                    if (closeTag != null) {
                        //newText += closeTag;
                        //remainingText = remainingText.substring(closeTag.length());
                        dummyTagAdded = false;
                    } else {
                        dummyTagAdded = true;
                        //newText += "[]";
                    }
                    */
                    dummyTagAdded = (closeTag == null);
                }
                textTimer = textTimerDelay;
                textboxLabel.setText(newText);
                textboxLabel.layout();
                numLines = textboxLabel.getGlyphLayout().runs.size;
                textboxLabel.invalidate();
            } while (displayAll && !doneSpeaking());
        }
    }
    /** Set the sound to be played for the current speaker to the sound named SOUND. */
    public void setCurrentSpeakerSound(String sound) {
        currentSpeakerSound = sound;
    }
    /** If the text sounds should be played, it gets it from the AssetManager and plays it.
     * Toggles whether the sound should be played next frame. */
    private void playTextSound() {
        if (playSoundNow) {
            Sound s = GameManager.assetManager().get(currentSpeakerSound, Sound.class);
            s.play();
        }
        playSoundNow = !playSoundNow;
    }
    //TODO Fix this method and \n in messages later.
    private int numNewLineTags(String word) {
        return word.length() - word.replaceAll("\n", "").length();
    }
    /** If there are more commands, execute the next one. */
    public void nextCommand() {
        if (currentBranch.size() != 0) {
            ConversationCommand command = currentBranch.remove();
            command.execute(this);
            //System.out.println("Executed command: " + command.toString());
            currentCommand = command;
        } else {
            setTextBoxShowing(false);
        }
    }
    /** Sets the current branch to a copy of the list of ConversationCommands
     * corresponding to the branch in Conversation named BRANCH. */
    @SuppressWarnings("unchecked")
    public void setBranch(String branchName) {
        LinkedList<ConversationCommand> branch = currentConversation.getBranch(branchName);
        Object b = branch.clone();
        if (b instanceof LinkedList) {
            currentBranch = (LinkedList<ConversationCommand>) b;
        }
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
        return remainingText.isEmpty();
    }
    /** Sets the remaining text to be displayed to TEXT. */
    public void setRemainingText(String text){
        remainingText = text;
        remainingTextNoTags = removeTags(text);
        textboxLabel.setText("");
        textboxLabel.invalidate();
    }
    /** Sets the current speaking character to the one represented by CHARACTER. */
    public void setSpeaker(CharacterSprite character) {
        currentSpeaker = character;
    }
    /** Updates the speakerLabel to TEXT. */
    public void setSpeakerName(String text){
        if (text.isEmpty()) {
            speakerLabel.setVisible(false);
        } else {
            speakerLabel.setVisible(true);
            speakerLabel.setText(text + "  ");
            speakerLabel.setSize(speakerLabel.getPrefWidth(), speakerLabel.getPrefHeight());
            speakerLabel.invalidate();
        }
    }
    /** Sets the textTimerDelay to DELAY. */
    public void setTextTimer(int delay){
        textTimerDelay = delay;
    }
    /** Sets whether the textbox and speaker label should SHOW. */
    public void setTextBoxShowing(boolean show){
        textboxLabel.setVisible(show);
        speakerLabel.setVisible(show);
    }
    /** Set whether to display all text to DISPLAY. */
    public void setDisplayAll(boolean display) {
        displayAll = display;
    }

    /** Choices Code */

    /** Sets whether choices should SHOW for the player. */
    public void setChoiceShowing(boolean show) {
        choiceShowing = show;
    }
    /** Sets choice number CHOICE to CHOICE NAME. */
    public void setChoice(int choice, String choiceName) {
        choiceButtons[choice].setVisible(!choiceName.isEmpty());
        choiceButtons[choice].setText(choiceName);
    }
    /** Sets choice number CHOICE to execute the list of ConversationCommands COMMANDS. */
    public void setChoiceCommand(int choice, List<ConversationCommand> commands) {
        choiceCommands[choice] = commands;
    }
    /** Adds to the front of the command queue the list of commands corresponding to CHOICE
     * and send a CompleteEvent to the current command. */
    public void processChoice(int choice) {
        choiceShowing = false;
        for (TextButton b : choiceButtons) {
            b.setVisible(false);
        }
        List<ConversationCommand> commands = choiceCommands[choice];
        insertCommands(commands);
        currentCommand.complete(CompleteEvent.choice());
    }

    /** Tags Code */
    /** Returns String S with all tags removed. */
    private String removeTags(String s){
        boolean inTag = false;
        String output = "";
        for (int i = 0; i < s.length(); i++){
            char newChar = s.charAt(i);
            if (!inTag){ //not in tag so keep char unless starting tag
                if (newChar != '[') //todo, allow escape character [[?
                    output += newChar;
                else
                    inTag = true;
            }
            else{ //in tag so don't keep char. If closing bracket end tag
                if (newChar == ']')
                    inTag = false;
            }
        }
        return output;
    }
    /** Returns the tag in S. */ //TODO Update this and rework tags
    private String getTag(String s){
        if (s.length() == 0 || s.charAt(0) != '[')
            return null;
        else{
            String tag = "";
            for (int i = 0; i < s.length(); i++){
                char nextChar = s.charAt(i);
                tag += nextChar;
                if (nextChar == ']')
                    break;
            }
            return tag;
        }
    }

    /** Handles a touch on the screen and passes an Input CompleteEvent to the current
     * ConversationCommand. If someone is currently speaking, instead set displayAll to true
     * first. */
    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button, boolean justPressed) {
        if (justPressed) {
            if (!doneSpeaking()) {
                displayAll = true;
            }
            if (currentCommand != null) {
                currentCommand.complete(CompleteEvent.input());
            }
        }
    }
}
