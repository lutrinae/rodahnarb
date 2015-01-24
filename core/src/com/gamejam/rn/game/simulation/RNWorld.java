package com.gamejam.rn.game.simulation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.gamejam.rn.camera.SmoothCamDebugRenderer;
import com.gamejam.rn.camera.SmoothCamSubject;
import com.gamejam.rn.camera.SmoothCamWorld;

public class RNWorld implements Disposable {
	
	private static final float MAX_PHYSICS_TIMESTEP = 0.016f;
	private static final int PHYSICS_VELOCITY_ITERATIONS = 8; //6
	private static final int PHYSICS_POSITION_ITERATIONS = 3; //2

	private static final float CAMERA_VIEW_HEIGHT = 100;
	
	private static final boolean DEBUG_DRAW_CAMERA = true;
	
	private static final boolean DEBUG_DRAW_PHYSICS = true;
	private static final boolean DEBUG_DRAW_BODIES = true;
	private static final boolean DEBUG_DRAW_JOINTS = true;
	private static final boolean DEBUG_DRAW_AABBS = false;
	private static final boolean DEBUG_DRAW_INACTIVE_BODIES = true;
	private static final boolean DEBUG_DRAW_VELOCITIES = true;
	private static final boolean DEBUG_DRAW_CONTACTS = false;

	private List<Entity> entities;
	private Map<ParallaxLayer, List<Entity>> renderableEntities;

	private World physicsWorld;
	private SmoothCamWorld cameraWorld;
	private SmoothCamSubject cameraSubject;
	private OrthographicCamera camera;
	private float viewportAspect;

	private PolygonSpriteBatch batch;

	private Box2DDebugRenderer physicsDebugRenderer;
	private SmoothCamDebugRenderer cameraDebugRenderer;
	
	public RNWorld() {
		entities = new ArrayList<Entity>();
		renderableEntities = new EnumMap<ParallaxLayer, List<Entity>>(ParallaxLayer.class);
		for (ParallaxLayer layer : ParallaxLayer.cachedValues()) {
			renderableEntities.put(layer, new ArrayList<Entity>());
		}
		
		physicsWorld = new World(new Vector2(0, -9.81f), true);

		cameraSubject = new SmoothCamSubject();
		cameraWorld = new SmoothCamWorld(cameraSubject);
		camera = new OrthographicCamera();
		viewportAspect = camera.viewportWidth / camera.viewportHeight;
		
		batch = new PolygonSpriteBatch();
		
		cameraDebugRenderer = new SmoothCamDebugRenderer();
		physicsDebugRenderer = new Box2DDebugRenderer(
				DEBUG_DRAW_BODIES, DEBUG_DRAW_JOINTS,
				DEBUG_DRAW_AABBS, DEBUG_DRAW_INACTIVE_BODIES,
				DEBUG_DRAW_VELOCITIES, DEBUG_DRAW_CONTACTS);
	}
	
	public void update(float dt) {

		float remaining = dt;
		while (remaining > 0) {
			float d = Math.min(MAX_PHYSICS_TIMESTEP, remaining);
			physicsWorld.step(d, PHYSICS_VELOCITY_ITERATIONS, PHYSICS_POSITION_ITERATIONS);
			//time += d;
			remaining -= d;
		}
	}
	
	public void render() {
		cameraSubject.setPosition(0, 0);
		cameraSubject.setVelocity(0, 0);
		cameraWorld.update();
		
		camera.viewportHeight = CAMERA_VIEW_HEIGHT * cameraWorld.getZoom();
		camera.viewportWidth = viewportAspect * CAMERA_VIEW_HEIGHT * cameraWorld.getZoom();
		
		for (ParallaxLayer layer : ParallaxLayer.cachedValues()) {

			List<Entity> entities = renderableEntities.get(layer);
			if (entities.isEmpty())
				continue;
			
			camera.position.set(cameraWorld.getX(), cameraWorld.getY(), 0);
			camera.update();
			batch.setProjectionMatrix(camera.projection);
			batch.setTransformMatrix(camera.view);
			
			batch.begin();
			
			for (Entity e : entities) {
				e.render(batch);
			}
			
			batch.end();
			
			if (layer == ParallaxLayer.NORMAL) {
			
				if (DEBUG_DRAW_CAMERA)
					cameraDebugRenderer.render(cameraWorld, camera.combined);
			
				if (DEBUG_DRAW_PHYSICS)
					physicsDebugRenderer.render(physicsWorld, camera.combined);
			}
		}
	}
	
	public void resize(int width, int height) {
		viewportAspect = (float)width / height;
	}

	@Override
	public void dispose() {
		batch.dispose();
		physicsWorld.dispose();
	}
	
	public void addEntity(Entity entity) {
		
		if (entity == null)
			return;
		
		RNWorld currentOwner = entity.getWorld();
		if (currentOwner == this) {
			return;
		} else if (currentOwner != null) {
			currentOwner.removeEntity(entity);
		}
		
		entities.add(entity);
		if (entity.isRenderable()) {
			ParallaxLayer layer = entity.getLayer();
			if (layer != null) {
				renderableEntities.get(layer).add(entity);				
			}
		}
	}
	
	public boolean removeEntity(Entity entity) {
		if (entity == null)
			return false;
		
		if (!entities.remove(entity))
			return false;
		
		 if (entity.isRenderable()) {
			ParallaxLayer layer = entity.getLayer();
			if (layer != null) {
				renderableEntities.get(layer).remove(entity);				
			}
		}
		entity.setWorld(null);
		return true;
	}
	
}
