package com.wizered67.game.GUI.Conversations.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GUI.Conversations.CompleteEvent;

/**
 * Abstract superclass of all entities drawn and updated in a scene, including CharacterSprites and SceneImages.
 * Note: this class has a natural ordering that is probably inconsistent with equals.
 * It uses depth in compareTo for ordering but subclasses likely use other conditions for equality.
 * @author Adam Victor
 */
public abstract class SceneEntity implements Comparable<SceneEntity> {
    /** The SceneManager that contains this entity. */
    protected SceneManager manager;
    /** The Sprite object used to draw this entity. */
    protected Sprite sprite;
    /** The depth to draw this entity's Sprite at in the scene. Higher values are rendered on top of lower values. */
    protected int depth;
    /** Whether the depth has been assigned yet. */
    protected boolean hasDepth;
    /** Whether this entity has been removed. */
    protected boolean removed = false;
    /** Fade object used to control fading in and out. */
    protected Fade fade;
    /** Method called to restore state after saved variables are reloaded. */
    public abstract void reload();

    /** Called before serialization to make sure important values are stored into variables to be serialized. */
    public void save() {

    }

    /** Update the fade of the entity. Subclasses can override to update themselves. DELTA TIME is
     * the time elapsed since the last frame.
     */
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

    /** Updates the fade of this entity's sprite. Called every update frame.
     * DELTA TIME is the time elapsed since the last frame.
     */
    protected void updateFade(float deltaTime) {
        if (fade != null) {
            float alpha = fade.update(deltaTime);
            sprite.setAlpha(alpha);
            if (alpha <= 0) {
                finishVisibility(false);
            } else if (alpha >= 1) {
                finishVisibility(true);
            }
        }
    }

    /** Returns whether the sprite should be drawn. By default it draws if visibility conditions
     * are met, but can subclasses can override to provide new behavior.
     */
    protected boolean shouldDraw() {
        return isVisible();
    }

    /** Returns whether the sprite is visible. It is only considered visible if
     * the sprite is not null, the sprite texture is not null, the sprite's alpha is
     * greater than 0, and this entity has not already been removed.
     */
    public boolean isVisible() {
        return !removed && sprite != null && sprite.getColor().a > 0 && sprite.getTexture() != null;
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

    /** Changes the depth of this entity to NEW DEPTH. A reference to the SceneManager must be
     * passed in as M because the entity may not have been assigned one yet.
     * If a depth had already been assigned, this entity must first be removed from the sorted
     * list of entities in the scene, which is O(logN + D), where N is the total number of entities
     * in the list and D is the number of entities with the same depth as this entity's previous depth.
     */
    public void setDepth(SceneManager m, int newDepth) {
        if (hasDepth) {
            m.removeFromSorted(this);
        }
        depth = newDepth;
        m.addToSorted(this);
        hasDepth = true;
    }

    public boolean hasDepth() {
        return hasDepth;
    }

    public void setFade(Fade fade) {
        this.fade = fade;
    }

    /** Called to set the visibility to either full visibility or no visibility. This, in
     * effect, ends any fading going on and resets the fade to null. If it was not null before, a
     * CompleteEvent is fired. VISIBLE is whether this entity is now visible or not. If it is not
     * visible, it must be removed from the scene.
     */
    public void finishVisibility(boolean visible) {
        if (fade != null) {
            manager.complete(CompleteEvent.fade(manager, this));
        }
        if (visible) {
            sprite.setAlpha(1);
        } else {
            sprite.setAlpha(0);
            removeFromScene();
        }
        fade = null;
    }

    public void setSprite(Sprite s) {
        if (sprite == null) {
            sprite = s;
        } else {
            sprite.set(s);
        }
    }

    public abstract void addToScene(SceneManager sm);

    public abstract void removeFromScene();
    /** Compares SceneEntities on the basis of depth. */
    @Override
    public int compareTo(SceneEntity o) {
        return depth - o.depth;
    }
}
