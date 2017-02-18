package com.wizered67.game.conversations.commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.GameManager;

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

    public static LoadUnloadCommand makeCommand(XmlReader.Element element) {
        boolean load;
        boolean group;
        String resourceName;
        if (element.getName().equals("load")) {
            load = true;
            group = false;
        } else if (element.getName().equals("unload")) {
            load = false;
            group = false;
        } else if (element.getName().equals("loadGroup")) {
            load = true;
            group = true;
        } else if (element.getName().equals("unloadGroup")) {
            load = false;
            group = true;
        } else {
            GameManager.error("Invalid load/unload command name.");
            return null;
        }
        resourceName = element.getChildByName("text").getText();
        return new LoadUnloadCommand(resourceName, group, load);
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
