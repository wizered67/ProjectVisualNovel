package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.scene.SceneCharacter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;

import java.io.IOException;

/**
 * A ConversationCommand that changes the displayed name of a SceneCharacter.
 * @author Adam Victor
 */
public class CharacterNameCommand implements ConversationCommand {
    /** The identifier of the SceneCharacter to rename. */
    private String character;
    /** The new display name for the SceneCharacter. */
    private String newName;

    /** No arguments constructor. */
    public CharacterNameCommand() {
        character = "";
        newName = "";
    }

    /** Creates a new CharacterNameCommand that changes the SceneCharacter with identifier ID to have
     * display name NAME when executed.
     */
    public CharacterNameCommand(String id, String name) {
        character = id;
        newName = name;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        SceneCharacter c = conversationController.sceneManager().getCharacterByIdentifier(character);
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
        String name = element.getAttribute("id");
        String newName = element.getAttribute("displayname");
        return new CharacterNameCommand(name, newName);
    }
}
