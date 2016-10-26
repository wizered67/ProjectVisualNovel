package com.wizered67.game.Entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.wizered67.game.Collisions.ContactData;
import com.wizered67.game.Constants;
import com.wizered67.game.Inputs.InputInfo;

import java.util.HashMap;

/**
 * Created by Adam on 7/15/2016.
 */
public abstract class Entity {
    protected Body body;
    protected Sprite sprite;
    protected float boundingWidth;
    protected float boundingHeight;
    protected int depth;
    protected boolean destroyed = false;
    protected boolean isAnimated = false;
    protected AnimationProcessor animationProcessor;
    protected HashMap<Integer, InputInfo> inputs;
    protected Vector2 position;
    protected boolean persistant = false;

    public Entity() {
        animationProcessor = new AnimationProcessor(this);
        inputs = new HashMap<Integer, InputInfo>();
        sprite = new Sprite();
    }

    public void makeBody(){
        if (body != null)
            position = body.getPosition();
    }

    public void setTransform(Vector2 position, int angle) {body.setTransform(position, angle);}
    public Vector2 getPosition() {return position;}
    public Vector2 getBodyPosition(){
        return body.getPosition();
    }
    public Vector2 getVelocity(){
        return body.getLinearVelocity();
    }
    public float getX(){
        return body.getPosition().x;
    }
    public float getY(){
        return body.getPosition().y;
    }
    public float getWidth(){ return sprite.getWidth() * sprite.getScaleX();}
    public float getHeight(){
        return sprite.getHeight() * sprite.getScaleY();
    }
    public float getBoundingWidth(){
        return boundingWidth;
    }
    public float getBoundingHeight(){
        return boundingHeight;
    }
    public int getDepth() {return depth; }
    public boolean getPersistant(){return persistant; }
    public boolean stayActive() {return false; }
    public abstract void beginContact(ContactData c);
    public abstract void endContact(ContactData c);
    public abstract void preSolveCollision(ContactData c, Manifold m);
    public abstract void postSolveCollision(ContactData c, ContactImpulse impulse);

    public void update(float delta){
        updatePhysics(delta);
        updateTimers();
        if (isAnimated)
        {
            animationProcessor.update(delta);
        }
        updateSprite();
    }
    public boolean getInputPressed(int key, boolean justPressed){
        if (!inputs.containsKey(key)){ return false; }
        if (inputs.get(key) == null){ return false; }
        if (!justPressed && inputs.get(key).touched){ return true;}
        if (justPressed && inputs.get(key).justTouched){return true;}
        return false;
    }

    public void updateSprite(){
        sprite.setPosition(Constants.toPixels(getPosition().x) - getWidth() / (2 * sprite.getScaleX()), Constants.toPixels(getPosition().y - getBoundingHeight() / (2 * sprite.getScaleY())));
    }

    public void setInput(int index, InputInfo result){
        inputs.put(index, result);
    }

    public InputInfo getInput(Input index){
        return inputs.get(index);
    }

    public abstract void updatePhysics(float delta);
    public abstract void updateTimers();

    public void chooseAnimation() {}
    public Sprite getSprite() {return sprite;}
    public Vector2 getDrawOffset() {return new Vector2(0, 0);}
    public boolean equals(Entity e) {return e == this;}
    public Body getBody() {return body;}
    public void setDestroyed(boolean d) {destroyed = d;}
    public boolean getDestroyed() {return destroyed;}
    public abstract void destroy();
}
