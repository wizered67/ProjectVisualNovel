package com.wizered67.game.conversations.commands.impl.audio;

import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

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
    /** MusicManager index to play the music on. */
    private int index;

    /** No arguments constructor. Used for serialization. */
    public PlayMusicCommand() {
        music = "";
        loops = false;
        type = Type.PLAY;
        volume = 1;
        index = 0;
    }
    /** Creates a PlayMusicCommand that plays the music with identifier M and
     * sets its loop status to L when executed.
     */
    public PlayMusicCommand(String musicName, boolean loops, float volume, int index) {
        music = musicName;
        this.loops = loops;
        this.volume = volume;
        this.index = index;
        type = Type.PLAY;
    }

    /** Creates a PlayMusicCommand that pauses or resumes music when executed.
     * T is an enum corresponding to type.
     */
    public PlayMusicCommand(Type t, int index) { //pause/resume command, 1 for pause, 2 for resume
        type = t;
        this.index = index;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        if (type == Type.PAUSE) {
            GameManager.musicManager().pauseMusic(index);
            return;
        } else if (type == Type.RESUME) {
            GameManager.musicManager().resumeMusic(index);
            return;
        }
        if (music.equals("")) {
            GameManager.musicManager().stopMusic(index);
            return;
        }
        GameManager.musicManager().playMusic(music, loops, volume, index);
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
    /** The type of action this command does ie playing, pausing, or resuming music. */
    public enum Type {
        PLAY, PAUSE, RESUME
    }
}
