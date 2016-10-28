package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;
import com.wizered67.game.GameManager;

/**
 * Created by Adam on 10/26/2016.
 */
public class PlaySoundCommand implements ConversationCommand {
    private String sound;

    public PlaySoundCommand(String s) {
        sound = s;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        if (GameManager.assetManager().isLoaded(sound)) {
            Sound s = GameManager.assetManager().get(sound, Sound.class);
            s.play();
        } else {
            Gdx.app.log("Asset Error", "No sound loaded: " + sound);
        }
    }

    @Override
    public boolean waitToProceed() {
        return false;
    }

    @Override
    public void complete(CompleteEvent c) {

    }
}
