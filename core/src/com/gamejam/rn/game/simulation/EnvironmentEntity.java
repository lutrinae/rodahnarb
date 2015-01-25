package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnvironmentEntity extends Entity {

	Sprite sprite;
	ParallaxLayer layer;

	public EnvironmentEntity(TextureAtlas atlas, String name) {
		layer = ParallaxLayer.NORMAL;
		this.sprite = atlas.createSprite(name);
		this.sprite.setOriginCenter();
		this.sprite.setCenterX(position.x);
		this.sprite.setCenterY(position.y);
	}
	
	public EnvironmentEntity(TextureAtlas atlas, String name, float x, float y, float width, float height) {
		layer = ParallaxLayer.NORMAL;
		sprite = atlas.createSprite(name);
		sprite.setOriginCenter();
		sprite.setScale(width / sprite.getWidth(), height / sprite.getHeight());
		sprite.setCenterX(position.x);
		sprite.setCenterY(position.y);
	}
	
	public EnvironmentEntity(TextureRegion sprite) {
		layer = ParallaxLayer.NORMAL;
		this.sprite = new Sprite(sprite);
		this.sprite.setCenterX(position.x);
		this.sprite.setCenterY(position.y);
	}

	@Override
	public boolean isRenderable() {
		return true;
	}

	@Override
	public void render(Batch batch) {
		
		sprite.draw(batch);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		sprite.setCenterX(position.x);
		sprite.setCenterY(position.y);
	}
	
	public void setSize(float width, float height) {
		sprite.setScale(width / sprite.getWidth(), height / sprite.getHeight());
	}
	
	public void setLayer(ParallaxLayer layer) {
		if (layer == null)
			return;
		if (layer != this.layer) {
			if (world != null)
				world.changeLayers(this, this.layer, layer);
			
			this.layer = layer;
		}
		
	}
	
	@Override
	public ParallaxLayer getLayer() {
		return layer;
	}
	
}
