package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * Created by Adam on 11/2/2016.
 */
public class CharacterAddCommand implements ConversationCommand {
    String character;
    String animations;
    String speakingSound;

    public CharacterAddCommand(String name) {
        this(name, name, null);
    }

    public CharacterAddCommand(String name, String animationsName, String sound) {
        character = name;
        animations = animationsName;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        messageWindow.sceneManager().addCharacter(character, animations, speakingSound);
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
            xmlWriter.element("addcharacter")
                    .attribute("name", character)
                    .attribute("animations", animations)
                    .attribute("sound", speakingSound)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterAddCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        String animations = element.getAttribute("animations", name);
        String sound = element.getAttribute("sound", null);
        return new CharacterAddCommand(name, animations, sound);
    }

}
