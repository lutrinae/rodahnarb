package com.gamejam.rodohnarb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.gamejam.rodohnarb.RodohNarb;

public class Robot {
	// public class Player {

	Fixture baseFixture;
	Fixture springFixture;
	Fixture wheelFixture;
	PrismaticJoint springJoint;
	RevoluteJoint wheelJoint;
	int ammo;
	long lastFired;
	long lastJumped;
	Filter filter;
	OrthographicCamera camera;

	public Robot(World world, OrthographicCamera camera) {

		this.camera = camera;
		baseFixture = PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth / 2,
				camera.viewportHeight / 8), new Vector2[] { new Vector2(.5f, 1.75f) }, new float[] { 1.0f, 1.0f, 1.0f });
		springFixture = PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth / 2,
				camera.viewportHeight / 8), new Vector2[] { new Vector2(.25f, .75f) }, new float[] { 1.0f, 1.0f, 1.0f });
		wheelFixture = PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Circle, new Vector2(camera.viewportWidth / 2,
				camera.viewportHeight / 8), new Vector2[] { new Vector2(.5f, 1f) }, new float[] { 1.0f, 1.0f, 1.0f });
		baseFixture.setUserData(this);
		baseFixture.getBody().setFixedRotation(true);

		lastFired = System.currentTimeMillis();
		ammo = 10;

		filter = new Filter();
		filter.categoryBits = RodohNarb.CATEGORY_PLAYER;
		filter.maskBits = RodohNarb.MASK_PLAYER;
		baseFixture.setFilterData(filter);
		springFixture.setFilterData(filter);
		wheelFixture.setFilterData(filter);

		PrismaticJointDef springJointDef = new PrismaticJointDef();
		springJointDef.bodyA = springFixture.getBody();
		springJointDef.bodyB = baseFixture.getBody();
		springJointDef.collideConnected = false;
		springJointDef.localAxisA.set(0, 1f);
		springJointDef.localAnchorA.set(0, 1);
		springJointDef.localAnchorB.set(0, .5f);
		springJointDef.enableLimit = true;
		springJointDef.lowerTranslation = 1;
		springJointDef.upperTranslation = 2;
		springJointDef.motorSpeed = -2;
		springJointDef.enableMotor = true;
		springJointDef.maxMotorForce = 5000;

		springJoint = (PrismaticJoint) world.createJoint(springJointDef);

		RevoluteJointDef wheelJointDef = new RevoluteJointDef();
		wheelJointDef.bodyA = springFixture.getBody();
		wheelJointDef.bodyB = wheelFixture.getBody();
		wheelJointDef.collideConnected = false;
		wheelJointDef.enableMotor = true;
		wheelJointDef.localAnchorA.set(0, -.5f);
		wheelJointDef.localAnchorB.set(0, 0);
		wheelJointDef.maxMotorTorque = 500;

		wheelJoint = (RevoluteJoint) world.createJoint(wheelJointDef);
	}

	public void handleInput() {

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {

		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {

		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			wheelJoint.setMotorSpeed(15);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			wheelJoint.setMotorSpeed(-15);
		}
		if (Gdx.input.justTouched()) {
			fire();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			springJoint.setMotorSpeed(10);
			springJoint.enableMotor(true);
			lastJumped = System.currentTimeMillis();
		}

		// cleanup
		if (lastJumped + 1000 < System.currentTimeMillis()) {
			springJoint.setMotorSpeed(-2);
		}
		if (!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
			wheelJoint.setMotorSpeed(0);
		}
	}

	public void fire() {
		int bulletForce = 50;
		long reloadTime = 1000;

		if (ammo > 0 && (lastFired + reloadTime < System.currentTimeMillis())) {
			Fixture bulletFixture = PhysicsWrapper.createFixture(baseFixture.getBody().getWorld(), BodyType.DynamicBody, Shape.Type.Circle,
					baseFixture.getBody().getPosition(), new Vector2[] { new Vector2(.2f, 1f) }, new float[] {});
			bulletFixture.setFilterData(filter);
			float xDiff = Gdx.input.getX() * RodohNarb.WORLD_TO_BOX - bulletFixture.getBody().getPosition().x;
			float yDiff = Gdx.input.getY() * RodohNarb.WORLD_TO_BOX - bulletFixture.getBody().getPosition().y;
			float hypot = (float) Math.hypot(xDiff, yDiff);
			// System.out.println("xMouse:" + Gdx.input.getX() * Platformer.WORLD_TO_BOX + " xPlayer:" +
			// bulletFixture.getBody().getPosition().x);
			// System.out.println("yMouse:" + (100 - Gdx.input.getY() * Platformer.WORLD_TO_BOX) + " yPlayer:" +
			// bulletFixture.getBody().getPosition().y);
			bulletFixture.getBody().applyForceToCenter(xDiff / hypot * bulletForce, yDiff / hypot * bulletForce, true);
			lastFired = System.currentTimeMillis();
			--ammo;
		}
	}

	public Fixture getFixture() {
		return baseFixture;
	}
}