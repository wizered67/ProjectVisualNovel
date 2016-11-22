package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.math.Vector2;
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
public class CharacterPositionCommand implements ConversationCommand {
    private String character;
    private Vector2 position;

    public CharacterPositionCommand(String character, Vector2 position) {
        this.character = character;
        this.position = position;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setPosition(position);
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
            xmlWriter.element("setposition")
                    .attribute("name", character)
                    .attribute("x", position.x)
                    .attribute("y", position.y)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterPositionCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        //String position = element.getAttribute("position");
        float x = element.getFloatAttribute("x");
        float y = element.getFloatAttribute("y");
        return new CharacterPositionCommand(name, new Vector2(x, y)); //TODO fix this
    }
}
