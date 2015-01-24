package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.math.Vector3;

public class Entity {

	protected RNWorld world;
	
	protected Vector3 position;
	
	
	public Entity(RNWorld world) {
		this.world = world;
	}
	
}
