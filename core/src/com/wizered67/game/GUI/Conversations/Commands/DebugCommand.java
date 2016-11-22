package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * Created by Adam on 10/24/2016.
 */
public class DebugCommand implements ConversationCommand {

    private String message;

    public DebugCommand(String m) {
        message = m;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        System.out.println(message);
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
            xmlWriter.element("debug")
                    .attribute("message", message)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DebugCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String text = element.getAttribute("message");
        return new DebugCommand(text);
    }
}
