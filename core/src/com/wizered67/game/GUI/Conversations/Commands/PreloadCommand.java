package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;
import com.wizered67.game.GameManager;

/**
 * A ConversationCommand that ensures an asset is loaded.
 * @author Adam Victor
 */
public class PreloadCommand implements ConversationCommand {
    /** Name of the file to load. */
    private String file;
    /** Type of the file to load. */
    private Class type;
    /** Loads the file if true, unloads it when false. */
    private boolean load;

    /** Creates a new PreloadCommand that sets load status to L and loads or
     * unloads the file named F of type T when executed.
     */
    public PreloadCommand(String f, Class t, boolean l) {
        file = f;
        type = t;
        load = l;
    }
    /** Creates a new PreloadCommand that loads the file named F of type T when executed. */
    public PreloadCommand(String f, Class t) {
        this(f, t, true);
    }

    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
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
        //TODO Add this
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static PreloadCommand makeCommand(XmlReader.Element element) {
        //TODO Add this
        return null;
    }


}
