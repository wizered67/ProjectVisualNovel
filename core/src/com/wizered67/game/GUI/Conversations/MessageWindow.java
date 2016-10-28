package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.GameManager;
import com.wizered67.game.Inputs.Controllable;

import java.util.LinkedList;
import java.util.Queue;

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
    private int textTimerDelay = 2;
    private Label textboxLabel;
    private Label speakerLabel;
    private TextButton[] choiceButtons;
    private ConversationCommand[] choiceCommands;
    private Queue<ConversationCommand> currentBranch;
    private ConversationCommand currentCommand;
    private boolean playSoundNow = true;
    private String currentSpeakerSound;
    private boolean choiceShowing = false;
    private AnimationManager animationManager;

    public MessageWindow(Label textbox, Label speaker, TextButton[] choices) {
        System.out.println(numNewLineTags("\ntest\n"));
        System.out.println(numNewLineTags("\nte\nst\n"));
        System.out.println(numNewLineTags("\n\ntest\n\n"));
        System.out.println(numNewLineTags("test\nthis\n\nis\na\ntest"));
        System.out.println(numNewLineTags("test"));
        textboxLabel = textbox;
        speakerLabel = speaker;
        choiceButtons = choices;
        choiceCommands = new ConversationCommand[choiceButtons.length];
        animationManager = new AnimationManager(this);
        Conversation testConversation = new Conversation();
        LinkedList<ConversationCommand> branchOne = new LinkedList<ConversationCommand>();
        //branchOne.add(new MessageCommand("Test", "This is a [\n] test so [\n] allow me to test it [\n] please. [\n][\n] please."));
        branchOne.add(new MessageCommand("Adam", "Yo bruh, this is a message that we're currently 'testing' here at the lab. " +
                "Our top text scientists are attempting to determine if it properly scrolls. We are taking " +
                "notes and keeping our observations in a top secret log!           "));
        branchOne.add(new PlayMusicCommand("Music/crossexamination.mp3", true));
        branchOne.add(new MessageCommand("Donald", "Yep, that's right! I'll tell you folks, we have the best scrolling text boxes. The best!"));
        branchOne.add(new PlaySoundCommand("Sounds/intense.wav"));
        branchOne.add(new MessageCommand("Christine", "Wait... why is Donald Trump here? Adam wtf, really? Really?", "talksoundfemale"));
        branchOne.add(new MessageCommand("Adam", "I thought you invited him!"));
        branchOne.add(new MessageCommand("Christine", ".......", "talksoundfemale"));
        branchOne.add(new DebugCommand("This is a test 1."));
        branchOne.add(new MessageCommand("Adam", "......."));
        branchOne.add(new DebugCommand("This is another test."));
        branchOne.add(new DebugCommand("And another."));
        branchOne.add(new DebugCommand("And yet another."));
        branchOne.add(new MessageCommand("Adam", "I blame the American voters."));
        branchOne.add(new ChangeBranchCommand(testConversation, "Branch 2"));
        testConversation.addBranch("Main Branch", branchOne);

        LinkedList<ConversationCommand> branchTwo = new LinkedList<ConversationCommand>();
        branchTwo.add(new MessageCommand("Narrator", "So you're probably wondering what just happened. Why is there a narrator guy now? Well there's a simple explanation for that!"));
        branchTwo.add(new DebugCommand("yoooo branch 2 whattup yer boiii"));
        branchTwo.add(new MessageCommand("Christine", "We really weren't...", "talksoundfemale"));
        branchTwo.add(new MessageCommand("Narrator", "It's because you've entered... BRANCH TWO!"));
        branchTwo.add(new PreloadCommand("Music/Pursuit.mp3", Music.class));
        String[] text = new String[] {"Ask to return", "Beg to return", "Let Christine handle it", "Accuse of murder"};
        ConversationCommand[] commands = new ConversationCommand[4];
        commands[0] = new ChangeBranchCommand(testConversation, "Ask Branch");
        commands[1] = new ChangeBranchCommand(testConversation, "Beg Branch");
        commands[2] = new ChangeBranchCommand(testConversation, "Christine Branch");
        commands[3] = new CommandSequence(new PlaySoundCommand("Sounds/intense.wav"), new PlayMusicCommand("Music/Pursuit.mp3", true),
                new ChangeBranchCommand(testConversation, "Accuse Branch"));
        ShowChoicesCommand choice = new ShowChoicesCommand(text, commands);
        branchTwo.add(choice);
        testConversation.addBranch("Branch 2", branchTwo);

        LinkedList<ConversationCommand> askBranch = new LinkedList<ConversationCommand>();
        askBranch.add(new MessageCommand("Adam", "Can we just go back to the first one now please?"));
        askBranch.add(new MessageCommand("Narrator", "Fine! But I'll be back!"));
        askBranch.add(new ChangeBranchCommand(testConversation, "Main Branch"));
        testConversation.addBranch("Ask Branch", askBranch);

        LinkedList<ConversationCommand> begBranch = new LinkedList<ConversationCommand>();
        begBranch.add(new MessageCommand("Adam", "Please for the love of god let us go back!"));
        begBranch.add(new MessageCommand("Narrator", "Wow... I guess I didn't realize you felt so strongly. Alright then."));
        begBranch.add(new ChangeBranchCommand(testConversation, "Main Branch"));
        testConversation.addBranch("Beg Branch", begBranch);

        LinkedList<ConversationCommand> christineBranch = new LinkedList<ConversationCommand>();
        christineBranch.add(new MessageCommand("Christine", "I don't know who you are but I'm leaving. And if you try to follow I'll call the cops! I swear, Donald Trump was better than this!", "talksoundfemale"));
        christineBranch.add(new ChangeBranchCommand(testConversation, "Main Branch"));
        testConversation.addBranch("Christine Branch", christineBranch);

        LinkedList<ConversationCommand> accuseBranch = new LinkedList<ConversationCommand>();
        accuseBranch.add(new SetAnimationCommand("Think", true));
        accuseBranch.add(new MessageCommand("Adam", "Finally, everything's fallen into place... the real murderer..."));
        accuseBranch.add(new PlaySoundCommand("Sounds/intense.wav"));
        accuseBranch.add(new SetAnimationCommand("Point", false));
        accuseBranch.add(new MessageCommand("Adam", "WAS YOU!"));
        accuseBranch.add(new MessageCommand("Narrator", "Wha... What! No way!"));
        accuseBranch.add(new MessageCommand("Christine", "What tipped you off Adam?", "talksoundfemale"));
        accuseBranch.add(new MessageCommand("Adam", "I started having suspicions as soon as Donald Trump appeared. As we both said earlier, neither of us invited him. " +
                "So then how did he end up here? There's only one person with the power to do that. And that's you! The narrator!"));
        accuseBranch.add(new MessageCommand("Narrator", "Hahahahaha... Oh how right you are! I do possess power. More than you could possibly imagine! Now begone! I banish you to the main branch!"));
        accuseBranch.add(new PlaySoundCommand("Sounds/intense.wav"));
        accuseBranch.add(new SetAnimationCommand("Idle", false));
        accuseBranch.add(new PlayMusicCommand("", false));
        accuseBranch.add(new ChangeBranchCommand(testConversation, "Main Branch"));
        testConversation.addBranch("Accuse Branch", accuseBranch);

        setBranch(branchOne);
        //remainingText =
       //remainingTextNoTags = removeTags(remainingText);
        GameManager.getMainInputProcessor().register(this);
    }

    public AnimationManager animationManager() {
        return animationManager;
    }

    public void update(float deltaTime) {
        if (GameManager.assetManager().getQueuedAssets() != 0) {
            GameManager.assetManager().update();
            System.out.println(GameManager.assetManager().getProgress());
        }
        while ((currentCommand == null || !currentCommand.waitToProceed()) && currentBranch.size() != 0) {
            nextCommand();
        }
        updateText(deltaTime);
        animationManager.update(deltaTime);
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
                        char nextChar = remainingText.charAt(0);
                        newText += nextChar;
                        if (nextChar != ' ') {
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

    public void setCurrentSpeakerSound(String sound) {
        currentSpeakerSound = sound;
    }

    private void playTextSound() {
        if (playSoundNow) {
            Sound s = GameManager.assetManager().get("Sounds/" + currentSpeakerSound + ".wav", Sound.class);
            s.play();
        }
        playSoundNow = !playSoundNow;
    }

    private int numNewLineTags(String word) {
        return word.length() - word.replaceAll("\n", "").length();
    }

    public void nextCommand() {
        if (currentBranch.size() != 0) {
            ConversationCommand command = currentBranch.remove();
            command.execute(this);
            System.out.println("Executed command: " + command.toString());
            currentCommand = command;
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

    public void animationComplete(String name) {
        currentCommand.complete(new CompleteEvent(CompleteEvent.Type.ANIMATION_END, name));
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

    /** Choices Code */

    public void setChoiceShowing(boolean show) {
        choiceShowing = show;
    }

    public void setChoice(int number, String choice) {
        choiceButtons[number].setVisible(!choice.equals(""));
        choiceButtons[number].setText(choice);
    }

    public void processChoice(int choice) {
        choiceShowing = false;
        for (int i = 0; i < choiceButtons.length; i += 1) {
            choiceButtons[i].setVisible(false);
        }
        ConversationCommand command = choiceCommands[choice];
        if (command != null) {
            command.execute(this);
        }
        currentCommand.complete(new CompleteEvent(CompleteEvent.Type.CHOICE));
    }

    public void setChoiceCommand(int choice, ConversationCommand command) {
        choiceCommands[choice] = command;
    }

    /** Tags Code */

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
            } else {
                currentCommand.complete(new CompleteEvent(CompleteEvent.Type.INPUT));
            }
            //} else if (waitingForInput && !choiceShowing) {
            //    nextCommand();
            //}
        }
    }
}
