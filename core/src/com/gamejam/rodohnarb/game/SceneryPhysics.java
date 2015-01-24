package com.gamejam.rodohnarb.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rodohnarb.RodohNarb;

public class SceneryPhysics {

	Filter sceneryFilter;
	
	public SceneryPhysics(World world, Vector2 position, Vector2[] vertices) {
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = RodohNarb.CATEGORY_SCENERY;
		sceneryFilter.maskBits = RodohNarb.MASK_SCENERY;

		Fixture fixture = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, position, vertices, new float[]{});
		fixture.getBody().setUserData(this);
		fixture.setUserData(this);
		fixture.setFilterData(sceneryFilter);
		
	}

}