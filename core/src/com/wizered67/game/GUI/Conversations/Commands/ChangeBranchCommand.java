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
 * A ConversationCommand that changes the current branch to a new one.
 * @author Adam Victor
 */
public class ChangeBranchCommand implements ConversationCommand {
    /** The Conversation this command is part of. */
    private Conversation conversation;
    /** The name of the new branch to switch to. */
    private String newBranch;
    /** Creates a new ChangeBranchCommand with Conversation C that
     * changes the branch to BRANCH when executed. */
    public ChangeBranchCommand(Conversation c, String branch) {
        conversation = c;
        newBranch = branch;
    }
    /** Executes the command on the MESSAGE WINDOW. */
    @Override
    public void execute(MessageWindow messageWindow) {
        messageWindow.setBranch(conversation.getBranch(newBranch));
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return false;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {

    }
    /** Outputs XML to the XML WRITER for this command. */
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
    /** A string describing this command. */
    public String toString() {
        return "Change branch to " + newBranch + ".";
    }
    /** Static method to create a new command from XML Element ELEMENT that is part of CONVERSATION. */
    public static ChangeBranchCommand makeCommand(Conversation conversation, XmlReader.Element element) {
        String newBranch = element.getAttribute("newbranch", null);
        if (newBranch == null) {
            throw new GdxRuntimeException("No new branch specified for new branch command.");
        }
        return new ChangeBranchCommand(conversation, newBranch);
    }
}
