package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Entity {

	protected RNWorld world;
	
	protected Vector2 position;
	
	public Entity(RNWorld world) {
		position = new Vector2();
		if (world != null) {
			world.addEntity(this);
		}
	}
	
	public RNWorld getWorld() {
		return world;
	}
	
	/**
	 * package private - only RNWorld should call this
	 * @param world The new world this entity belongs to.
	 */
	void setWorld(RNWorld world) {
		this.world = world;
	}
	
	public boolean isRenderable() {
		return false;
	}
	
	public ParallaxLayer getLayer() {
		return ParallaxLayer.NORMAL;
	}
	
	public void render(Batch batch) {
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
}
