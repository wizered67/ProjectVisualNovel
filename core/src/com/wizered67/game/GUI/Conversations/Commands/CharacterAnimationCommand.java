package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * Created by Adam on 10/28/2016.
 */
public class CharacterAnimationCommand implements ConversationCommand {

    private boolean done;
    private String animation;
    private boolean wait;
    private String character;

    public CharacterAnimationCommand(String name, String anim, boolean w) {
        wait = w;
        done = !wait;
        animation = anim;
        character = name;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        CharacterSprite c = messageWindow.sceneManager().getCharacterByName(character);
        if (c == null) {
            done = true;
            return;
        }

        done = !wait;
        if (!c.setCurrentAnimation(animation)) {
            done = true;
        }
    }

    @Override
    public boolean waitToProceed() {
        return !done;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.ANIMATION_END) {
            if (c.data.equals(animation)) {
                done = true;
            }

        }
    }

    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("setanimation")
                    .attribute("name", character)
                    .attribute("animation", animation)
                    .attribute("wait", wait)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CharacterAnimationCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String name = element.getAttribute("name");
        String animation = element.getAttribute("animation", name);
        boolean wait = element.getBoolean("wait", false);
        return new CharacterAnimationCommand(name, animation, wait);
    }
}
