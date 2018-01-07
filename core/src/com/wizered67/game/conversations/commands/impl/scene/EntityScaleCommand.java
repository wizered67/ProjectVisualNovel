package com.wizered67.game.conversations.commands.impl.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.commands.ConversationCommand;
import com.wizered67.game.conversations.scene.SceneEntity;
import com.wizered67.game.conversations.scene.SceneManager;

/**
 * A ConversationCommand that changes an existing SceneEntity's scale.
 * @author Adam Victor
 */
public class EntityScaleCommand implements ConversationCommand {
    /** Whether the entity to change the scale of is a character. */
    private boolean isCharacter;
    /** Identifier of the SceneEntity to change the scale of. */
    private String identifier;
    /** Scale to apply to the SceneEntity's sprite. */
    private Vector2 scale;
    /** Whether the xScale was specified. */
    private boolean xSpecified;
    /** Whether the yScale was specified. */
    private boolean ySpecified;

    /** No arguments constructor. */
    public EntityScaleCommand() {
        identifier = "";
        scale = new Vector2(1, 1);
        xSpecified = false;
        ySpecified = false;
    }
    /** Creates a new EntityScaleCommand that changes the SceneEntity
     * ID's scale to SCALE when executed.
     */
    public EntityScaleCommand(String id, Vector2 scale, boolean xSpecified, boolean ySpecified, boolean isCharacter) {
        identifier = id;
        this.scale = scale;
        this.xSpecified = xSpecified;
        this.ySpecified = ySpecified;
        this.isCharacter = isCharacter;
    }
    /** Executes the command on the CONVERSATION CONTROLLER. */
    @Override
    public void execute(ConversationController conversationController) {
        SceneManager manager = conversationController.currentSceneManager();
        boolean result = manager.applyEntityCommand(identifier, isCharacter, new EntityAction<SceneEntity>() {

            @Override
            public void apply(SceneEntity entity) {
                float xScale = xSpecified ? scale.x : entity.getSprite().getScaleX();
                float yScale = ySpecified ? scale.y : entity.getSprite().getScaleY();
                entity.getSprite().setScale(xScale, yScale);
            }
        });
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

    /** Static method to create a new command from XML Element ELEMENT. */
    public static EntityScaleCommand makeCommand(String instance, XmlReader.Element element) {
        boolean isCharacter = false;
        if (instance == null) {
            instance = element.getAttribute("id");
            isCharacter = true;
        }
        String xString = element.getAttribute("xScale", null);
        String yString = element.getAttribute("yScale", null);
        float x = (xString != null) ? Float.parseFloat(xString) : 0;
        float y = (yString != null) ? Float.parseFloat(yString) : 0;
        return new EntityScaleCommand(instance, new Vector2(x, y), xString != null, yString != null, isCharacter);
    }
}
