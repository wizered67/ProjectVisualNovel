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
    /** Identifier specified in commands to reference this image. */
    private String instanceIdentifier;
    /** Identifier specified in commands to refer to group of images. */
    private String groupIdentifier;
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
    private static int nextInstance;
    static {
        nextInstance = 0;
    }


    public SceneImage() {}

    /** Used to make dummy image at depth 0. Used in SceneManager. */
    public SceneImage(int depth) {
        this.depth = depth;
    }

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
    }

    public void update(float deltaTime) {
        //System.out.println(fadePerSecond + ", " + sprite.getColor().a);
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
        if (sprite == null || sprite.getTexture() == null || sprite.getColor().a == 0 || removed) {
            return;
        }
        sprite.draw(batch);
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

    public void setPosition(Vector2 position) {
        sprite.setPosition(position.x, position.y);
    }

    public void setX(float x) {
        sprite.setPosition(x, sprite.getY());
    }

    public void setY(float y) {
        sprite.setPosition(sprite.getX(), y);
    }

    public void setDepth(SceneManager manager, int d) {
        depth = d;
        manager.addImageToSorted(this);
    }

    public void setFade(float fadeAmount) {
        fadePerSecond = fadeAmount;
    }

    public void setFullVisible(boolean visible) {
        if (fadePerSecond != 0) {
            manager.complete(CompleteEvent.fade()); //todo add fade data
        }
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
