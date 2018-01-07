package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.commands.impl.base.MessageCommand;
import com.wizered67.game.conversations.commands.impl.base.ShowChoicesCommand;
import com.wizered67.game.conversations.commands.impl.scripting.VariableConditionCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;
import com.wizered67.game.conversations.xmlio.ConversationParsingException;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating a ShowChoicesCommand from an XML element.
 *
 * @author Adam Victor
 */
public class ShowChoicesCommandFactory implements ConversationCommandFactory<ShowChoicesCommand> {
    private final static ShowChoicesCommandFactory INSTANCE = new ShowChoicesCommandFactory();

    public static ShowChoicesCommandFactory getInstance() {
        return INSTANCE;
    }

    private ShowChoicesCommandFactory() {
    }

    @Override
    public ShowChoicesCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
        int numChoices = ShowChoicesCommand.MAX_CHOICES;
        String[] textChoices = new String[numChoices];
        @SuppressWarnings("unchecked")
        List<ConversationCommand>[] commandChoices = new List[numChoices];
        for (int i = 0; i < numChoices; i += 1) {
            commandChoices[i] = new ArrayList<ConversationCommand>();
        }
        VariableConditionCommand[] conditions = new VariableConditionCommand[numChoices];
        int choiceNum = -1;
        boolean afterChoice = false;
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element elem = element.getChild(i);
            if (elem.getName().equalsIgnoreCase("text")) { //block of text. Iterate through to separate into message/choices
                //also handle case where condition directly follows choice
                String text = elem.getText().replaceAll("\\r", "");
                String[] lines = text.split("\\n");
                String messages = "";
                for (String line : lines) {
                    line = line.trim();
                    if (line.charAt(line.length() - 1) == ':' && line.charAt(line.length() - 2) != '\\') {
                        if (!messages.isEmpty()) {
                            addMessageCommand(messages, choiceNum, commandChoices);
                        }
                        messages = "";
                        choiceNum += 1;
                        textChoices[choiceNum] = line.substring(0, line.length() - 1);
                        afterChoice = true;
                    } else {
                        line = line.replaceAll("\\\\:", ":");
                        messages += (" " + line + "\n");
                        afterChoice = false;
                    }
                }
                if (!messages.isEmpty()) {
                    addMessageCommand(messages, choiceNum, commandChoices);
                }
            } else {
                ConversationCommand c = null;
                try {
                    c = loader.getCommand(elem);
                    if (afterChoice && c instanceof VariableConditionCommand) {
                        conditions[choiceNum] = (VariableConditionCommand) c;
                    } else {
                        List<ConversationCommand> commands = commandChoices[choiceNum];
                        commands.add(c);
                    }
                } catch (ConversationParsingException e) {
                    e.printStackTrace();
                }
                afterChoice = false;
            }
        }
        return new ShowChoicesCommand(textChoices, commandChoices, conditions);
    }

    /** Helper method for adding a MessageCommand with content MESSAGES to command list of choice CHOICENUM. */
    private static void addMessageCommand(String messages, int choiceNum, List<ConversationCommand>[] commandChoices) {
        if (choiceNum < 0) {
            Gdx.app.error("Command Parser", "Trying to add message before choice in choices block.");
        } else {
            commandChoices[choiceNum].add(MessageCommand.makeCommand(messages));
        }
    }
}