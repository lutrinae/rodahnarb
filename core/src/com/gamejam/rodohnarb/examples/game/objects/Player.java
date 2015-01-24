package com.me.platformer.game.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.me.platformer.Platformer;
import com.me.platformer.camera.SmoothCamSubject;
import com.me.platformer.camera.SmoothCamWorld;

public abstract class Player extends AnimatedPhysics {

	public SmoothCamSubject playerCam;

	public Vector2 curVelocity = new Vector2(0f, 0f);

	public Player(World world, OrthographicCamera camera, String atlasName, Array<String> skeletonSlotsExcludeFromBodies) {
		super(world, camera, atlasName, new Vector2(camera.viewportWidth / 2, camera.viewportHeight / 16), skeletonSlotsExcludeFromBodies, Platformer.CATEGORY_PLAYER,
				Platformer.MASK_PLAYER);

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
		camera.viewportWidth = Platformer.WIDTH * Platformer.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.viewportHeight = Platformer.HEIGHT * Platformer.WORLD_TO_BOX * smoothCamWorld.getZoom();
		camera.update();
	}

	public abstract void moveRight();

	public abstract void moveLeft();

	public abstract void moveStop();

	public abstract void jump();

	// abstract void moveDown();

}