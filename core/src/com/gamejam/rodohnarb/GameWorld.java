package com.gamejam.rodohnarb;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rodohnarb.game.PhysicsWrapper;
import com.gamejam.rodohnarb.game.SceneryPhysics;

public class GameWorld {
	
	Filter sceneryFilter;
	World world;
	
	public GameWorld(Camera camera) {
		
		this.world = new World(new Vector2(0, -10), true);
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = RodohNarb.CATEGORY_SCENERY;
		sceneryFilter.maskBits = RodohNarb.MASK_SCENERY;
		
		float bw = 1.0f; //border width

		SceneryPhysics ground = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)});
		SceneryPhysics ceiling = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,camera.viewportHeight-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		SceneryPhysics wallLeft = new SceneryPhysics(world, new Vector2(bw,camera.viewportHeight/2-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		SceneryPhysics wallRight = new SceneryPhysics(world, new Vector2(camera.viewportWidth-bw,camera.viewportHeight/2), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		
		//box obstacles
		for (int i = 0; i < 20; i++) {
			PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth/4 + (float)(Math.random() * camera.viewportWidth/2), camera.viewportHeight/4 + (float)(Math.random() * camera.viewportHeight/2)), new Vector2[]{new Vector2(.2f,.2f)},new float[]{}).setFilterData(sceneryFilter);
		}
		
	}

	public World getWorld() {
		return world;
	}
}