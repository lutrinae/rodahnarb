package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Entity implements Disposable {

	protected RNWorld world;
	
	protected Vector2 position;
	
	public Entity() {
		position = new Vector2();
	}	
	
	protected void init() {
	}
	
	public RNWorld getWorld() {
		return world;
	}
	
	/**
	 * package private - only RNWorld should call this
	 * @param world The new world this entity belongs to.
	 */
	void setWorld(RNWorld world) {
		if (world == null) {
			dispose();
			this.world = null;
			return;
		}
		
		if (this.world != null)
			throw new IllegalStateException();
		
		this.world = world;
		init();
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

	@Override
	public void dispose() {
	}
	
}
