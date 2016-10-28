package com.wizered67.game.GUI.Conversations.Commands;

import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.Conversation;
import com.wizered67.game.GUI.Conversations.MessageWindow;

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

    public String toString() {
        return "Change branch to " + newBranch + ".";
    }
}
