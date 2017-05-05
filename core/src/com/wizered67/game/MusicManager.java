package com.wizered67.game;

import com.badlogic.gdx.audio.Music;


/**
 * Used to set the current music to be played to ensure that only one is played at a time.
 * Also allows pausing the music and resuming it later.
 * @author Adam Victor
 */
public class MusicManager {
    /** The maximum number of music instances that can be playing at the same time. */
    private final static int MAX_MUSIC = 5;
    /** The Music object currently being played. */
    private transient Music[] currentMusic;
    /** The name of the music currently being played. Used to tell if music has changed. */
    private String[] currentMusicName;
    /** Whether the music being played has been paused. */
    private boolean[] paused;
    /** Whether the music is looping. */
    private boolean[] looping;
    /** Volume of the music. */
    private float[] volumes;
    /** Initialize MusicManager with no music being played. */
    public MusicManager() {
        currentMusic = new Music[MAX_MUSIC];
        currentMusicName = new String[MAX_MUSIC];
        paused = new boolean[MAX_MUSIC];
        looping = new boolean[MAX_MUSIC];
        volumes = new float[MAX_MUSIC];
    }

    /** Plays the music with identifier NAME. If it was already playing but paused, resume it.
     * If different music was playing before, stop it. Iff LOOPS, the music will continue to loop.
     */
    public void playMusic(String id, boolean loops, float volume, int index) {
        if (GameManager.assetManager().isLoaded(id)) {
            Music music = GameManager.assetManager().get(id);
            playMusic(music, id, loops, volume, index);
        } else {
            GameManager.error("Music '" + id + "' was not loaded first.");
        }
    }

    /** Plays the Music object MUSIC named NAME. If it was already playing but paused, resume it.
     * If different music was playing before, stop it. Iff LOOPS, the music will continue to loop.
     */
    public void playMusic(Music music, String name, boolean loops, float volume, int index) {
        if (currentMusicName[index] != null && currentMusicName[index].equals(name)) {
            if (paused[index]) {
                resumeMusic(index);
            }
            return;
        }
        paused[index] = false;
        stopMusic(index);
        currentMusic[index] = music;
        currentMusicName[index] = name;
        setLooping(loops, index);
        setVolume(volume, index);
        music.play();
    }
    /** Stops the music currently being played and resets currentMusic and currentMusicName. */
    public void stopMusic(int index) {
        if (currentMusic[index] != null) {
            currentMusic[index].stop();
            currentMusicName[index] = null;
            currentMusic[index] = null;
        }
    }
    /** Resume the music currently being played. */
    public void resumeMusic(int index) {
        if (currentMusic[index] != null) {
            currentMusic[index].play();
        }
    }
    /** Pause the music currently being played. */
    public void pauseMusic(int index) {
        if (currentMusic[index] != null) {
            currentMusic[index].pause();
            paused[index] = true;
        }
    }
    /** Sets whether the music should loop. */
    public void setLooping(boolean loop, int index) {
        if (currentMusic[index] != null) {
            currentMusic[index].setLooping(loop);
            looping[index] = loop;
        }
    }
    /** Sets the volumes of the music playing. */
    public void setVolume(float volume, int index) {
        if (currentMusic[index] != null) {
            currentMusic[index].setVolume(volume);
            volumes[index] = volume;
        }
    }
    /** Returns the name of the music playing. */
    public String getCurrentMusicName(int index) {
        return currentMusicName[index];
    }
    /** Returns a Music object for the music playing. */
    public Music getCurrentMusic(int index) {
        return currentMusic[index];
    }
    /** Reloads the music that was previously playing. */
    public void reload() {
        for (int i = 0; i < MAX_MUSIC; i += 1) {
            if (currentMusic[i] != null) {
                currentMusic[i].stop();
            }
            if (currentMusicName[i] != null && GameManager.assetManager().isLoaded(currentMusicName[i], Music.class)) {
                currentMusic[i] = GameManager.assetManager().get(currentMusicName[i], Music.class);
                currentMusic[i].setVolume(volumes[i]);
                currentMusic[i].setLooping(looping[i]);
                currentMusic[i].setPosition(0);
            }
            if (!paused[i] && currentMusic[i] != null) {
                currentMusic[i].play();
            }
        }

    }

}
