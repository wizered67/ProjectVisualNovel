package com.wizered67.game.GUI.Conversations.Commands;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.GUI.Conversations.CharacterSprite;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GUI.Conversations.ConversationController;

import java.io.IOException;

/**
 * A ConversationCommand that changes an existing CharacterSprite's animation.
 * @author Adam Victor
 */
public class CharacterAnimationCommand implements ConversationCommand {
    /** Whether this command is done executing. If wait is false, then automatically true.
     * Otherwise only true once the Animation has finished. */
    private boolean done;
    /** The name of the animation to switch to. */
    private String animation;
    /** Whether to wait for the Animation to complete before moving to the next
     * ConversationCommand. */
    private boolean wait;
    /** The identifier of the CharacterSprite that should have its animation changed. */
    private String character;

    /** No arguments constructor. */
    public CharacterAnimationCommand() {
        wait = false;
        done = true;
        animation = "";
        character = "";
    }

    /** Creates a new CharacterAnimationCommand that changes CharacterSprite NAME's
     * animation to ANIM when executed. Waits to complete before going to the next ConversationCommand
     * iff W.
     */
    public CharacterAnimationCommand(String id, String anim, boolean w) {
        wait = w;
        done = !wait;
        animation = anim;
        character = id;
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
        if (!c.setCurrentAnimation(animation)) {
            done = true;
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
        if (c.type == CompleteEvent.Type.ANIMATION_END) {
            if (c.data.equals(animation)) {
                done = true;
            }

        }
    }
    /** Outputs XML to the XML WRITER for this command. */
    @Override
    public void writeXml(XmlWriter xmlWriter) {
        try {
            xmlWriter.element("setanimation")
                    .attribute("name", character)
                    .attribute("animation", animation)
                    .attribute("wait", wait)
                    .pop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterAnimationCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("id");
        String animation = element.getAttribute("animation", name);
        boolean wait = element.getBoolean("wait", false);
        return new CharacterAnimationCommand(name, animation, wait);
    }
}
