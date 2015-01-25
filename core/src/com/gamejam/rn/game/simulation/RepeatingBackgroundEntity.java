package com.gamejam.rn.game.simulation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/* An EnvironmentEntity that repeats itself in the x direction */
public class RepeatingBackgroundEntity extends EnvironmentEntity {

	public RepeatingBackgroundEntity(TextureAtlas atlas, String name, ParallaxLayer layer) {
		super(atlas, name);
		setLayer(layer);
	}
	
	@Override
	public void render(Batch batch) {
		float x = world.getCamera().position.x;
		float w = world.getCamera().viewportWidth;
		
		float spriteWidth = sprite.getWidth() * sprite.getScaleX();
		
		float x0 = x - w * 0.5f;
		float x1 = x + w * 0.5f;
		
		float cx = 0;
		while (x0 < cx) {
			cx -= spriteWidth;
		}
		
		while (cx < x1) {
			sprite.setCenterX(cx + spriteWidth * 0.5f);
			sprite.draw(batch);
			cx += spriteWidth;
		}
	}
}
