package com.wizered67.game;

import com.badlogic.gdx.audio.Music;

/**
 * Created by Adam on 10/26/2016.
 */
public class MusicManager {
    private Music currentMusic;
    private String currentMusicName;
    private boolean paused;

    public MusicManager() {
        currentMusic = null;
        currentMusicName = "";
        paused = false;
    }

    public void playMusic(Music music, String name, boolean loops) {
        if (currentMusicName.equals(name)) {
            if (paused) {
                resumeMusic();
            }
            return;
        }
        paused = false;
        stopMusic();
        currentMusic = music;
        currentMusicName = name;
        music.play();
        setLooping(loops);
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusicName = "";
            currentMusic = null;
        }
    }

    public void resumeMusic() {
        if (currentMusic != null) {
            currentMusic.play();
        }
    }

    public void pauseMusic() {
        if (currentMusic != null) {
            currentMusic.pause();
            paused = true;
        }
    }

    public void setLooping(boolean loop) {
        if (currentMusic != null) {
            currentMusic.setLooping(loop);
        }
    }

    public void setVolume(float volume) {
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public String getCurrentMusicName() {
        return currentMusicName;
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

}
