package com.gamejam.rn.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gamejam.rn.RNGame;
import com.gamejam.rn.camera.SmoothCamSubject;
import com.gamejam.rn.camera.SmoothCamWorld;

public abstract class Player extends AnimatedPhysics {

	public SmoothCamSubject playerCam;

	public Vector2 curVelocity = new Vector2(0f, 0f);

	/**
	 * @param world
	 * @param camera
	 * @param atlasName
	 * @param skeletonSlotsBodiesSubset
	 */
	public Player(World world, Camera camera, String atlasName, Array<String> skeletonSlotsBodiesSubset) {
		super(world, camera, atlasName, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), skeletonSlotsBodiesSubset, RNGame.CATEGORY_PLAYER,
				RNGame.MASK_PLAYER);

		playerCam = new SmoothCamSubject();
		playerCam.setVelocityRadius(30f);
		playerCam.setAimingRadius(50f);
	}
	
	/**
	 * @param world
	 * @param camera
	 * @param atlasName
	 * @param skeletonSlotsBodiesSubset
	 * @param scale float to scale Spine character to our World's units based on Box2D's 1 meter.  Spineboy uses 0.006f
	 */
	public Player(World world, Camera camera, String atlasName, Array<String> skeletonSlotsBodiesSubset, float scale) {
		super(world, camera, atlasName, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), skeletonSlotsBodiesSubset, RNGame.CATEGORY_PLAYER,
				RNGame.MASK_PLAYER, scale);

		playerCam = new SmoothCamSubject();
		playerCam.setVelocityRadius(30f);
		playerCam.setAimingRadius(50f);

	}

	public void updateCamera(SmoothCamWorld smoothCamWorld) {
		playerCam.setPosition(skeleton.getX(), skeleton.getY());
//		playerCam.setVelocity(curVelocity.x, curVelocity.y);
		playerCam.setVelocity(0f, 0.1f);
		smoothCamWorld.update();

		camera.position.set(smoothCamWorld.getX(), smoothCamWorld.getY(), 0);
		camera.viewportWidth = RNGame.WIDTH * RNGame.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.viewportHeight = RNGame.HEIGHT * RNGame.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.update();
	}

	public abstract void moveRight();

	public abstract void moveLeft();

	public abstract void moveStop();

	public abstract void jump();

	public abstract void crouch();

}