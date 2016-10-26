package com.wizered67.game.Collisions;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Contact Data - Used to store results of collision (Contact object, this Fixture, other Fixture) and pass around to collision handlers
 */
public class ContactData {
	private Contact contact;
	private Fixture thisFixture;
	private Fixture otherFixture;
	
	public ContactData(Contact contact, Fixture t, Fixture o){
		this.contact = contact;
		this.thisFixture = t;
		this.otherFixture = o;
	}
	
	public Fixture getThis(){
		return thisFixture;
	}
	
	public Fixture getOther(){
		return otherFixture;
	}
	
	public Contact getContact(){
		return contact;
	}
}
