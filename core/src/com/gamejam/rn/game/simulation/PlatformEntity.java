package com.gamejam.rn.game.simulation;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.gamejam.rn.RNGame;
import com.gamejam.rn.game.PhysicsWrapper;

public class PlatformEntity extends Entity {
	
	private static final Filter filter;
	
	static {
		filter = new Filter();
		filter.categoryBits = RNGame.CATEGORY_SCENERY;
		filter.maskBits = RNGame.MASK_SCENERY;
	}
	
	private Fixture fixture;
	private Body body;
	private float width;
	private float height;
	
	private List<Sprite> sprites;
	
	public PlatformEntity(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	protected void init() {
		Vector2[] verts = new Vector2[1];
		verts[0] = new Vector2(width, height);
		
		fixture = PhysicsWrapper.createFixture(world.getPhysicsWorld(),
				BodyType.StaticBody, Shape.Type.Polygon, position, verts, new float[]{});
		body = fixture.getBody();
		
		body.setUserData(this);
		fixture.setUserData(this);
		fixture.setFilterData(filter);
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		fixture.getBody().setTransform(x, y, 0);
	}
	
	@Override
	public void dispose() {
		world.getPhysicsWorld().destroyBody(body);
	}
	
	@Override
	public boolean isRenderable() {
		return true;
	}
	
	public void addSprite(Sprite sprite) {
		addSprite(sprite, 0f, 0f);
	}
	
	public void addSprite(Sprite sprite, float x, float y) {
		sprites.add(sprite);
		sprite.setOriginCenter();
		sprite.setCenter(body.getWorldCenter().x + x, body.getWorldCenter().y + y);
	}
	
	public void addSprite(Sprite sprite, float x, float y, float width, float height) {
		sprites.add(sprite);
		sprite.setOriginCenter();
		sprite.setScale(width / sprite.getWidth(), height / sprite.getHeight());
		sprite.setCenter(body.getWorldCenter().x + x, body.getWorldCenter().y + y);
	}
	
	@Override
	public void render(Batch batch) {
		for (Sprite s : sprites) {
			s.draw(batch);
		}
	}
	
}
