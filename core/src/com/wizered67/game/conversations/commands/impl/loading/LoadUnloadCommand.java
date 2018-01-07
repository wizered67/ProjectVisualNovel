package com.wizered67.game.conversations.commands.impl.loading;

import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;

/**
 * Multi-purpose ConversationCommand that can both load or unload resources or groups of resources.
 * @author Adam Victor
 */
public class LoadUnloadCommand implements ConversationCommand {
    private String resourceName;
    private boolean isGroup;
    private boolean isLoad;
    public LoadUnloadCommand() {}

    public LoadUnloadCommand(String name, boolean group, boolean load) {
        resourceName = name;
        isGroup = group;
        isLoad = load;
    }
    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        if (isLoad) {
            if (isGroup) {
                GameManager.assetManager().loadGroup(resourceName);
            } else {
                GameManager.assetManager().load(resourceName);
            }
        } else {
            if (isGroup) {
                GameManager.assetManager().unloadGroup(resourceName);
            } else {
                GameManager.assetManager().unload(resourceName);
            }
        }
    }

    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return false;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {

    }
}
