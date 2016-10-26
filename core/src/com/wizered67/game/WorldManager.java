package com.wizered67.game;

import com.badlogic.gdx.physics.box2d.World;
import com.wizered67.game.Collisions.MyCollisionListener;
import com.wizered67.game.Entities.Entity;

import java.util.ArrayList;

public class WorldManager {
	public static World world;
	public static ArrayList<Entity> addObjects = new ArrayList<Entity>();
	public static void init(){
		World.setVelocityThreshold(Constants.VELOCITY_THRESHOLD);
		world = new World(Constants.DEFAULT_GRAVITY_VECTOR, true);
		world.setContactListener(new MyCollisionListener());
	}

	public static void addNewObjectBody(Entity e){
		addObjects.add(e);
	}

	public static void makeAllBodies(){
		for (Entity e : addObjects){
			e.makeBody();
		}
		addObjects.clear();
	}

	public static void worldStep(){
		world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
	}
}
