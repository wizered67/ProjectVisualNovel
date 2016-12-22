package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.Scripting.ScriptManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A ConversationCommand that displays a message to the window
 * in ConversationController. Can also have subcommands that execute in
 * the middle of the message.
 * @author Adam Victor
 */
public class MessageCommand implements ConversationCommand {
    /** The name of the speaker of this message. */
    private String speaker;
    /** Whether this MessageCommand is done and the next ConversationCommand
     * should be executed. Begins as false. */
    private boolean done;
    /** The default speaking sound to be played. */
    private static final String DEFAULT_SOUND = "talksoundmale";
    /** Mapping between text shortcuts in the form @c{string} and ConversationCommands. */
    private Map<String, ConversationCommand> assignments;
    /** List of all blocks of text to be displayed. Each String in the list is
     * a separate dialogue box. */
    private LinkedList<String> storedText;
    /** The list of all blocks of text that still needs to be displayed. */
    private LinkedList<String> currentText;
    /** Reference to the ConversationController that is processing this MessageCommand. Used
     * to set text and speaker and other message attributes. */
    private ConversationController conversationController;
    /** The ConversationCommand embedded in this message currently being executed. */
    private ConversationCommand currentSubcommand;
    /** Whether it is necessary to wait for input to proceed to the next command.
     * If false, automatically go to next command once all text is shown. Assumed to
     * be true if not specified in conversation file. */
    private boolean waitForInput;
    /** Regex pattern used to match variables in messages. */
    private transient Pattern scriptVariablePattern = Pattern.compile("@v\\{(.*?)_(.*?)\\}");

    /** No arguments constructor. */
    public MessageCommand() {
        speaker = "";
        done = true;
        assignments = null;
        storedText = null;
        waitForInput = false;
    }
    /** Creates a new MessageCommand with speaker named CHARACTER. If WAIT,
     * does not allow going on to the next command without input. Otherwise,
     * automatically goes to next command once all text is shown.
     * Sets stored text to empty initially but it can be added to later.
     */
    public MessageCommand(String character, boolean wait) {
        speaker = character;
        done = false;
        assignments = new HashMap<String, ConversationCommand>();
        storedText = new LinkedList<String>();
        waitForInput = wait;
    }

    /** Creates a new MessageCommand with speaker named CHARACTER
     * and WAIT as true.
     */
    public MessageCommand(String character) {
        this(character, true);
    }

    /** Returns the speaker of this message. */
    public String getSpeaker() {
        return speaker;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @SuppressWarnings("unchecked")
    public void execute(ConversationController message) {
        conversationController = message;
        currentText = (LinkedList) storedText.clone();
        //conversationController.setRemainingText(currentText.remove());
        updateText();
        CharacterSprite characterSpeaking = conversationController.sceneManager().getCharacterByName(speaker);
        conversationController.setTextBoxShowing(true);
        conversationController.setSpeaker(characterSpeaking);
        conversationController.setCurrentSpeakerSound(characterSpeaking.getSpeakingSound());
        currentSubcommand = null;
        done = false;
    }
    /** Updates the text to be displayed to the next one in the currentText queue. */
    public void updateText() {
        String nextText = currentText.remove();
        Matcher matcher = scriptVariablePattern.matcher(nextText);
        while (matcher.find()) {
            String language = matcher.group(1);
            String variable = matcher.group(2);
            ScriptManager manager = ConversationController.scriptManager(language);
            String variableString = manager.objectToString(manager.getValue(variable));
            nextText = nextText.replaceFirst(scriptVariablePattern.toString(), variableString);
        }
        conversationController.setRemainingText(nextText);
    }
    /** Whether the text should be updated. False iff there is a current subcommand
     * that is being waited on.
     */
    public boolean shouldUpdate() {
        return currentSubcommand == null || !currentSubcommand.waitToProceed();
    }
    /** Sets the current subcommand to the one contained in COMMAND STRING. */
    public void setSubcommand(String commandString) {
        String commandName = commandString.replaceAll("@c\\{(.*)\\}", "$1");
        ConversationCommand command = assignments.get(commandName);
        if (command != null) {
            currentSubcommand = command;
            currentSubcommand.execute(conversationController);
        }
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        if (waitForInput) {
            return !done;
        } else {
            return currentText.size() != 0 || !conversationController.doneSpeaking();
        }
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {
        if (currentSubcommand != null) {
            currentSubcommand.complete(c);
        }
        if (c.type == CompleteEvent.Type.INPUT) {
            if (currentText.size() == 0) {
                done = true;
            } else {
                updateText();
                conversationController.setDisplayAll(false);
            }
        }
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("message")
                    .attribute("speaker", speaker)
                    .attribute("wait", waitForInput);

            for (String assigned : assignments.keySet()) {
                xmlWriter.element("assign")
                        .attribute("name", assigned);
                ConversationCommand command = assignments.get(assigned);
                command.writeXml(xmlWriter);
                xmlWriter.pop();
            }
            for (String text : storedText) {
                xmlWriter.element("text", text).pop();
            }
            xmlWriter.pop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static MessageCommand makeCommand(XmlReader.Element element) {
        String speaker = element.getAttribute("speaker");
        boolean waitForInput = element.getBooleanAttribute("wait", true);
        //String message = element.getAttribute("text");
        MessageCommand message = new MessageCommand(speaker, waitForInput);
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element e = element.getChild(i);
            if (e.getName().equalsIgnoreCase("text")) {
                String text = e.getText();
                text = text.replaceAll("\\n\\s*", " ");
                text = text.replaceAll("\\r", "");
                text = text.replaceAll("\\t", "");
                text = text.replaceAll("@n", "\n");
                message.storedText.addLast(text);
            } else if (e.getName().equalsIgnoreCase("assign")) {
                message.assignments.put(e.getAttribute("name", ""),
                        ConversationLoader.getCommand(e.getChild(0)));
            }
        }
        return message;
    }
}
