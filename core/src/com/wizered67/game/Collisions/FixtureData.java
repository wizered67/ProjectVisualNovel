package com.wizered67.game.Collisions;

import com.wizered67.game.Enums.Attributes;
import com.wizered67.game.Enums.Fixtures;

import java.util.HashMap;

public class FixtureData {
	private HashMap<Attributes, String> attributes;
	private Fixtures fixtureType;
	
	public FixtureData(Fixtures type){
		fixtureType = type;
		attributes = new HashMap<Attributes, String>();
		for (Attributes a : Attributes.values()){
			attributes.put(a, "");
		}
	}
	
	public Fixtures getType(){
		return fixtureType;
	}
	
	public void setType(Fixtures type){
		fixtureType = type;
	}
	
	public void setAttribute(Attributes attr, String value){
		attributes.put(attr, value);
	}
	
	public String getAttribute(Attributes attr){
		return attributes.get(attr);
	}
	
	public boolean hasAttribute(Attributes attr){
		return (attributes.get(attr) != null && attributes.get(attr) != "");
	}
	
}
