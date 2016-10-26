package com.wizered67.game.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.wizered67.game.Collisions.ContactData;
import com.wizered67.game.Constants;
import com.wizered67.game.WorldManager;

/**
 * Created by Adam on 8/2/2016.
 */
public class CircleEntity extends Entity {

    public CircleEntity(){
        super();
        makeBody();
    }

    public void makeBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(Constants.toMeters(50), Constants.toMeters(50));
        bdef.fixedRotation = true;
        body = WorldManager.world.createBody(bdef);

        CircleShape circ = new CircleShape();
        circ.setPosition(new Vector2(0, 0));
        circ.setRadius(Constants.toMeters(16));
        FixtureDef mb = new FixtureDef();
        mb.shape = circ;
        mb.density = 0.5f;
        mb.friction = 0f;
        mb.restitution = 0;
        mb.filter.categoryBits = Constants.CATEGORY_SCENERY;
        mb.filter.maskBits = Constants.MASK_SCENERY;
        body.createFixture(mb);
        circ.dispose();
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
