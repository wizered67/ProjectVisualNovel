package com.wizered67.game.Entities;

import com.wizered67.game.EntityManager;

import java.util.ArrayList;

/**
 * Created by Adam on 8/13/2016.
 */
public abstract class EnemyEntity extends Entity {
    protected int stunTimer = 0;
    protected boolean stunned = false;
    protected int health = 0;

    public boolean canBeHit(){
        return !stunned;
    }

    public void setStun(int duration){
        stunTimer = duration;
        stunned = true;
    }

    public void damage(int amount){
        health -= amount;
    }
    @Override
    public void updateTimers(){
        if (stunned){
            stunTimer = Math.max(0, stunTimer - 1);
            if (stunTimer <= 0)
                stunned = false;
        }
    }

    public void destroy(){

    }
}
