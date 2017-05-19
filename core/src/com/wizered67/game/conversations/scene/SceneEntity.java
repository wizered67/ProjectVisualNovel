package com.wizered67.game.conversations.scene;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.wizered67.game.GameManager;
import com.wizered67.game.conversations.CompleteEvent;
import com.wizered67.game.conversations.ConversationController;
import com.wizered67.game.conversations.scene.interpolations.FloatInterpolation;
import com.wizered67.game.conversations.scene.interpolations.PositionInterpolation;
import com.wizered67.game.scripting.GameScript;
import com.wizered67.game.scripting.ScriptManager;

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
    /** FloatInterpolation object used to control fading in and out. */
    protected FloatInterpolation fade;
    /** PositionInterpolation object used to control position change interpolation. */
    protected PositionInterpolation positionInterpolation;

    /** Animation object containing all frames of animation. */
    protected transient Animation<TextureRegion> currentAnimation;
    /** The name of the current animation. Used to tell if the animation is being changed. */
    protected String animationName;
    /** Whether the Animation should loop. */
    protected boolean looping;
    /** The stateTime used by Animation object to determine which frame should be displayed. */
    protected float stateTime;
    /** Whether the animation being displayed has been finished. */
    protected boolean wasFinished;

    public SceneEntity() {
        looping = false;
        stateTime = 0;
        animationName = "";
    }

    /** Method called to restore state after saved variables are reloaded. */
    public void reload() {
        currentAnimation = GameManager.assetManager().getAnimation(animationName);
        updateSprite();
    }

    /** Called before serialization to make sure important values are stored into variables to be serialized. */
    public void save() {

    }

    /** Update the fade of the entity. Subclasses can override to update themselves. DELTA TIME is
     * the time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        updateFade(deltaTime);
        updatePosition(deltaTime);
        updateAnimations(deltaTime);
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
            if (fade.isDone()) {
                if (alpha <= 0.5) {
                    finishVisibility(false);
                } else if (alpha >= 0.5) {
                    finishVisibility(true);
                }
            }
        }
    }

    /** Updates the position of this entity's sprite. Called every update frame.
     * DELTA TIME is the time elapsed since the last frame.
     */
    protected void updatePosition(float deltaTime) {
        if (positionInterpolation != null) {
            Vector2 newPos = positionInterpolation.update(deltaTime);
            sprite.setPosition(newPos.x, newPos.y);
            if (positionInterpolation.isDone()) {
               finishPositionInterpolation();
            }
        }
    }

    /** Updates the entity's animation and sets the sprite to the current frame. */
    protected void updateAnimations(float deltaTime) {
        if (currentAnimation != null) { //todo decide if should check visibility first
            stateTime += deltaTime;
            updateSprite();
            callAnimationScript();
            //sprite.setCenter(sprite.getWidth() * scale / 2, 0);
            if (currentAnimation.isAnimationFinished(stateTime) && !wasFinished) {
                manager.complete(CompleteEvent.animationEnd(animationName));
                wasFinished = true;
            }
        }
    }

    private void callAnimationScript() {
        GameScript animationScript = GameManager.assetManager().getAnimationScript(animationName);
        if (animationScript != null) {
            animationScript.execute(sprite, stateTime, currentAnimation.getKeyFrameIndex(stateTime));
        }
    }

    /** Ends the position interpolation and sends a CompleteEvent. */
    private void finishPositionInterpolation() {
        positionInterpolation = null;
        manager.complete(CompleteEvent.interpolation(manager, this, CompleteEvent.InterpolationEventType.POSITION));
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
    //todo split into 2 methods? one changes variable and other updates.
    public void setDepth(SceneManager m, int newDepth) {
        if (hasDepth) {
            m.removeFromSorted(this);
        }
        depth = newDepth;
        m.addToSorted(this);
        hasDepth = true;
    }

    public int getDepth() {
        return depth;
    }
    /** Sets the interpolation used for fading to FADE. */
    public void setFade(FloatInterpolation fade) {
        this.fade = fade;
    }
    /** Sets the interpolation used for changing position to INTERPOLATION. */
    public void setPositionInterpolation(PositionInterpolation interpolation) {
        positionInterpolation = interpolation;
    }

    /** Called to set the visibility to either full visibility or no visibility. This, in
     * effect, ends any fading going on and resets the fade to null. If it was not null before, a
     * CompleteEvent is fired. VISIBLE is whether this entity is now visible or not. If it is not
     * visible, it must be removed from the scene.
     */
    public void finishVisibility(boolean visible) {
        if (fade != null) {
            manager.complete(CompleteEvent.interpolation(manager, this, CompleteEvent.InterpolationEventType.FADE));
        }
        if (visible) {
            sprite.setAlpha(1);
        } else {
            sprite.setAlpha(0);
            removeFromScene();
        }
        fade = null;
    }

    /** Switches this SceneCharacter's animation to the one named NAME. Returns true if animation is valid. */
    public boolean setCurrentAnimation(String name) {
        if (!animationName.equalsIgnoreCase(name)) {
            stateTime = 0;
            animationName = name;
        }
        currentAnimation = GameManager.assetManager().getAnimation(name);
        wasFinished = false;
        if (currentAnimation == null) {
            GameManager.error("No animation found: " + name);
        }
        //update(0);
        /*
        if (!isVisible()) {
            return false;
        }
        */
        return currentAnimation != null;
    }
    /** Returns the name of this SceneCharacter's current Animation. */
    public String getAnimationName() {
        return animationName;
    }

    /** Updates the sprite to the correct animation frame. */
    protected void updateSprite() {
        if (currentAnimation != null) {
            TextureRegion currentTexture = currentAnimation.getKeyFrame(stateTime, looping);
            sprite.setTexture(currentTexture.getTexture());
            sprite.setRegion(currentTexture);
            sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
            sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            //sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth() * scale.x, currentSprite.getRegionHeight() * scale.y);
            sprite.setOrigin(Math.abs(sprite.getWidth()) / 2, 0);
        }
    }
    /** Sets this SceneCharacter's current sprite to the TextureRegion TEXTURE. */
    public void setCurrentSprite(TextureRegion texture) {
        TextureRegion currentTexture = texture;
        sprite.setTexture(currentTexture.getTexture());
        sprite.setRegion(currentTexture);
        //sprite.setBounds(position.x, position.y, currentSprite.getRegionWidth(), currentSprite.getRegionHeight());
        sprite.setOrigin(sprite.getWidth() / 2, 0);
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
