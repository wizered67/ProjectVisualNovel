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
 * A class to convert ConversationCommands to their XML equivalent.
 * @author Adam Victor
 */
public class ConversationWriter {
    /** Writes the entire CONVERSATION to an XML file with name FILENAME. */
    public static void writeConversation(Conversation conversation, String filename) {
        FileHandle file = Gdx.files.local(filename);
        Writer writer = file.writer(false);
        XmlWriter xmlWriter = new XmlWriter(writer);
        try {

            xmlWriter.element("conversation");
            for (String branch : conversation.getAllBranches()) {
                writeBranch(xmlWriter, conversation, branch);
            }
            xmlWriter.pop();
            writer.close();
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Writes the branch of CONVERSATION with name BRANCHNAME to
     * XML by using the XML WRITER. */
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
