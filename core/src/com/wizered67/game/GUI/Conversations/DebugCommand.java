package com.wizered67.game.GUI.Conversations;

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
    public boolean waitForInput() {
        return false;
    }
}
