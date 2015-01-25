package com.gamejam.rn.game.simulation;

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
	float width;
	float height;
	
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
	
}
