package com.gamejam.rodohnarb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rodohnarb.camera.SmoothCamDebugRenderer;
import com.gamejam.rodohnarb.camera.SmoothCamWorld;
import com.gamejam.rodohnarb.game.Player;
import com.gamejam.rodohnarb.game.Robot;
import com.gamejam.rodohnarb.game.SpineBoy;
import com.gamejam.rodohnarb.input.GameInputListener;

public class RodohNarb extends ApplicationAdapter {

	
	private World world;
	private SmoothCamWorld smoothCamWorld;
	float time;
	
	Box2DDebugRenderer debugRenderer;
	private SmoothCamDebugRenderer scDebug;
	
	OrthographicCamera camera;
	static final float BOX_STEP=1/60f;
	static final int BOX_VELOCITY_ITERATIONS=8; //6
	static final int BOX_POSITION_ITERATIONS=3; //2
	public static final float WORLD_TO_BOX=0.1f;
	static final float BOX_WORLD_TO=10f;

	public static final int HEIGHT = 320;
	public static final int WIDTH  = 480;
	
	public static final short CATEGORY_PLAYER = 0x0001;
	public static final short CATEGORY_MONSTER = 0x0002;
	public static final short CATEGORY_SCENERY = 0x0004;
	
	public static final short MASK_PLAYER = CATEGORY_MONSTER | CATEGORY_SCENERY; // or ~CATEGORY_PLAYER
	public static final short MASK_MONSTER = CATEGORY_PLAYER | CATEGORY_SCENERY; // or ~CATEGORY_MONSTER
	public static final short MASK_SCENERY = -1; //aka 0xFFFE
	
	Player player;
	Robot robot;
	GameWorld room;
	
	@Override
	public void create () {
		Box2D.init();
		
		camera = new OrthographicCamera(WIDTH * WORLD_TO_BOX,HEIGHT * WORLD_TO_BOX);
		camera.zoom = 0.4f; //1.0 normal zoom, 2.0 zoomed out
		camera.update(); 
		
		debugRenderer = new Box2DDebugRenderer();
		scDebug = new SmoothCamDebugRenderer();

		world = new GameWorld(camera).getWorld();
		player = new SpineBoy(world, camera);
//		robot = new Robot(world, camera);
		smoothCamWorld = new SmoothCamWorld(player.playerCam);
		smoothCamWorld.setBoundingBox(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.8f);
		
		//input
		Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(camera, player, smoothCamWorld))); // Listen for touch events in our InputListener class
	}

	@Override
	public void render () {
		
		//time
		float delta = Gdx.graphics.getDeltaTime();
		float remaining = delta;
		while (remaining > 0) {
			float d = Math.min(0.016f, remaining);
			world.step(d, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
			time += d;
			remaining -= d;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//render objects
		player.updateCamera(smoothCamWorld);
		player.animate(time, delta);

		if (robot != null) {
			robot.handleInput();
//			camera.position.set(robot.getFixture().getBody().getPosition().x,robot.getFixture().getBody().getPosition().y,0f);
		}
		
//		debugRenderer.render(world, camera.combined);
//		scDebug.render(smoothCamWorld, camera.combined);
		
	}
	
	@Override
	public void resize(int width, int height) {
		player.resize(camera.projection);
	}
	
	@Override
	public void dispose() {
	}
}
