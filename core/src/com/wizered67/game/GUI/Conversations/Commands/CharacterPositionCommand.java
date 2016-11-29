package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * A ConversationCommand that changes the position of an existing CharacterSprite.
 * @author Adam Victor
 */
public class CharacterPositionCommand implements ConversationCommand {
    /** The name of the CharacterSprite to change the position of. */
    private String character;
    /** A Vector2 containing the new position of the CharacterSprite. */
    private Vector2 position;

    /** Creates a new CharacterPositionCommand that moves the CharacterSprite
     * named CHARACTER to POSITION when executed.
     */
    public CharacterPositionCommand(String character, Vector2 position) {
        this.character = character;
        this.position = position;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setPosition(position);
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
            xmlWriter.element("setposition")
                    .attribute("name", character)
                    .attribute("x", position.x)
                    .attribute("y", position.y)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT that is part of CONVERSATION. */
    public static CharacterPositionCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        //String position = element.getAttribute("position");
        float x = element.getFloatAttribute("x");
        float y = element.getFloatAttribute("y");
        return new CharacterPositionCommand(name, new Vector2(x, y)); //TODO fix this
    }
}
