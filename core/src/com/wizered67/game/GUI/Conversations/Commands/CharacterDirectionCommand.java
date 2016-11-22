package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * Created by Adam on 11/2/2016.
 */
public class CharacterDirectionCommand implements ConversationCommand {

    String character;
    int direction;

    public CharacterDirectionCommand(String name, int dir) {
        character = name;
        direction = dir;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setDirection(direction);
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
            xmlWriter.element("setdirection")
                    .attribute("name", character)
                    .attribute("direction", direction)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterDirectionCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        int direction = element.getInt("direction", 1);
        return new CharacterDirectionCommand(name, direction);
    }
}
