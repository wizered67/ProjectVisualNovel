package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.Conversations.CompleteEvent;

/**
 * Abstract superclass of all entities drawn and updated in a scene, including CharacterSprites and SceneImages.
 */
public abstract class SceneEntity implements Comparable<SceneEntity> {
    /** The SceneManager that contains this entity. */
    protected SceneManager manager;
    /** The Sprite object used to draw this entity. */
    protected Sprite sprite;
    /** How much the alpha of the sprite being drawn should change per second. */
    protected float fadePerSecond;
    /** The depth to draw this entity's Sprite at in the scene. Higher values are rendered on top of lower values. */
    protected int depth;
    /** Whether the depth has been assigned yet. */
    protected boolean hasDepth;
    /** Whether this entity has been removed. */
    protected boolean removed = false;

    public abstract void reload();

    public void update(float deltaTime) {
        updateFade(deltaTime);
    }

    public void draw(Batch batch) {
        if (shouldDraw()) {
            sprite.draw(batch);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    protected void updateFade(float deltaTime) {
        if (fadePerSecond != 0) {
            float alpha = sprite.getColor().a + (deltaTime * fadePerSecond);
            sprite.setAlpha(alpha);
            if (alpha <= 0) {
                finishVisibility(false);
            } else if (alpha >= 1) {
                finishVisibility(true);
            }
        }
    }

    protected boolean shouldDraw() {
        return isVisible();
    }

    public boolean isVisible() {
        if (sprite == null) {
            return false;
        } else {
            return sprite.getColor().a > 0 && sprite.getTexture() != null && !removed;
        }
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setPosition(Vector2 newPosition) {
        setPosition(newPosition.x, newPosition.y);
    }

    public void setPosition(float newX, float newY) {
        if (sprite == null) {
            return;
        }
        sprite.setPosition(newX, newY);
    }

    public void setX(float newX) {
        if (sprite == null) {
            return;
        }
        sprite.setX(newX);
    }

    public void setY(float newY) {
        if (sprite == null) {
            return;
        }
        sprite.setY(newY);
    }


    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void setDepth(SceneManager m, int newDepth) {
        if (hasDepth) {
            m.removeFromSorted(this);
        }
        depth = newDepth;
        m.addToSorted(this);
        hasDepth = true;
    }

    public void setFade(float fadePerSecond) {
        this.fadePerSecond = fadePerSecond;
    }

    public void finishVisibility(boolean visible) {
        if (fadePerSecond != 0) {
            manager.complete(CompleteEvent.fade(manager, this));
        }
        if (visible) {
            sprite.setAlpha(1);
        } else {
            sprite.setAlpha(0);
            removeFromScene();
        }
        fadePerSecond = 0;
    }

    public abstract void addToScene(SceneManager sm);

    public abstract void removeFromScene();

    @Override
    public int compareTo(SceneEntity o) {
        return depth - o.depth;
    }
}
