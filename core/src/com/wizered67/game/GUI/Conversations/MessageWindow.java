package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.wizered67.game.GameManager;
import com.wizered67.game.Inputs.Controllable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by Adam on 10/23/2016.
 */
public class MessageWindow implements Controllable{
    private String remainingText = "";
    private String remainingTextNoTags = "";
    private int textTimer = 4;
    private int numLines = 0;
    private final int LEFT_PADDING = 10;
    private boolean dummyTagAdded = false;
    private int textTimerDelay = 1;
    private Label textboxLabel;
    private Label speakerLabel;
    private Queue<ConversationCommand> currentBranch;
    private boolean waitingForInput = false;

    public MessageWindow(Label textbox, Label speaker) {
        System.out.println(numNewLineTags("\ntest\n"));
        System.out.println(numNewLineTags("\nte\nst\n"));
        System.out.println(numNewLineTags("\n\ntest\n\n"));
        System.out.println(numNewLineTags("test\nthis\n\nis\na\ntest"));
        System.out.println(numNewLineTags("test"));
        textboxLabel = textbox;
        speakerLabel = speaker;
        Conversation testConversation = new Conversation();
        LinkedList<ConversationCommand> branchOne = new LinkedList<ConversationCommand>();
        //branchOne.add(new Message("Test", "This is a [\n] test so [\n] allow me to test it [\n] please. [\n][\n] please."));
        branchOne.add(new Message("Adam", "Yo bruh, this is a message that we're currently 'testing' here at the lab. " +
                "Our top text scientists are attempting to determine if it properly scrolls. We are taking " +
                "notes and keeping our observations in a top secret log!           "));
        branchOne.add(new Message("Donald", "Yep, that's right! I'll tell you folks, we have the best scrolling text boxes. The best!"));
        branchOne.add(new Message("Christine", "Wait... why is Donald Trump here? Adam wtf, really? Really?"));
        branchOne.add(new Message("Adam", "I thought you invited him!"));
        branchOne.add(new Message("Christine", "......."));
        branchOne.add(new DebugCommand("This is a test 1."));
        branchOne.add(new Message("Adam", "......."));
        branchOne.add(new DebugCommand("This is another test."));
        branchOne.add(new DebugCommand("And another."));
        branchOne.add(new DebugCommand("And yet another."));
        branchOne.add(new Message("Adam", "I blame the American voters."));
        branchOne.add(new ChangeBranchCommand(testConversation, "Branch 2"));
        testConversation.addBranch("Main Branch", branchOne);

        LinkedList<ConversationCommand> branchTwo = new LinkedList<ConversationCommand>();
        branchTwo.add(new Message("Narrator", "So you're probably wondering what just happened. Why is there a narrator guy now? Well there's a simple explanation for that!"));
        branchTwo.add(new DebugCommand("yoooo branch 2 whattup yer boiii"));
        branchTwo.add(new Message("Christine", "We really weren't..."));
       // branchTwo.add(new Message("Narrator", "It's because you've entered... [\n][\n][\n]     BRANCH TWO"));
        branchTwo.add(new Message("Adam", "Can we just go back to the first one now please?"));
        branchTwo.add(new Message("Narrator", "Fine! But I'll be back!"));
        branchTwo.add(new ChangeBranchCommand(testConversation, "Main Branch"));
        testConversation.addBranch("Branch 2", branchTwo);

        setBranch(branchOne);
        //remainingText =
       //remainingTextNoTags = removeTags(remainingText);
        GameManager.getMainInputProcessor().register(this);
    }

    public void update(float deltaTime) {
        while (!waitingForInput && currentBranch.size() != 0) {
            nextCommand();
        }
        updateText(deltaTime);
    }

    public void updateText(float deltaTime) {
        if (doneSpeaking()) {
            return;
        }
        textTimer = Math.max(textTimer - 1, 0);
        if (textTimer <= 0) {
            boolean textAdded = false;
            boolean tagAdded = false;
            String newText = null;
            String originalText = textboxLabel.getText().toString();
            if (dummyTagAdded){
                dummyTagAdded = false;
            }
            while (!textAdded) {
                String[] words = remainingTextNoTags.split(" ");
                String nextWord = "";
                if (words.length > 0)
                    nextWord = words[0];

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
                        newText += remainingText.charAt(0);
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
                if (closeTag != null){
                    //newText += closeTag;
                    //remainingText = remainingText.substring(closeTag.length());
                    dummyTagAdded = false;
                }
                else{
                    dummyTagAdded = true;
                    //newText += "[]";
                }
            }
            textTimer = textTimerDelay;
            textboxLabel.setText(newText);
            textboxLabel.layout();
            numLines = textboxLabel.getGlyphLayout().runs.size;
            textboxLabel.invalidate();
        }
    }

    private int numNewLineTags(String word) {
        return word.length() - word.replaceAll("\n", "").length();
    }

    public void nextCommand() {
        if (currentBranch.size() != 0) {
            ConversationCommand command = currentBranch.remove();
            command.execute(this);
            System.out.println("Executed command: " + command.toString());
            waitingForInput = command.waitForInput();
            /*
            Message message = currentBranch.remove();
            setSpeaker(message.getSpeaker());
            setRemainingText(message.getText());

        } else {
            setTextBoxShowing(false);
        }
        */
        } else {
            setTextBoxShowing(false);
        }
    }

    @SuppressWarnings("unchecked")
    public void setBranch(LinkedList<ConversationCommand> branch) {
        Object b = branch.clone();
        if (b instanceof LinkedList) {
            currentBranch = (LinkedList<ConversationCommand>) b;
        }
    }

    public boolean doneSpeaking() {
        return remainingText.isEmpty();
    }

    public void setRemainingText(String text){
        remainingText = text;
        remainingTextNoTags = removeTags(text);
        textboxLabel.setText("");
        textboxLabel.invalidate();
    }

    public void setSpeaker(String text){
        speakerLabel.setText(text + "  ");
        speakerLabel.setSize(speakerLabel.getPrefWidth(), speakerLabel.getPrefHeight());
        speakerLabel.invalidate();
    }

    public void setTextTimer(int delay){
        textTimerDelay = delay;
    }

    public void setTextBoxShowing(boolean show){
        textboxLabel.setVisible(show);
        speakerLabel.setVisible(show);
    }

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

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button, boolean justPressed) {
        if (justPressed) {
            if (!doneSpeaking()) {
                textboxLabel.setText(textboxLabel.getText().toString() + remainingText);
                remainingText = "";
            } else if (waitingForInput) {
                nextCommand();
            }
        }
    }
}
