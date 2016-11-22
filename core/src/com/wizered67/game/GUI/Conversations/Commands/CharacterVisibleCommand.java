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
public class CharacterVisibleCommand implements ConversationCommand {

    String character;
    boolean show;

    public CharacterVisibleCommand(String name, boolean visible) {
        character = name;
        show = visible;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            return;
        }
        c.setVisible(show);
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
            xmlWriter.element("setvisible")
                    .attribute("name", character)
                    .attribute("visible", show)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterVisibleCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        boolean visible = element.getBoolean("visible", false);
        return new CharacterVisibleCommand(name, visible);
    }
}
