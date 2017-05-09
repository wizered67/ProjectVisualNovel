package com.wizered67.game.conversations.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.wizered67.game.GameManager;

/**
 * Represents an image displayed by the user during a scene. Extends the SceneEntity abstract
 * class so that it is updated and displayed in SceneManager. When commands change the values of
 * SceneImages, they must either specify an instance identifier or group identifier. Instance
 * identifiers should be unique and specify a specific SceneImage while groupIdentifiers can
 * specify a set of several SceneImages. The commands used to modify images can be found in the
 * images subpackage of the commands package.
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 * It uses depth in compareTo for ordering and uses instanceIdentifier for equals.
 * @author Adam Victor
 */
public class SceneImage extends SceneEntity {
    /** Identifier specified in commands to reference this image. */
    private String instanceIdentifier;
    /** Identifier specified in commands to refer to group of images. */
    private String groupIdentifier;
    /** Used to assign a unique id to each instance in the case that an instance identifier is not specified. */
    private static int nextInstance;
    static {
        nextInstance = 0;
    }
    /** Empty constructor used for serialization. */
    public SceneImage() {
        super();
    }

    /** Creates a SceneImage with the instance identifier INSTANCE. If INSTANCE is empty,
     * a unique identifier is given based on the value of nextInstance. Creates a Sprite but
     * sets it to not visible. To give the SceneImage a texture, make it visible, or change its depth
     * methods must be called, usually through commands.
     */
    public SceneImage(String instance) {
        super();
        removed = false;
        if (!instance.isEmpty()) {
            instanceIdentifier = instance;
        } else {
            instanceIdentifier = "__INTERNAL_INSTANCE__" + nextInstance;
            nextInstance += 1;
        }
        sprite = new Sprite();
        sprite.setAlpha(0);
        fade = null;
        hasDepth = false;
    }

    public void addToScene(SceneManager m) {
        manager = m;
        manager.addImage(this);
        setDepth(manager, depth);
        removed = false;
    }

    public void removeFromScene() {
        if (manager != null) {
            manager.removeImage(this);
        }
        removed = true;
    }

    public void changeGroup(SceneManager manager, String newGroup) {
        String oldGroup = groupIdentifier;
        groupIdentifier = newGroup;
        manager.changeImageGroup(this, oldGroup);
    }

    public String getGroup() {
        return groupIdentifier;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    @Override
    public int hashCode() {
        return instanceIdentifier.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof SceneImage) {
            SceneImage other = (SceneImage) o;
            return other.instanceIdentifier.equals(instanceIdentifier);
        } else {
            return false;
        }
    }
}
