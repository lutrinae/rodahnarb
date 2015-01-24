package com.gamejam.rn.game.simulation;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.camera.SmoothCamDebugRenderer;
import com.gamejam.rn.camera.SmoothCamWorld;

public class RNWorld {

	private static final float MIN_TIMESTEP = 0.016f;
	
	private List<Entity> entities;
	
	
	private World physicsWorld;
	private SmoothCamWorld cameraWorld;
	
	
	private OrthographicCamera camera;
	
	
	private Box2DDebugRenderer physicsDebugRenderer;
	private SmoothCamDebugRenderer cameraDebugRenderer;
	
	
		
	
	public RNWorld() {
		
//		physicsWorld = new World(0, -9.81f, true);
		
//		cameraWorld = new SmoothCamWorld();
		
		
		
		
	}
	
	public void update(float dt) {
		
	}
	
}
