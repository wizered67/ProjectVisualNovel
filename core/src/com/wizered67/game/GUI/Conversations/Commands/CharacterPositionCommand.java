package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

import java.io.IOException;

/**
 * A ConversationCommand that changes the position of an existing CharacterSprite.
 * @author Adam Victor
 */
public class CharacterPositionCommand implements ConversationCommand {
    /** The identifier of the CharacterSprite to change the position of. */
    private String character;
    /** A Vector2 containing the new position of the CharacterSprite. */
    private Vector2 position;

    /** No arguments constructor. */
    public CharacterPositionCommand() {
        character = "";
        position = null;
    }
    /** Creates a new CharacterPositionCommand that moves the CharacterSprite
     * with identifier ID to POSITION when executed.
     */
    public CharacterPositionCommand(String id, Vector2 posn) {
        character = id;
        position = posn;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        CharacterSprite c = conversationController.sceneManager().getCharacterByIdentifier(character);
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
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterPositionCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("id");
        //String position = element.getAttribute("position");
        float x = element.getFloatAttribute("x");
        float y = element.getFloatAttribute("y");
        return new CharacterPositionCommand(name, new Vector2(x, y)); //TODO fix this to use String names
    }
}
