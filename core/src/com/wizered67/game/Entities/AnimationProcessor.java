package com.wizered67.game.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.wizered67.game.Enums.Animations;

import java.util.HashMap;

/**
 * Created by Adam on 7/17/2016.
 */
public class AnimationProcessor {
    private Entity entity;
    private Animations animationType;
    private Animation currentAnimation;
    private HashMap<Animations, Animation> animations = new HashMap<Animations, Animation>();
    private float animTimer = 0;

    public AnimationProcessor(Entity e){
        entity = e;
    }

    public void addAnimation(Animations type, Animation anim){
        animations.put(type, anim);
    }

    public void update(float delta){
        if (entity != null) {
            entity.chooseAnimation();
            updateAnimationTimer(delta);
            updateSprite();
        }
    }

    public void updateAnimationTimer(float delta){
        animTimer += delta;
    }
    public void updateSprite(){
        //entity.sprite = currentAnimation.getKeyFrame(animTimer, true);
        //entity.getSprite().setRegion(currentAnimation.getKeyFrame(animTimer, true));
        //System.out.println("Method 1: " + entity.getSprite().getRegionX() + ", " + entity.getSprite().getRegionY());
        entity.sprite = new Sprite(currentAnimation.getKeyFrame(animTimer, true));
        //System.out.println("Method 2: " + entity.getSprite().getRegionX() + ", " + entity.getSprite().getRegionY());
    }

    public void setAnimation(Animations anim){
        if (anim != animationType){
            animTimer = 0;
        }
        animationType = anim;
        currentAnimation = animations.get(anim);
    }

    public Animations getAnimation(){
        return animationType;
    }

    public void setEntity(Entity e){ entity = e; }
    public Entity getEntity() {return entity; }

}
