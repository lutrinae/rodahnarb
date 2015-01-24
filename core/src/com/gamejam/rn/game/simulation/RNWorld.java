package com.gamejam.rn.game.simulation;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.camera.SmoothCamDebugRenderer;
import com.gamejam.rn.camera.SmoothCamSubject;
import com.gamejam.rn.camera.SmoothCamWorld;

public class RNWorld {

	private static final float MIN_PHYSICS_TIMESTEP = 0.016f;

	static final int PHYSICS_VELOCITY_ITERATIONS=8; //6
	static final int PHYSICS_POSITION_ITERATIONS=3; //2
	
	private static final boolean DEBUG_DRAW_BODIES = true;
	private static final boolean DEBUG_DRAW_JOINTS = true;
	private static final boolean DEBUG_DRAW_AABBS = false;
	private static final boolean DEBUG_DRAW_INACTIVE_BODIES = true;
	private static final boolean DEBUG_DRAW_VELOCITIES = true;
	private static final boolean DEBUG_DRAW_CONTACTS = false;

	private List<Entity> entities;

	private World physicsWorld;
	private SmoothCamWorld cameraWorld;
	private SmoothCamSubject cameraSubject;
	private OrthographicCamera camera;
	
	private PolygonSpriteBatch spineSpriteBatch;
	private SpriteBatch environmentSpriteBatch;

	private Box2DDebugRenderer physicsDebugRenderer;
	private SmoothCamDebugRenderer cameraDebugRenderer;

	public RNWorld() {

		physicsWorld = new World(new Vector2(0, -9.81f), true);
		physicsDebugRenderer = new Box2DDebugRenderer(
				DEBUG_DRAW_BODIES, DEBUG_DRAW_JOINTS,
				DEBUG_DRAW_AABBS, DEBUG_DRAW_INACTIVE_BODIES,
				DEBUG_DRAW_VELOCITIES, DEBUG_DRAW_CONTACTS);

		cameraSubject = new SmoothCamSubject();
		cameraWorld = new SmoothCamWorld(cameraSubject);

		cameraDebugRenderer = new SmoothCamDebugRenderer();
		
		spineSpriteBatch = new PolygonSpriteBatch();
		
		environmentSpriteBatch = new SpriteBatch();
		
		
		
	}

	public void update(float dt) {
		float remaining = dt;
		while (remaining > 0) {
			float d = Math.min(0.016f, remaining);
			physicsWorld.step(d, PHYSICS_VELOCITY_ITERATIONS, PHYSICS_POSITION_ITERATIONS);
			//time += d;
			remaining -= d;
		}
	}
	
}
