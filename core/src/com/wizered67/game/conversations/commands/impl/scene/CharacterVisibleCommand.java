package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneCharacter;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;

/**
 * A ConversationCommand that sets the visibility of a SceneCharacter.
 * @author Adam Victor
 */
public class CharacterVisibleCommand implements ConversationCommand {
    /** The identifier of the SceneCharacter to modify the visibility of. */
    private String character;
    /** Whether the SceneCharacter should be visible. */
    private boolean show;
    /** How long to fade in or out the SceneCharacter. */
    private float fadeTime;
    /** Whether to wait for the fade to complete before going on to the next command. */
    private boolean wait;
    /** Whether the command is completed and the next one can be executed. */
    private boolean done;
    /** The name of the interpolation used for fading in and out. */
    private String interpolation = "linear";
    /** No arguments constructor. */
    public CharacterVisibleCommand() {
        character = "";
        show = true;
        fadeTime = 0;
        wait = false;
        done = true;
    }
    /** Creates a new CharacterVisibleCommand that sets the SceneCharacter
     * with identifier ID's visibility to VISIBLE when executed.
     * Waits for completion iff W.
     */
    public CharacterVisibleCommand(String id, boolean visible, float time, String fadeType, boolean w) {
        character = id;
        show = visible;
        fadeTime = time;
        interpolation = fadeType;
        wait = w;
        done = !wait;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        SceneCharacter c = conversationController.currentSceneManager().getOrAddCharacterByIdentifier(character);
        if (c == null) {
            done = true;
            return;
        }
        done = !wait;
        if (fadeTime == 0) {
            c.finishVisibility(show);
            done = true;
        } else {
            int end = show ? 1 : 0;
            c.setFade(new FloatInterpolation(interpolation, c.getSprite().getColor().a, end, fadeTime));
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
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.FADE) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                SceneCharacter cs = manager.getOrAddCharacterByIdentifier(character);
                if (cs != null && cs.equals(entity)) {
                    done = true;
                }
            }
        }
    }
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterVisibleCommand makeCommand(XmlReader.Element element) {
        String name = element.getAttribute("id");
        boolean visible = element.getBooleanAttribute("visible", false);
        float fade = element.getFloatAttribute("fadeTime", 0f);
        String fadeType = element.getAttribute("fadeType", "linear");
        boolean wait = element.getBooleanAttribute("wait", true);
        return new CharacterVisibleCommand(name, visible, fade, fadeType, wait);
    }
}
