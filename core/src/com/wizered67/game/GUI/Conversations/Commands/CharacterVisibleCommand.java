package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * A ConversationCommand that sets the visibility of a CharacterSprite.
 * @author Adam Victor
 */
public class CharacterVisibleCommand implements ConversationCommand {
    /** The name of the CharacterSprite to modify the visibility of. */
    String character;
    /** Whether the CharacterSprite should be visible. */
    boolean show;

    /** Creates a new CharacterVisibleCommand that sets the CharacterSprite
     * NAME's visibility to VISIBLE when executed.
     */
    public CharacterVisibleCommand(String name, boolean visible) {
        character = name;
        show = visible;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setVisible(show);
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
            xmlWriter.element("setvisible")
                    .attribute("name", character)
                    .attribute("visible", show)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT that is part of CONVERSATION. */
    public static CharacterVisibleCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        boolean visible = element.getBoolean("visible", false);
        return new CharacterVisibleCommand(name, visible);
    }
}
