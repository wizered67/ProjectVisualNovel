package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneEntity;
import com.wizered67.game.conversations.scene.SceneManager;
import com.wizered67.game.conversations.scene.interpolations.PositionInterpolation;

/**
 * Command that sets the position and depth of an entity.
 * Any component not specified is left unchanged.
 * @author Adam Victor
 */
public class EntityPositionCommand implements ConversationCommand { //todo allow working on image groups again
    /** Whether this command is being applied to a character (true) or image (false). */
    private boolean isCharacter;
    /** The identifier of the character to act on, or empty if it should be a group. */
    private String identifier;
    /** The new position to set the character to, if specified. */
    private Vector2 newPosition;
    /** Whether the x component of position has been specified and should be changed. */
    private boolean xSpecified;
    /** Whether the y component of position has been specified and should be changed. */
    private boolean ySpecified;
    /** The new depth to set the character to, if specified. */
    private int depth;
    /** Whether the depth has been specified and should be changed. */
    private boolean depthSpecified;
    /** The name of the type of interpolation to use. */
    private String interpolationType;
    /** Over what period of time to move to the final position. */
    private float time;
    /** Whether changes should be relative to current position. */
    private boolean relative;
    /** Whether the command has finished executing. */
    private boolean done;
    /** Whether to wait for the interpolation to finish before moving on. */
    private boolean wait;

    EntityPositionCommand() {}

    EntityPositionCommand(String inst, Vector2 position, int depth, String interpolation, float time, boolean wait,
                          boolean rel, boolean isCharacter) {
        identifier = inst;
        newPosition = position;
        this.depth = depth;
        interpolationType = interpolation;
        this.time = time;
        xSpecified = true;
        ySpecified = true;
        depthSpecified = true;
        relative = rel;
        this.wait = wait;
        this.isCharacter = isCharacter;
    }

    /**
     * Executes the command on the CONVERSATION CONTROLLER.
     */
    @Override
    public void execute(ConversationController conversationController) {
        final SceneManager manager = conversationController.currentSceneManager();
        boolean result = manager.applyEntityCommand(identifier, isCharacter, new EntityAction<SceneEntity>() {
            @Override
            public void apply(SceneEntity entity) {
                float newX = entity.getPosition().x;
                float newY = entity.getPosition().y;
                if (xSpecified) {
                    if (relative) {
                        newX += newPosition.x;
                    } else {
                        newX = newPosition.x;
                    }
                }
                if (ySpecified) {
                    if (relative) {
                        newY += newPosition.y;
                    } else {
                        newY = newPosition.y;
                    }
                }
                if (time == 0) {
                    entity.setPosition(newX, newY);
                } else {
                    entity.setPositionInterpolation(new PositionInterpolation(interpolationType,
                            entity.getPosition(), new Vector2(newX, newY), time));
                }
                if (depthSpecified) {
                    int newDepth = depth;
                    if (relative) {
                        newDepth += entity.getDepth();
                    }
                    entity.setDepth(manager, newDepth);
                }
            }
        });
        done = !wait || !result;
    }


    /**
     * Whether to wait before proceeding to the next command in the branch.
     */
    @Override
    public boolean waitToProceed() {
        return !done;
    }

    /**
     * Checks whether the CompleteEvent C completes this command,
     * and if so acts accordingly.
     */
    @Override
    public void complete(CompleteEvent c) {
        if (c.type == CompleteEvent.Type.INTERPOLATION) {
            Object[] data = (Object[]) c.data;
            if (data[2] == CompleteEvent.InterpolationEventType.POSITION) {
                SceneManager manager = (SceneManager) data[0];
                Object entity = data[1];
                SceneEntity thisEntity = isCharacter ? manager.getOrAddCharacterByIdentifier(identifier) : manager.getImage(identifier);
                if (entity != null && entity.equals(thisEntity)) {
                    done = true;
                }
            }
        }
    }

    /** Static method to create a new command from XML Element ELEMENT. */
    public static EntityPositionCommand makeCommand(String instance, XmlReader.Element element) {
        boolean isCharacter = false;
        if (instance == null) {
            instance = element.getAttribute("id");
            isCharacter = true;
        }
        String xString = element.getAttribute("x", null);
        String yString = element.getAttribute("y", null);
        String depthString = element.getAttribute("depth", null);
        boolean relative = element.getBooleanAttribute("relative", false);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        int depth = (depthString != null) ? Integer.parseInt(depthString) : 0;
        String interpolationType = element.getAttribute("interpolation", "linear");
        float time = element.getFloatAttribute("time", 0);
        boolean wait = element.getBooleanAttribute("wait", false);
        EntityPositionCommand command = new EntityPositionCommand(instance, new Vector2(x, y), depth,
                interpolationType, time, wait, relative, isCharacter);
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
}
