package com.gamejam.rodohnarb.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gamejam.rodohnarb.camera.SmoothCamSubject;
import com.gamejam.rodohnarb.camera.SmoothCamWorld;
import com.gamejam.rodohnarb.RodohNarb;

public abstract class Player extends AnimatedPhysics {

	public SmoothCamSubject playerCam;

	public Vector2 curVelocity = new Vector2(0f, 0f);

	public Player(World world, OrthographicCamera camera, String atlasName, Array<String> skeletonSlotsExcludeFromBodies) {
		super(world, camera, atlasName, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), skeletonSlotsExcludeFromBodies, RodohNarb.CATEGORY_PLAYER,
				RodohNarb.MASK_PLAYER);

		playerCam = new SmoothCamSubject();
		playerCam.setVelocityRadius(30f);
		playerCam.setAimingRadius(50f);

	}

	public void updateCamera(SmoothCamWorld smoothCamWorld) {
		playerCam.setPosition(skeleton.getX(), skeleton.getY());
//		playerCam.setVelocity(curVelocity.x, curVelocity.y);
		playerCam.setVelocity(0f, 0f);
		smoothCamWorld.update();

		camera.position.set(smoothCamWorld.getX(), smoothCamWorld.getY(), 0);
		camera.viewportWidth = RodohNarb.WIDTH * RodohNarb.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.viewportHeight = RodohNarb.HEIGHT * RodohNarb.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.update();
	}

	public abstract void moveRight();

	public abstract void moveLeft();

	public abstract void moveStop();

	public abstract void jump();

	// abstract void moveDown();

}