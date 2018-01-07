package com.wizered67.game.conversations.commands.impl.scene;

import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.CharacterDefinition;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * A ConversationCommand that changes the displayed name of a SceneCharacter.
 * @author Adam Victor
 */
public class CharacterNameCommand implements ConversationCommand {
    /** The identifier of the SceneCharacter to rename. */
    private String character;
    /** The new display name for the SceneCharacter. */
    private String newName;

    /** No arguments constructor. */
    public CharacterNameCommand() {
        character = "";
        newName = "";
    }

    /** Creates a new CharacterNameCommand that changes the SceneCharacter with identifier ID to have
     * display name NAME when executed.
     */
    public CharacterNameCommand(String id, String name) {
        character = id;
        newName = name;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        CharacterDefinition definition = SceneManager.getCharacterDefinition(character);
        if (definition != null) {
            definition.setKnownName(newName);
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
}
