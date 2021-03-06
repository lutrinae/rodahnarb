package com.gamejam.rn.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;

public class Hero extends Player {

	private float walkSpeed = 0.05f;
	private float runSpeed = 0.15f;

	public boolean midAir = false;
	public boolean sprinting = false;
	public boolean walking = false;
	
	Bone head;

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
		
		head = skeleton.findBone("head");
	}

	public void animate(float time, float delta) {
		startAnimate(time);

		skeleton.setX(skeleton.getX() + curVelocity.x);
		skeleton.setY(skeleton.getY() + curVelocity.y);
//		head.setScale(5);
		
		updateAnimate(delta);
		lookAtMouse();
		finishAnimate(time);
		
	}
	
	private void lookAtMouse() {
		Vector3 mouseDirection = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0)).sub(skeleton.getX(), skeleton.getY() + 3,0);
		Vector2 mouseDirection2 = new Vector2 (mouseDirection.x, mouseDirection.y);;
		float rawMouseAngle = mouseDirection2.angle();
		mouseDirection2.x = Math.abs(mouseDirection2.x);
		float localMouseAngle =  mouseDirection2.angle();
		float rotateHead = 90;
		
		if (rawMouseAngle > 180) {
			rawMouseAngle -= 360;
			localMouseAngle -= 360;
		}
		rotateHead = localMouseAngle;

		if (localMouseAngle > 45)
			rotateHead = 45;
		if (localMouseAngle < -45)
			rotateHead = -45;
		Gdx.app.log("Angle", "" + (rawMouseAngle));
		
		head.setRotation(rotateHead - 90);
		
		if (rawMouseAngle > 90 || rawMouseAngle < -90) {
			head.setFlipX(!skeleton.getFlipX());
		}
		if (rawMouseAngle < 90 && rawMouseAngle > -90) {
			head.setFlipX(skeleton.getFlipX());
		}
	}
	
	

	@Override
	public void moveRight(boolean doMove) {
		
		if (curVelocity.x == walkSpeed || curVelocity.x == runSpeed || sprinting) {
			curVelocity.x = runSpeed;
			animationState.setAnimation(0, "run", true);
		}

		if (curVelocity.x == 0) {
			curVelocity.x = walkSpeed;
			animationState.setAnimation(0, "walk", true);
		} else if (curVelocity.x < 0) {
			curVelocity.x = walkSpeed;
			animationState.setAnimation(0, "walk", true);
		}

		if (skeleton.getFlipX()) {
			skeleton.setFlipX(false);
		}

	}
	
	@Override
	public void moveLeft(boolean doMove) {

		if (curVelocity.x == -walkSpeed || curVelocity.x == -runSpeed  || sprinting) {
			curVelocity.x = -runSpeed;
			animationState.setAnimation(0, "run", true);
		}
		if (curVelocity.x == 0) {
			curVelocity.x = -walkSpeed;
			animationState.setAnimation(0, "walk", true);
		} else if (curVelocity.x > 0) {
			curVelocity.x = -walkSpeed;
			animationState.setAnimation(0, "walk", true);
		}

		if (!skeleton.getFlipX()) {
			skeleton.setFlipX(true);
		}
		
	}

	@Override
	public void stop() {
		if (curVelocity.x != 0) {
			curVelocity.x = 0f;
		}
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("idle")) {
			animationState.setAnimation(0, "idle", true);
			Gdx.app.log("Animaiton state:", animationState.getCurrent(0).toString());
		}
	}

	@Override
	public void jump(boolean doJump) {
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("jump")) {
			
			animationState.setAnimation(0, "jump", false);
			if (curVelocity.x == walkSpeed || curVelocity.x == -walkSpeed) {
				animationState.addAnimation(0, "walk", true, 0);
			} else if (curVelocity.x == runSpeed || curVelocity.x == -runSpeed) {
				animationState.addAnimation(0, "run", true, 0);
			} else {
				animationState.addAnimation(0, "idle", true, 0);
			}
		}
	}
	
	@Override
	public void crouch(boolean doCrouch) {
		stop();
		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("crouch")) {
			animationState.setAnimation(0, "crouch", true);
			Gdx.app.log("Animaiton state:", animationState.getCurrent(0).toString());
		}
		
	}

	@Override
	public void sprint(boolean doSprint) {
		sprinting = doSprint;
	}
	
	private boolean checkFeetOnGround(Fixture fixtureA, Fixture fixtureB) {

		if (fixtureA.getUserData() != null && fixtureA.getBody().getUserData() != null
				&& fixtureA.getUserData() instanceof AnimatedPhysics.Box2dRegionAttachment && fixtureA.getBody().getUserData() instanceof Player) {
			AnimatedPhysics.Box2dRegionAttachment attachment = (AnimatedPhysics.Box2dRegionAttachment) fixtureA.getUserData();
			if (attachment.getName().equals("foot1") || attachment.getName().equals("foot2")) {
				if (fixtureB.getBody().getUserData() != null && fixtureA.getBody().getUserData() instanceof SceneryPhysics) {
					//TODO check if contact surface is vertical or horizontal.  If vert, return false
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void look(boolean doLook) {
		
		
//		if (animationState.getCurrent(0) == null || !animationState.getCurrent(0).toString().equals("headturn")) {
//			animationState.setAnimation(0, "headturn", true);
//			Gdx.app.log("Animaiton state:", animationState.getCurrent(0).toString());
//		}
		
	}

	@Override
	public void primaryFire(boolean doPrimaryFire) {
		// TODO Auto-generated method stub
		
	}


}