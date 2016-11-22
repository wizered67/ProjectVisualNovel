package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

import java.io.IOException;

/**
 * Created by Adam on 10/24/2016.
 */
public class ChangeBranchCommand implements ConversationCommand {

    private Conversation conversation;
    private String newBranch;

    public ChangeBranchCommand(Conversation c, String branch) {
        conversation = c;
        newBranch = branch;
    }

    @Override
    public void execute(MessageWindow messageWindow) {
        messageWindow.setBranch(conversation.getBranch(newBranch));
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
            xmlWriter.element("changebranch")
                    .attribute("newbranch", newBranch)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Change branch to " + newBranch + ".";
    }

    public static ChangeBranchCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String newBranch = element.getAttribute("newbranch", null);
        if (newBranch == null) {
            throw new GdxRuntimeException("No new branch specified for new branch command.");
        }
        return new ChangeBranchCommand(conversation, newBranch);
    }
}
