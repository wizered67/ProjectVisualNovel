package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GameManager;

import java.io.IOException;

/**
 * A ConversationCommand that plays, pauses, or resumes background music.
 * @author Adam Victor
 */
public class PlayMusicCommand implements ConversationCommand {
    /** Identifier of the music to play, if play command. */
    private String music;
    /** Whether the music should loop, if play command. */
    private boolean loops;
    /** If pause is 1, acts as a pause command. If pause is 2, acts as a resume command.
     * When 0 as normal, just plays the music. */
    private Type type;
    /** The volume to play the sound at, between 0 and 1. */
    private float volume;

    /** No arguments constructor. Used for serialization. */
    public PlayMusicCommand() {
        music = "";
        loops = false;
        type = Type.PLAY;
        volume = 1;
    }
    /** Creates a PlayMusicCommand that plays the music with identifier M and
     * sets its loop status to L when executed.
     */
    public PlayMusicCommand(String m, boolean l, float v) {
        music = m;
        loops = l;
        volume = v;
        type = Type.PLAY;
    }

    /** Creates a PlayMusicCommand that pauses or resumes music when executed.
     * T is an enum corresponding to type.
     */
    public PlayMusicCommand(Type t) { //pause/resume command, 1 for pause, 2 for resume
        type = t;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        if (type == Type.PAUSE) {
            GameManager.musicManager().pauseMusic();
            return;
        } else if (type == Type.RESUME) {
            GameManager.musicManager().resumeMusic();
            return;
        }
        if (music.equals("")) {
            GameManager.musicManager().stopMusic();
            return;
        }
        GameManager.musicManager().playMusic(music, loops, volume);
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
            if (type == Type.PLAY) {
                xmlWriter.element("playmusic")
                        .attribute("name", music)
                        .attribute("loop", loops)
                        .pop();
            } else if (type == Type.PAUSE) {
                xmlWriter.element("pausemusic").pop();
            } else if (type == Type.RESUME) {
                xmlWriter.element("resumemusic").pop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static PlayMusicCommand makeCommand(XmlReader.Element element) {
        if (element.getName().equals("pausemusic")) {
            return new PlayMusicCommand(Type.PAUSE);
        }
        if (element.getName().equals("resumemusic")) {
            return new PlayMusicCommand(Type.RESUME);
        }
        String id = element.getAttribute("id");
        boolean loop = element.getBooleanAttribute("loop", false);
        float volume = element.getFloatAttribute("volume", 1);
        return new PlayMusicCommand(id, loop, volume);
    }
    /** The type of action this command does ie playing, pausing, or resuming music. */
    private enum Type {
        PLAY, PAUSE, RESUME
    }
}
