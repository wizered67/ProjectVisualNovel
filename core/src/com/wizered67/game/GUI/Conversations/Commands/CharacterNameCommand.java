package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * A ConversationCommand that changes the displayed name of a CharacterSprite.
 * @author Adam Victor
 */
public class CharacterNameCommand implements ConversationCommand {
    /** The name of the CharacterSprite to rename. */
    private String character;
    /** The new display name for the CharacterSprite. */
    private String newName;

    /** Creates a new CharacterNameCommand that changes the CharacterSprite named WHO to have
     * display name NAME when executed.
     */
    public CharacterNameCommand(String who, String name) {
        character = who;
        newName = name;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setKnownName(newName);
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
            xmlWriter.element("setname")
                    .attribute("name", character)
                    .attribute("displayname", newName)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterNameCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("name");
        String newName = element.getAttribute("displayname");
        return new CharacterNameCommand(name, newName);
    }
}
