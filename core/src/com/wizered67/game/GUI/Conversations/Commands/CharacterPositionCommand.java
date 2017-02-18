package com.wizered67.game.gui.conversations.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.wizered67.game.gui.conversations.CompleteEvent;
import com.wizered67.game.gui.conversations.ConversationController;
import com.wizered67.game.gui.conversations.scene.SceneCharacter;
import com.wizered67.game.gui.conversations.scene.SceneManager;

/**
 * Command that sets the position and depth of a character.
 * Any component not specified is left unchanged.
 * @author Adam Victor
 */
public class CharacterPositionCommand implements ConversationCommand {
    /** The identifier of the character to act on, or empty if it should be a group. */
    private String identifier;
    /** The new position to set the character to, if specified. */
    private Vector2 position;
    /** Whether the x component of position has been specified and should be changed. */
    private boolean xSpecified;
    /** Whether the y component of position has been specified and should be changed. */
    private boolean ySpecified;
    /** The new depth to set the character to, if specified. */
    private int depth;
    /** Whether the depth has been specified and should be changed. */
    private boolean depthSpecified;
    /** Whether changes should be relative to current position. */
    private boolean relative;

    CharacterPositionCommand() {}

    CharacterPositionCommand(String inst, Vector2 pos, int depth, boolean rel) {
        identifier = inst;
        position = pos;
        this.depth = depth;
        xSpecified = true;
        ySpecified = true;
        depthSpecified = true;
        relative = rel;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        SceneManager manager = conversationController.sceneManager();
        SceneCharacter character = manager.getCharacterByIdentifier(identifier);
        if (xSpecified) {
            float newX = position.x;
            if (relative) {
                newX += character.getPosition().x;
            }
            character.setX(newX);
        }
        if (ySpecified) {
            float newY = position.y;
            if (relative) {
                newY += character.getPosition().y;
            }
            character.setY(newY);
        }
        if (depthSpecified) {
            int newDepth = depth;
            if (relative) {
                newDepth += character.getDepth();
            }
            character.setDepth(manager, newDepth);
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
    /** Static method to create a new command from XML Element ELEMENT. */
    public static CharacterPositionCommand makeCommand(XmlReader.Element element) {
        String instance = element.getAttribute("id");
        String xString = element.getAttribute("x", null);
        String yString = element.getAttribute("y", null);
        String depthString = element.getAttribute("depth", null);
        boolean relative = element.getBooleanAttribute("relative", false);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        int depth = (depthString != null) ? Integer.parseInt(depthString) : 0;
        CharacterPositionCommand command = new CharacterPositionCommand(instance, new Vector2(x, y), depth, relative);
        if (xString == null) {
            command.xSpecified = false;
        }
        if (yString == null) {
            command.ySpecified = false;
        }
        if (depthString == null) {
            command.depthSpecified = false;
        }
        return command;
    }

    /**
     * Outputs XML to the XML WRITER for this command.
     */
    @Override
    public void writeXml(XmlWriter xmlWriter) {

    }
}
