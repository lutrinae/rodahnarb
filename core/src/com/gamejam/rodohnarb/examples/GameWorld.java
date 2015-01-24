package com.me.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.me.platformer.game.objects.AnimatedPhysics;
import com.me.platformer.game.objects.PhysicsWrapper;
import com.me.platformer.game.objects.Player;
import com.me.platformer.game.objects.SceneryPhysics;

public class GameWorld {
	
	Filter sceneryFilter;
	World world;
	
	public GameWorld(OrthographicCamera camera) {
		
		this.world = new World(new Vector2(0, -10), true);
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = Platformer.CATEGORY_SCENERY;
		sceneryFilter.maskBits = Platformer.MASK_SCENERY;
		
		float bw = 1.0f; //border width

		SceneryPhysics ground = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)});
		SceneryPhysics ceiling = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,camera.viewportHeight-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		SceneryPhysics wallLeft = new SceneryPhysics(world, new Vector2(bw,camera.viewportHeight/2-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		SceneryPhysics wallRight = new SceneryPhysics(world, new Vector2(camera.viewportWidth-bw,camera.viewportHeight/2), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
//		Fixture ground = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(bw + camera.viewportWidth/2,bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)},new float[]{});
//		Fixture ceiling = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(bw + camera.viewportWidth/2,camera.viewportHeight-bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)},new float[]{});
//		Fixture wallLeft = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(bw,camera.viewportHeight/2-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)},new float[]{});
//		Fixture wallRight = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth-bw,camera.viewportHeight/2), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)},new float[]{});
		
		//ramp
//		PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), new Vector2[]{new Vector2(4f,0),new Vector2(1,2f),new Vector2(-1,2f),new Vector2(-12f,0f)},new float[]{}).setFilterData(filter);

		/*
		float[] vertices = {-0.07421887f, -0.16276085f, -0.12109375f, -0.22786504f, -0.157552f, -0.7122401f, 0.04296875f,
				-0.7122401f, 0.110677004f, -0.6419276f, 0.13151026f, -0.49869835f, 0.08984375f, -0.3190109f};
		Vector2[] pairedVertices = new Vector2[vertices.length/2];
		for (int i = 0; i < vertices.length - 1; i+=2) {
			pairedVertices[i/2]  = new Vector2(vertices[i],vertices[i+1]);
		}
		AnimatedPhysics.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), pairedVertices,new float[]{}).setFilterData(filter);
		*/
		
		//box obstacles
		for (int i = 0; i < 20; i++) {
			PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth/4 + (float)(Math.random() * camera.viewportWidth/2), camera.viewportHeight/4 + (float)(Math.random() * camera.viewportHeight/2)), new Vector2[]{new Vector2(.1f,.1f)},new float[]{}).setFilterData(sceneryFilter);
		}
		
	}

	public World getWorld() {
		return world;
	}
}