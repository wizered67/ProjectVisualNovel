package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;
import com.wizered67.game.GUI.Conversations.XmlIO.ConversationLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam on 10/24/2016.
 */
public class MessageCommand implements ConversationCommand {
    private String text;
    private String speaker;
    private boolean done;
    private static final String DEFAULT_SOUND = "talksoundmale";
    private Map<String, ConversationCommand> assignments;

   /* public MessageCommand(String s, String t) {
        this(s, t, "talksoundmale");
    } */

    public MessageCommand(String character, String message) {
        speaker = character;
        text = message;
        done = false;
        assignments = new HashMap<String, ConversationCommand>();
    }

    public String getText() {
        return text;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void execute(MessageWindow messageWindow) {
        messageWindow.setRemainingText(text);
        CharacterSprite characterSpeaking = messageWindow.sceneManager().getCharacterByName(speaker);
        messageWindow.setSpeaker(characterSpeaking.getKnownName());
        messageWindow.setCurrentSpeakerSound(characterSpeaking.getSpeakingSound());
        done = false;
    }

    @Override
    public boolean waitToProceed() {
        return !done;
    }

    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.INPUT) {
            done = true;
        }
    }

    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("message")
                    .attribute("speaker", speaker)
                    .attribute("text", text)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String speaker = element.getAttribute("speaker");
        //String message = element.getAttribute("text");
        MessageCommand message = new MessageCommand(speaker, "");
        for (int i = 0; i < element.getChildCount(); i += 1) {
            XmlReader.Element e = element.getChild(i);
            if (e.getName().equalsIgnoreCase("text")) {

            } else if (e.getName().equalsIgnoreCase("assign")) {
                message.assignments.put(e.getAttribute("name", ""),
                        ConversationLoader.getCommand(conversation, e));
            }
        }
        return null;
    }
}
