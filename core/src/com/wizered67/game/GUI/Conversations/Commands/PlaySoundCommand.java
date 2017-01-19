package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GameManager;

import java.io.IOException;

/**
 * A ConversationCommand that plays a sound effect.
 * @author Adam Victor
 */
public class PlaySoundCommand implements ConversationCommand {
    /** Identifier of the sound to play. */
    private String sound;

    /** No arguments constructor. */
    public PlaySoundCommand() {
        sound = "";
    }
    /** Creates a new PlaySoundCommand that plays the sound S when executed. */
    public PlaySoundCommand(String s) {
        sound = s;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        if (GameManager.assetManager().isLoaded(sound)) {
            Sound s = GameManager.assetManager().get(sound, Sound.class);
            s.play();
        } else {
            GameManager.error("No sound loaded: " + sound);
        }
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
            xmlWriter.element("playsound")
                    .attribute("name", sound)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static PlaySoundCommand makeCommand(XmlReader.Element element) {
        String id = element.getAttribute("id");
        return new PlaySoundCommand(id);
    }
}
