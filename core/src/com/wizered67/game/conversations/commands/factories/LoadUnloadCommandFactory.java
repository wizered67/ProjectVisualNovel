package com.wizered67.game.conversations.commands.factories;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.commands.impl.loading.LoadUnloadCommand;
import com.wizered67.game.conversations.xmlio.ConversationLoader;

/**
 * Factory for creating a LoadUnloadCommand from an XML element.
 * @author Adam Victor
 */
public class LoadUnloadCommandFactory implements ConversationCommandFactory<LoadUnloadCommand> {
    private final static LoadUnloadCommandFactory INSTANCE = new LoadUnloadCommandFactory();

    public static LoadUnloadCommandFactory getInstance() {
        return INSTANCE;
    }

    private LoadUnloadCommandFactory() {
    }

    @Override
    public LoadUnloadCommand makeCommand(ConversationLoader loader, XmlReader.Element element) {
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
}