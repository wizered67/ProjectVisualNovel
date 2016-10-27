package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.wizered67.game.GUI.Conversations.MessageWindow;
import com.wizered67.game.GameManager;

/**
 * Created by Adam on 10/26/2016.
 */
public class PlayMusicCommand implements ConversationCommand {

    private String music;
    private boolean loops;
    private int pause;

    public PlayMusicCommand(String m, boolean l) {
        music = m;
        loops = l;
        pause = 0;
    }

    public PlayMusicCommand(int shouldPause) { //pause/resume command, 1 for pause, 2 for resume
        pause = shouldPause;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
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
            Gdx.app.log("Asset Error", "No sound loaded: " + music);
        }
    }

    @Override
    public boolean waitForInput() {
        return false;
    }
}
