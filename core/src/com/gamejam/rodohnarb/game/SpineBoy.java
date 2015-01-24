package com.gamejam.rodohnarb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;

public class SpineBoy extends Player {

	private float walkSpeed = 0.05f;

	public boolean midAir = false;

	static Array<String> skeletonSlotsExcludeFromBodies = new Array<String>(new String[] { "Eyes", "Neck", "Pelvis", "Arm_Upper_Far",
			"Arm_Upper_Near", "Arm_Lower_Far", "Arm_Lower_Near", "Leg_Upper_Far", "Leg_Lower_Far", "Leg_Upper_Near", "Leg_Lower_Near" });

	public SpineBoy(World world, OrthographicCamera camera) {
		super(world, camera, "spineboy", skeletonSlotsExcludeFromBodies);

		AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
		stateData.setMix("idle", "jump", 0.2f);
		stateData.setMix("idle", "walk", 0.2f);
		stateData.setMix("walk", "jump", 0.2f);
		stateData.setMix("walk", "idle", 0.2f);
		stateData.setMix("jump", "walk", 0.4f);
		stateData.setMix("jump", "idle", 0.4f);
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
			animationState.setAnimation(0, "idle", false);
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

}