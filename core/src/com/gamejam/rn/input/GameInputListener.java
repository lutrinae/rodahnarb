package com.gamejam.rn.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.camera.SmoothCamWorld;
import com.gamejam.rn.game.Hero;
import com.gamejam.rn.game.Player;

public class GameInputListener implements GestureListener, InputProcessor {

	Player player;
	SmoothCamWorld smoothCamWorld;
	World world;

	public GameInputListener(Camera camera, Player player, SmoothCamWorld smoothCamWorld) {
		this.player = player;
		this.smoothCamWorld = smoothCamWorld;
		this.world = player.getWorld();

		createContactListener();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		System.out.println(keycode); //FIXME this never triggers
		
		switch (keycode) {
		case Keys.SHIFT_LEFT: 
			player.sprint(true);
			return true;
		case Keys.A:
			player.moveLeft(true);
			return true;
		case Keys.D:
			player.moveRight(true);
			return true;
		case Keys.SPACE:
			player.jump(true);
			return true;
		case Keys.CONTROL_LEFT:
			player.crouch(true);
			return true;
		}
		return false;
	}

	
	@Override
	public boolean keyUp(int keycode) {
//		switch (keycode) {
//		case Keys.SHIFT_LEFT: 
//			player.sprint(false);
//			return true;
//		case Keys.A:
//			player.moveLeft(false);
//			return true;
//		case Keys.D:
//			player.moveRight(false);
//			return true;
//		case Keys.SPACE:
//			player.jump(false);
//			return true;
//		case Keys.CONTROL_LEFT:
//			player.crouch(false);
//			return true;
//		}
		return false;
	}

	public boolean fling(float velocityX, float velocityY, int button) {
		if (Math.abs(velocityX) > Math.abs(velocityY)) {
			if (velocityX > 0) { // fling right
				player.moveRight(true);
			} else if (velocityX < 0) { // fling left
				player.moveLeft(true);
			}
		} else {
			if (velocityY < 0) { // fling up
				player.jump(true);
			} else if (velocityY > 0) { // fling down
				player.crouch(true);
			}
		}
		return false;
	}

	public boolean tap(float x, float y, int count, int button) {
		player.stop();
		return false;
	}

	public boolean zoom(float initialDistance, float distance) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		
//		if (character == 'a') {
//			player.moveLeft();
//		}
//		if (character == 'd') {
//			player.moveRight();
//		}
//		if (character == 'w') {
//			player.jump();
//		}
//		if (character == 's') {
//			player.crouch();
//		}
		return false;
	}
	
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
