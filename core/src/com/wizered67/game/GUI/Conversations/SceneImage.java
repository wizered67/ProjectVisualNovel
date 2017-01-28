package com.wizered67.game.GUI.Conversations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GameManager;

/**
 * Represents an image displayed by the user during a scene and its depth.
 * Holds a Sprite and the filename associated with it for serialization,
 * as well as an "instance id" to be used to reference this with commands.
 * Note: this class has a natural ordering that is inconsistent with equals.
 * It uses depth in compareTo for ordering and uses instanceIdentifier for equals.
 * @author Adam Victor
 */
public class SceneImage implements Comparable<SceneImage> {
    /** Identifier specified in commands to reference this image, or the identifier of
     * the texture if none is specified. */
    private String instanceIdentifier;
    /** The sprite that is drawn by this image. */
    private transient Sprite sprite;
    /** Whether this image has already been removed. */
    private boolean removed;
    /** The SceneManager this is part of. */
    private SceneManager manager;
    /** How much the alpha of the sprite being drawn should change per second. */
    private float fadePerSecond;
    /** Depth to draw this image at, lower is closer to background, higher is foreground. */
    private int depth;
    /** The name of the texture used with this. */
    private String textureName;


    public SceneImage() {}

    /** Used to make dummy image at depth 0. Used in SceneManager. */
    public SceneImage(int depth) {
        this.depth = depth;
    }

    public SceneImage(String instance, String texture, int depth) {
        removed = false;
        instanceIdentifier = instance;
        if (!GameManager.assetManager().isLoaded(texture)) {
            GameManager.assetManager().finishLoadingAsset(texture);
        }
        sprite = new Sprite(GameManager.assetManager().get(texture, Texture.class));
        sprite.setAlpha(0);
        textureName = texture;
        fadePerSecond = 0;
        this.depth = depth;
    }

    public void update(float deltaTime) {
        System.out.println(fadePerSecond + ", " + sprite.getColor().a);
        if (fadePerSecond != 0) {
            float alpha = sprite.getColor().a;
            alpha += deltaTime * fadePerSecond;
            sprite.setAlpha(alpha);
            if (alpha <= 0) {
                setFullVisible(false);
            } else if (alpha >= 1) {
                setFullVisible(true);
            }
        }
    }

    public void draw(Batch batch) {
        if (sprite == null || removed) {
            return;
        }
        sprite.draw(batch);
    }

    public void setPosition(Vector2 position) {
        sprite.setPosition(position.x, position.y);
    }

    public void setFade(float fadeAmount) {
        fadePerSecond = fadeAmount;
    }

    public void setFullVisible(boolean visible) {
        if (visible) {
            sprite.setAlpha(1);
        } else {
            sprite.setAlpha(0);
            removeFromScene();
        }
        fadePerSecond = 0;
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

    public String getTextureName() {
        return textureName;
    }

    public boolean isRemoved() {
        return removed;
    }

    public int getDepth() {
        return depth;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    @Override
    public int compareTo(SceneImage o) {
        return depth - o.depth;
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
