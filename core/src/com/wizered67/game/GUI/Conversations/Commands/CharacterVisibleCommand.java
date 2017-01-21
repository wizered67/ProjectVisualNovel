package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

import java.io.IOException;
/**
 * A ConversationCommand that sets the visibility of a CharacterSprite.
 * @author Adam Victor
 */
public class CharacterVisibleCommand implements ConversationCommand {
    /** The identifier of the CharacterSprite to modify the visibility of. */
    private String character;
    /** Whether the CharacterSprite should be visible. */
    private boolean show;
    /** How long to fade in or out the CharacterSprite. */
    private float fadeTime;
    /** Whether to wait for the fade to complete before going on to the next command. */
    private boolean wait;
    /** Whether the command is completed and the next one can be executed. */
    private boolean done;
    /** No arguments constructor. */
    public CharacterVisibleCommand() {
        character = "";
        show = true;
        fadeTime = 0;
        wait = false;
        done = true;
    }
    /** Creates a new CharacterVisibleCommand that sets the CharacterSprite
     * with identifier ID's visibility to VISIBLE when executed.
     * Waits for completion iff W.
     */
    public CharacterVisibleCommand(String id, boolean visible, float time, boolean w) {
        character = id;
        show = visible;
        fadeTime = time;
        wait = w;
        done = !wait;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        CharacterSprite c = conversationController.sceneManager().getCharacterByIdentifier(character);
        if (c == null) {
            done = true;
            return;
        }
        done = !wait;
        if (fadeTime == 0) {
            c.setVisible(show);
        } else {
            float factor = show ? 1f : -1f;
            float fadePerSecond = factor / fadeTime;
            c.setFade(fadePerSecond);
        }
        if (show) {
            c.addToScene(conversationController.sceneManager());
        }
    }
    /** Whether to wait before proceeding to the next command in the branch. */
    @Override
    public boolean waitToProceed() {
        return !done;
    }
    /** Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly. */
    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.FADE_END) {
            done = true;
        }
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("setvisible")
                    .attribute("name", character)
                    .attribute("visible", show)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterVisibleCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("id");
        boolean visible = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fade", 0f);
        boolean wait = element.getBooleanAttribute("wait", true);
        return new CharacterVisibleCommand(name, visible, fade, wait);
    }
}
