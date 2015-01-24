package com.gamejam.rn.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;

public class Hero extends Player {

	private float walkSpeed = 0.05f;

	public boolean midAir = false;

	static Array<String> skeletonSlotsExcludeFromBodies = new Array<String>(new String[] { "INCLUDE", "weapon", "eyes", "body", "foot1", "foot2"});

	public Hero(World world, Camera camera) {
		super(world, camera, "hero", skeletonSlotsExcludeFromBodies, 0.012f);

		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setDefaultMix(0.1f);
//		stateData.setMix("idle", "jump", 0.2f);
//		stateData.setMix("idle", "walk", 0.2f);
//		stateData.setMix("idle", "crouch", 0.2f);
//		stateData.setMix("walk", "jump", 0.1f);
//		stateData.setMix("walk", "idle", 0.2f);
//		stateData.setMix("jump", "walk", 0.4f);
//		stateData.setMix("jump", "idle", 0.4f);
		createAnimationState(stateData);

		animationState.setAnimation(0, "idle", true);
	}

	public void animate(float time, float delta) {
		startAnimate(time);

		skeleton.setX(skeleton.getX() + curVelocity.x);
		
		finishAnimate(time, delta);
	}

	@Override
	public void moveRight() {
		
		if (curVelocity.x == 0) {
			curVelocity.x = walkSpeed;
		} else if (curVelocity.x < 0) {
			curVelocity.x *= -1;
		}

		if (skeleton.getFlipX()) {
			skeleton.setFlipX(false);
		}

		animationState.setAnimation(0, "walk", true);
		
	}

	@Override
	public void moveLeft() {

		if (curVelocity.x == 0) {
			curVelocity.x = -walkSpeed;
		} else if (curVelocity.x > 0) {
			curVelocity.x *= -1;
		}
		
		if (!skeleton.getFlipX()) {
			skeleton.setFlipX(true);
		}
		
		animationState.setAnimation(0, "walk", true);
	}

	@Override
	public void moveStop() {
		if (curVelocity.x != 0) {
			curVelocity.x = 0f;
		}
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("idle")) {
			animationState.setAnimation(0, "idle", true);
			Gdx.app.log("Animaiton state:", animationState.getCurrent(0).toString());
		}
	}

	@Override
	public void jump() {
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("jump")) {
			
			animationState.setAnimation(0, "jump", false);
			if (curVelocity.x != 0) {
				animationState.addAnimation(0, "walk", true, 0);
			} else {
				animationState.addAnimation(0, "idle", true, 0);
			}
		}
	}
	
	private boolean checkFeetOnGround(Fixture fixtureA, Fixture fixtureB) {

		if (fixtureA.getUserData() != null && fixtureA.getBody().getUserData() != null
				&& fixtureA.getUserData() instanceof AnimatedPhysics.Box2dRegionAttachment && fixtureA.getBody().getUserData() instanceof Player) {
			AnimatedPhysics.Box2dRegionAttachment attachment = (AnimatedPhysics.Box2dRegionAttachment) fixtureA.getUserData();
			if (attachment.getName().equals("Foot_Far") || attachment.getName().equals("Foot_Near")) {
				if (fixtureB.getBody().getUserData() != null && fixtureA.getBody().getUserData() instanceof SceneryPhysics) {
					//TODO check if contact surface is vertical or horizontal.  If vert, return false
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void crouch() {
		moveStop();
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("crouch")) {
			animationState.setAnimation(0, "crouch", true);
			Gdx.app.log("Animaiton state:", animationState.getCurrent(0).toString());
		}
		
	}

}