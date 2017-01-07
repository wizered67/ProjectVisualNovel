import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.wizered67.game.GUI.Conversations.Commands.*;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;
import com.wizered67.game.GUI.GUIManager;
import junitx.util.PrivateAccessor;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
@RunWith(GdxTestRunner.class)
public class UnitTests {
    private static Conversation test1;

    @BeforeClass
    public static void load() {
        ConversationLoader loader = new ConversationLoader();
        GUIManager guiManager = new GUIManager();
        test1 = loader.loadConversation(Gdx.files.internal("test1.conv"));
    }

    @Test
    public void testFilesExist() {
        assertTrue("test1.conv doesn't exist.", Gdx.files.internal("test1.conv").exists());
    }

    @Test
    public void parsingTest() {
        assertNotNull("Test1 Conversation is null.", test1);
        LinkedList<ConversationCommand> branch = test1.getBranch("BranchOne");
        assertNotNull("BranchOne is null", branch);
        assertTrue("First command should be a message", branch.get(0) instanceof MessageCommand);
        assertTrue("Second command should be debug", branch.get(1) instanceof DebugCommand);
        assertTrue("Third command should be a message", branch.get(2) instanceof MessageCommand);
        assertTrue("Fourth command should be changebranch", branch.get(3) instanceof ChangeBranchCommand);
        MessageCommand message = (MessageCommand) branch.get(0);
        verifyMessage(message, Arrays.asList("text one"), Arrays.asList("c1"), Arrays.asList(true));
        message = (MessageCommand) branch.get(2);
        verifyMessage(message, Arrays.asList("text two text three", "text four", "text five"),
                Arrays.asList("c2", "c2", "c3"), Arrays.asList(true, false, true));

        branch = test1.getBranch("branchtwo");
        assertNotNull("BranchTwo is null", branch);
        ShowChoicesCommand choices = (ShowChoicesCommand) branch.get(0);
        verifyChoices(choices, new String[] {"choice one", "choice two", "choice three", "choice four"},
                new boolean[] {false, false, false, false});
        List<ConversationCommand>[] choicesCommands = getChoicesCommands(choices);
        verifyMessage((MessageCommand)choicesCommands[0].get(0), Arrays.asList("text six: text seven: text e:ight.", "text nine.", "text ten"),
                Arrays.asList("c1", "c2", "c2"), Arrays.asList(true, false, true));
        verifyMessage((MessageCommand)choicesCommands[1].get(0), Arrays.asList("text eleven text twelve"),
                Arrays.asList("c1"), Arrays.asList(true));
        verifyMessage((MessageCommand)choicesCommands[2].get(0), Arrays.asList("text thirteen text fourteen", "text fifteen"),
                Arrays.asList("c2", "c3"), Arrays.asList(false, false));
        verifyMessage((MessageCommand)choicesCommands[3].get(0), Arrays.asList("text sixteen"),
                Arrays.asList("c1"), Arrays.asList(true));

        branch = test1.getBranch("branchthree");
        assertNotNull("BranchThree is null.", branch);
        choices = (ShowChoicesCommand) branch.get(0);
        verifyChoices(choices, new String[] {"choice one", "choice two", "choice three", null},
                new boolean[] {true, false, false, false});
        choicesCommands = getChoicesCommands(choices);
        verifyMessage((MessageCommand) choicesCommands[0].get(1), Arrays.asList("text seventeen"),
                Arrays.asList("c1"), Arrays.asList(true));
        assertTrue(choicesCommands[1].get(0) instanceof DebugCommand);
        assertTrue(choicesCommands[1].get(1) instanceof VariableConditionCommand);
        assertEquals(3, choicesCommands[1].size());
        verifyMessage((MessageCommand) choicesCommands[2].get(0),
                Arrays.asList("text eighteen"), Arrays.asList("c1"), Arrays.asList(false));

        List<ConversationCommand> ifCommands = getConditionCommands((VariableConditionCommand) branch.get(1));
        verifyMessage((MessageCommand) ifCommands.get(0),
                Arrays.asList("text nineteen", "text twenty"), Arrays.asList("c1", "c1"),
                Arrays.asList(true, false));
        assertTrue(ifCommands.get(1) instanceof DebugCommand);
    }
    @SuppressWarnings("unchecked")
    public void verifyMessage(MessageCommand message, List<String> expectedText,
                              List<String> expectedSpeakers, List<Boolean> expectedWait) {
        try {
            ArrayList<String> text = (ArrayList) PrivateAccessor.getField(message, "storedText");
            assertEquals(expectedText, text);
            ArrayList<String> speakers = (ArrayList) PrivateAccessor.getField(message, "speakers");
            ArrayList<Boolean> wait = (ArrayList) PrivateAccessor.getField(message, "waitForInput");
            assertEquals(expectedText, text);
            assertEquals(expectedSpeakers, speakers);
            assertEquals(expectedWait, wait);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyChoices(ShowChoicesCommand choices, String[] expectedText, boolean[] expectedConditions) {
        try {
            String[] choicesText = (String[]) PrivateAccessor.getField(choices, "choicesText");
            assertArrayEquals(expectedText, choicesText);
            VariableConditionCommand[] conditions = (VariableConditionCommand[]) PrivateAccessor.getField(choices, "conditions");
            for (int i = 0; i < expectedConditions.length; i += 1) {
                assertEquals(expectedConditions[i], conditions[i] != null);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public List<ConversationCommand>[] getChoicesCommands(ShowChoicesCommand choices) {
        try {
            return (List<ConversationCommand>[]) PrivateAccessor.getField(choices, "choicesCommands");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<ConversationCommand> getConditionCommands(VariableConditionCommand command) {
        try {
            return (List<ConversationCommand>) PrivateAccessor.getField(command, "commands");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void regexTests() {
        testMessageRegex();
        testVariableRegex();
    }
    @Test
    public void testMessageRegex() {
        String test = "speaker: text";
        testMessageRegex(test, "speaker", "text");

        test = "   speaker   :     text   ";
        testMessageRegex(test, "speaker", "text");

        test = "   sp\\eaker: text";
        testMessageRegex(test, "sp\\eaker", "text");

        test = "speaker\\: text";
        testMessageRegex(test, false);

        test = "longer_speaker: text with many words";
        testMessageRegex(test, "longer_speaker", "text with many words");

        test = "   : text";
        testMessageRegex(test, "", "text");
    }

    public void testMessageRegex(String input, String speaker, String text) {
        Matcher matcher = MessageCommand.speakerMessagePattern.matcher(input);
        assertTrue("Did not match for: " + input, matcher.matches());
        assertEquals(speaker, matcher.group(1).trim());
        assertEquals(text, matcher.group(2).trim());
    }

    public void testMessageRegex(String input, boolean shouldMatch) {
        Matcher matcher = MessageCommand.speakerMessagePattern.matcher(input);
        assertEquals(shouldMatch, matcher.matches());
    }
    @Test
    public void testVariableRegex() {
        String test = "@v{language_name}";
        testVariableRegex(test, "language", "name");
    }

    public void testVariableRegex(String input, String language, String name) {
        Matcher matcher = MessageCommand.scriptVariablePattern.matcher(input);
        assertTrue("Did not match for: " + input, matcher.matches());
        assertEquals(language, matcher.group(1).trim());
        assertEquals(name, matcher.group(2).trim());
    }
}