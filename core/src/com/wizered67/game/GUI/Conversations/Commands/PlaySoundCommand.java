package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;
import com.wizered67.game.GameManager;

import java.io.IOException;

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

    public static PlaySoundCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        return new PlaySoundCommand(name);
    }
}
