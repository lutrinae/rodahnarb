package com.gamejam.rn.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.camera.SmoothCamWorld;
import com.gamejam.rn.game.Player;

public class GameInputListener extends InputListener {

	Player player;
	SmoothCamWorld smoothCamWorld;
	World world;

	public GameInputListener(Camera camera, Player player, SmoothCamWorld smoothCamWorld) {
		super(camera);
		this.player = player;
		this.smoothCamWorld = smoothCamWorld;
		this.world = player.getWorld();

		createContactListener();
	}

	public boolean fling(float velocityX, float velocityY, int button) {
		if (Math.abs(velocityX) > Math.abs(velocityY)) {
			if (velocityX > 0) { // fling right
				player.moveRight();
			} else if (velocityX < 0) { // fling left
				player.moveLeft();
			}
		} else {
			if (velocityY < 0) { // fling up
				player.jump();
			} else if (velocityY > 0) { // fling down

			}
		}
		return false;
	}

	public boolean tap(float x, float y, int count, int button) {
		player.moveStop();
		return false;
	}

	public boolean zoom(float initialDistance, float distance) {

		return false;
	}

	/*
	public boolean keyTyped(char character) {
		System.out.println(character);
		return false;
	}

	public boolean keyDown(int keycode) {
		System.out.println(keycode); //FIXME this never triggers
		if (keycode == Keys.X) {
			if (smoothCamWorld.fixedX) {
				smoothCamWorld.freeFixedX();
			} else {
				smoothCamWorld.setFixedX(smoothCamWorld.getX());
			}
			return true;
		}
		if (keycode == Keys.Y) {
			if (smoothCamWorld.fixedY) {
				smoothCamWorld.freeFixedY();
			} else {
				smoothCamWorld.setFixedY(smoothCamWorld.getY());
			}
			return true;
		}
		if (keycode == Keys.MINUS) {
			camera.zoom += 0.02;
		}
		if (keycode == Keys.PLUS) {
			camera.zoom -= 0.02;
		}
		if (keycode == Keys.LEFT) {
			if (camera.position.x > 0)
				camera.translate(-3, 0, 0);
		}
		if (keycode == Keys.RIGHT) {
			if (camera.position.x < 1024)
				camera.translate(3, 0, 0);
		}
		if (keycode == Keys.DOWN) {
			if (camera.position.y > 0)
				camera.translate(0, -3, 0);
		}
		if (keycode == Keys.UP) {
			if (camera.position.y < 1024)
				camera.translate(0, 3, 0);
		}
		return false;
	}
	*/

	private void createContactListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				//TODO do something with contacts
//				if (checkPlayerFeetOnGround(fixtureA, fixtureB) || checkPlayerFeetOnGround(fixtureB, fixtureA)) {
//					player.midAir = false;
//				} else {
//					player.midAir = true;
//				}

				// Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				// Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}



		});
	}
}
