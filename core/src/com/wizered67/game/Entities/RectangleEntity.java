package com.wizered67.game.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.wizered67.game.Collisions.ContactData;
import com.wizered67.game.WorldManager;

/**
 * Created by Adam on 8/2/2016.
 */
public class RectangleEntity extends Entity {
    float x, y, width, height;
    public RectangleEntity(float x, float y, float width, float height){
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        WorldManager.addNewObjectBody(this);
        //makeBody(x, y, width, height);
    }

    public void makeBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);
        bdef.fixedRotation = true;
        body = WorldManager.world.createBody(bdef);

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(width / 2, height / 2, new Vector2(0,0), 0);
        FixtureDef mb = new FixtureDef();
        mb.isSensor = true;
        mb.shape = rect;
        mb.density = 0.5f;
        mb.friction = 0f;
        mb.restitution = 0;
        //mb.filter.categoryBits = Constants.CATEGORY_SCENERY;
        //mb.filter.maskBits = Constants.MASK_SCENERY;
        body.createFixture(mb);
        rect.dispose();
        super.makeBody();

    }

    @Override
    public void beginContact(ContactData c) {

    }

    @Override
    public void endContact(ContactData c) {

    }

    @Override
    public void preSolveCollision(ContactData c, Manifold m) {

    }

    @Override
    public void postSolveCollision(ContactData c, ContactImpulse impulse) {

    }

    @Override
    public void updatePhysics(float delta) {

    }

    @Override
    public void updateTimers() {

    }

    @Override
    public void destroy() {

    }
}
