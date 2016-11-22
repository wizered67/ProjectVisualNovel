package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.MessageWindow;
import com.wizered67.game.GameManager;

/**
 * Created by Adam on 10/26/2016.
 */
public class PreloadCommand implements ConversationCommand {
    private String file;
    private Class type;
    private boolean load;

    public PreloadCommand(String f, Class t, boolean l) {
        file = f;
        type = t;
        load = l;
    }

    public PreloadCommand(String f, Class t) {
        this(f, t, true);
    }


    @Override
    public void execute(MessageWindow messageWindow) {
        boolean alreadyLoaded = GameManager.assetManager().isLoaded(file, type);
        if (load) {
            if (!alreadyLoaded) {
                GameManager.assetManager().load(file, type);
                System.out.println("Now loading " + file);
            }
        } else {
            if (alreadyLoaded) {
                GameManager.assetManager().unload(file);
            }
        }
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

    }


}
