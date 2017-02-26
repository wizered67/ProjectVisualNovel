package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.scripting.ScriptManager;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ConversationCommand that displays a message to the window
 * in ConversationController. Can also have subcommands that execute in
 * the middle of the message.
 * @author Adam Victor
 */
public class MessageCommand implements ConversationCommand {
    /** List of strings of speakers. Each speaker corresponds to a message of the same index. */
    private ArrayList<String> speakers;
    /** Whether this MessageCommand is done and the next ConversationCommand
     * should be executed. Begins as false. */
    private boolean done;
    /** The default speaking sound to be played. */
    private static final String DEFAULT_SOUND = "talksoundmale";
    /** List of all blocks of text to be displayed. Each String in the list is
     * a separate dialogue box. */
    private ArrayList<String> storedText;
    /** Reference to the ConversationController that is processing this MessageCommand. Used
     * to set text and speaker and other message attributes. */
    private ConversationController conversationController;
    /** The ConversationCommand embedded in this message currently being executed. */
    private ConversationCommand currentSubcommand;
    /** Whether it is necessary to wait for input to proceed to the next command.
     * If false, automatically go to next command once all text is shown. Assumed to
     * be true if not specified in conversation file. */
    private ArrayList<Boolean> waitForInput;
    /** Index of the message currently being displayed. */
    private int index;
    /** Regex pattern used to match variables in messages when language is specified. */
    public transient static final Pattern SCRIPT_VARIABLE_LANGUAGE_PATTERN = Pattern.compile("@v\\{(\\S+?)_(\\S+?)\\}");
    /** Regex pattern used to match variables in messages when no language is specified. */
    public transient static final Pattern SCRIPT_VARIABLE_PATTERN = Pattern.compile("@v\\{(\\S+?)\\}");
    /** Regex pattern used to match messages with a speaker. */
    public transient static final Pattern SPEAKER_MESSAGE_PATTERN = Pattern.compile("\\s*(\\S*)\\s*(?<!\\\\):(.+)");
    public transient static final Pattern COMMAND_PATTERN = Pattern.compile("@c\\{(\\S+?)\\}");

    /** No arguments constructor. */
    public MessageCommand() {
        speakers = null;
        done = true;
        storedText = null;
        waitForInput = null;
    }
    /** Creates a new MessageCommand with speakers in SPEAKER list. WAIT is a list
     * that determines whether player input is necessary before moving on to the next
     * command or if message ending is enough. TEXT contains stored text.
     */
    public MessageCommand(ArrayList<String> text, ArrayList<String> speaker, ArrayList<Boolean> wait) {
        done = false;
        storedText = text;
        speakers = speaker;
        waitForInput = wait;
    }

    /** Returns the speaker of this message. */
    public String getSpeaker() {
        return speakers.get(index);
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @SuppressWarnings("unchecked")
    public void execute(ConversationController message) {
        conversationController = message;
        index = 0;
        updateText();
        conversationController.setTextBoxShowing(true);
        currentSubcommand = null;
        done = false;
    }
    /** Increments index of current text being displayed as well as speaker. */
    public void updateText() {
        String nextText = storedText.get(index);
        conversationController.setText(nextText);
        SceneCharacter characterSpeaking = conversationController.sceneManager().getCharacterByIdentifier(getSpeaker());
        conversationController.setSpeaker(characterSpeaking);
        conversationController.setCurrentSpeakerSound(characterSpeaking.getSpeakingSound());
        index += 1;
    }
    /** Whether the text should be updated. False iff there is a current subcommand
     * that is being waited on. */
    public boolean shouldUpdate() {
        return currentSubcommand == null || !currentSubcommand.waitToProceed();
    }
    /** Sets the current subcommand to the one contained in COMMAND STRING. */
    public void setSubcommand(String commandName) {
        ConversationCommand command = conversationController.conversation().getAssignment(commandName);
        if (command != null) {
            currentSubcommand = command;
            currentSubcommand.execute(conversationController);
        }
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        if (waitForInput.get(index - 1)) { //todo make it so text keeps scrolling if !waitForInput.get(index)
            return !done;
        } else {
            return index < storedText.size() || !conversationController.doneSpeaking();
        }
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {
        if (currentSubcommand != null) {
            currentSubcommand.complete(c);
        }
        if (c.type == CompleteEvent.Type.INPUT && conversationController.doneSpeaking()) {
            conversationController.setDisplayAll(false);
            if (index >= storedText.size()) {
                done = true;
            } else {
                updateText();
            }
        }
        if (c.type == CompleteEvent.Type.TEXT) {
            if (index < storedText.size() && !waitForInput.get(index - 1)
                    && (currentSubcommand == null || !currentSubcommand.waitToProceed())) {
                updateText();
                conversationController.setDisplayAll(false);
            }
        }
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        //todo fix?
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static MessageCommand makeCommand(XmlReader.Element element) {
        return makeCommand(element.getText());
    }
    /** Creates a MessageCommand from the text TEXT. */
    public static MessageCommand makeCommand(String text) {
        ArrayList<String> storedText = new ArrayList<String>();
        ArrayList<Boolean> waitToProceed = new ArrayList<Boolean>();
        ArrayList<String> speakers = new ArrayList<String>();
        text = text.replaceAll("\\r", "");
        text = text.replaceAll(COMMAND_PATTERN.toString(), "{EVENT=COMMAND_$1}");
        text = text.replaceAll(SCRIPT_VARIABLE_LANGUAGE_PATTERN.toString(), "{VAR=SCRIPTING_$1_$2}");
        text = text.replaceAll(SCRIPT_VARIABLE_PATTERN.toString(), "{VAR=SCRIPTING_$1}");
        String[] lines = text.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            Matcher matcher = SPEAKER_MESSAGE_PATTERN.matcher(line);
            if (matcher.matches()) {
                String messageText = matcher.group(2).trim().replaceAll("@n", "\n");
                storedText.add(messageText);
                String speakerText = matcher.group(1);
                if (!speakerText.isEmpty() && speakerText.charAt(0) == '!') {
                    waitToProceed.add(false);
                    speakerText = speakerText.substring(1);
                } else {
                    waitToProceed.add(true);
                }
                if (speakerText.trim().isEmpty()) {
                    if (speakers.isEmpty()) {
                        Gdx.app.error("Command Parser", "Text with continued speaker has no previous speaker.");
                    }
                    speakers.add(speakers.get(speakers.size() - 1));
                } else {
                    speakers.add(speakerText);
                }
            } else {
                if (storedText.isEmpty()) {
                    Gdx.app.error("Command Parser", "Trying to add text with no speaker declared.");
                }
                String last = storedText.get(storedText.size() - 1);
                line = line.replaceAll("\\\\:", ":");
                line = line.replaceAll("@n", "\n");
                storedText.set(storedText.size() - 1, last + " " + line);
            }
        }
        return new MessageCommand(storedText, speakers, waitToProceed);
    }
}
