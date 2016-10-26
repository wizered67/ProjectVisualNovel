package com.wizered67.game.GUI.Conversations;

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
    public boolean waitForInput() {
        return false;
    }

    public String toString() {
        return "Change branch to " + newBranch + ".";
    }
}
