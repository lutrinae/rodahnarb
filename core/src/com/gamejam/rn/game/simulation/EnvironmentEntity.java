package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnvironmentEntity extends StaticEntity {

	Sprite sprite;

	public EnvironmentEntity(RNWorld world, TextureAtlas atlas, String name) {
		super(world);
		this.sprite = atlas.createSprite(name);
		this.sprite.setOriginCenter();
		this.sprite.setCenterX(position.x);
		this.sprite.setCenterY(position.y);
	}
	
	public EnvironmentEntity(RNWorld world, TextureRegion sprite) {
		super(world);
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
		sprite.setSize(width, height);
	}

}
