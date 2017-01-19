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
    private int pause;

    /** No arguments constructor. Used for serialization. */
    public PlayMusicCommand() {
        music = "";
        loops = false;
        pause = 0;
    }
    /** Creates a PlayMusicCommand that plays the music with identifier M and
     * sets its loop status to L when executed.
     */
    public PlayMusicCommand(String m, boolean l) {
        music = m;
        loops = l;
        pause = 0;
    }

    /** Creates a PlayMusicCommand that pauses or resumes music when executed. SHOULDPAUSE is 1 for
     * pausing and 2 for resuming.
     */
    public PlayMusicCommand(int shouldPause) { //pause/resume command, 1 for pause, 2 for resume
        pause = shouldPause;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        if (pause != 0) {
            if (pause == 1) {
                GameManager.musicManager().pauseMusic();
            } else {
                if (pause == 2) {
                    GameManager.musicManager().resumeMusic();
                }
            }
            return;
        }
        if (music.equals("")) {
            GameManager.musicManager().stopMusic();
            return;
        }
        if (GameManager.assetManager().isLoaded(music)) {
            Music m = GameManager.assetManager().get(music, Music.class);
            GameManager.musicManager().playMusic(m, music, loops);
            GameManager.musicManager().setVolume(0.8f);
        } else {
            GameManager.error("No sound loaded: " + music);
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
            if (pause == 0) {
                xmlWriter.element("playmusic")
                        .attribute("name", music)
                        .attribute("loop", loops)
                        .pop();
            } else if (pause == 1) {
                xmlWriter.element("pausemusic").pop();
            } else if (pause == 2) {
                xmlWriter.element("resumemusic").pop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static PlayMusicCommand makeCommand(XmlReader.Element element) {
        String id = element.getAttribute("id");
        boolean loop = element.getBoolean("loop", false);
        return new PlayMusicCommand(id, loop);
    }
}
