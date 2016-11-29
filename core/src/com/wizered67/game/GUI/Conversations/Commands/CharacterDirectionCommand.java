package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * A ConversationCommand that changes an existing CharacterSprite's direction.
 * Mainly intended for debug use.
 * @author Adam Victor
 */
public class CharacterDirectionCommand implements ConversationCommand {
    /** Name of the CharacterSprite to change the direction of. */
    String character;
    /** New direction for the CharacterSprite to face. */
    int direction;

    /** Creates a new CharacterDirectionCommand that changes the CharacterSprite
     * NAME's direction to DIR when executed.
     */
    public CharacterDirectionCommand(String name, int dir) {
        character = name;
        direction = dir;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setDirection(direction);
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("setdirection")
                    .attribute("name", character)
                    .attribute("direction", direction)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterDirectionCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("name");
        int direction = element.getInt("direction", 1);
        return new CharacterDirectionCommand(name, direction);
    }
}
