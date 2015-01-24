package com.gamejam.rn.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.RNGame;

public class SceneryPhysics {

	Filter sceneryFilter;
	
	/**
	 * 
	 * @param world Box2D world
	 * @param position x,y
	 * @param hwvertices vertices with height and width of the polygon or radius of the circle
	 */
	public SceneryPhysics(World world, Vector2 position, Vector2[] hwvertices) {
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = RNGame.CATEGORY_SCENERY;
		sceneryFilter.maskBits = RNGame.MASK_SCENERY;

		Fixture fixture = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, position, hwvertices, new float[]{});
		fixture.getBody().setUserData(this);
		fixture.setUserData(this);
		fixture.setFilterData(sceneryFilter);
		
	}

}