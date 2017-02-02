package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.Conversations.CompleteEvent;
import com.wizered67.game.GameManager;

/**
 * Represents an image displayed by the user during a scene and its depth.
 * Holds a Sprite and the filename associated with it for serialization,
 * as well as an "instance id" to be used to reference this with commands.
 * Note: this class has a natural ordering that is inconsistent with equals.
 * It uses depth in compareTo for ordering and uses instanceIdentifier for equals.
 * @author Adam Victor
 */
public class SceneImage extends SceneEntity {
    /** Identifier specified in commands to reference this image. */
    private String instanceIdentifier;
    /** Identifier specified in commands to refer to group of images. */
    private String groupIdentifier;
    /** The name of the texture used with this. */
    private String textureName;
    private static int nextInstance;
    static {
        nextInstance = 0;
    }
    /** Empty constructor used for serialization. */
    public SceneImage() {}

    public SceneImage(String instance) {
        removed = false;
        if (!instance.isEmpty()) {
            instanceIdentifier = instance;
        } else {
            instanceIdentifier = "__INTERNAL_INSTANCE__" + nextInstance;
            nextInstance += 1;
        }
        sprite = new Sprite();
        sprite.setAlpha(0);
        fadePerSecond = 0;
        hasDepth = false;
    }

    @Override
    public void reload() {

    }

    public void setTexture(String texture) {
        textureName = texture;
        if (!GameManager.assetManager().isLoaded(texture)) {
            GameManager.assetManager().finishLoadingAsset(texture);
        }
        //sprite.setTexture();
        //sprite.setRegion(GameManager.assetManager().get(texture, Texture.class));
        Texture t = GameManager.assetManager().get(texture, Texture.class);
        sprite.setTexture(t);
        sprite.setRegion(t);
        sprite.setSize(t.getWidth(), t.getHeight());
        sprite.setOrigin(t.getWidth() / 2, t.getHeight() / 2);
        //sprite = new Sprite(GameManager.assetManager().get(texture, Texture.class));
    }

    public void addToScene(SceneManager m) {
        manager = m;
        manager.addImage(this);
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

    public String getTextureName() {
        return textureName;
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
