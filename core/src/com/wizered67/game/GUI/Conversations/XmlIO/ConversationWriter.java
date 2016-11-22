package com.wizered67.game.GUI.Conversations.XmlIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.Commands.ConversationCommand;
import com.wizered67.game.GUI.Conversations.Conversation;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

/**
 * Created by Adam on 11/16/2016.
 */
public class ConversationWriter {
    public static void writeConversation(Conversation conversation, String filename) {
        FileHandle file = Gdx.files.local(filename);
        Writer writer = file.writer(false);
        XmlWriter xmlWriter = new XmlWriter(writer);
        try {

            xmlWriter.element("conversation");
            for (String branch : conversation.getAllBranches()) {
                writeBranch(xmlWriter, conversation, branch);
            }
                   /*
                    .element("branch").attribute("name", "default")
                    .element("message").attribute("speaker", "Adam").attribute("text", "Test message.")
                    .pop()
                    .pop()
                    .pop();
                    */
            xmlWriter.pop();
            writer.close();
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBranch(XmlWriter xmlWriter, Conversation conversation, String branchName) {
        try {
            xmlWriter.element("branch").attribute("name", branchName);
            LinkedList<ConversationCommand>  branch = conversation.getBranch(branchName);
            for (ConversationCommand command : branch) {
                command.writeXml(xmlWriter);
            }
            xmlWriter.pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
