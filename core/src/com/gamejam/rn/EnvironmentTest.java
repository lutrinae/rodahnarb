package com.gamejam.rn;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.gamejam.rn.game.simulation.EnvironmentEntity;
import com.gamejam.rn.game.simulation.ParallaxLayer;
import com.gamejam.rn.game.simulation.RNWorld;

public class EnvironmentTest extends ApplicationAdapter {

	private RNWorld world;
	
	@Override
	public void create () {
		Box2D.init();
		world = new RNWorld();
		
		TextureAtlas environmentAtlas = new TextureAtlas(Gdx.files.internal("environment.atlas"));
		
		EnvironmentEntity e1 = new EnvironmentEntity(environmentAtlas, "test1");
		e1.setSize(100, 100);
		e1.setPosition(0, 0);
		
		EnvironmentEntity e2 = new EnvironmentEntity(environmentAtlas, "test2");
		e2.setLayer(ParallaxLayer.BACKGROUND);
		e2.setSize(100, 100);
		e2.setPosition(10, 10);
		
		world.addEntity(e1);
		world.addEntity(e2);
		
		world.getCameraSubject().setVelocity(10f,  0f);
		
		//input
		//Gdx.input.setInputProcessor(new GestureDetector(new GameInputListener(camera, player, smoothCamWorld))); // Listen for touch events in our InputListener class
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void render () {
		
		float dt = Gdx.graphics.getDeltaTime();
		world.update(dt);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		world.render();		
	}
	
	@Override
	public void resize(int width, int height) {
		world.resize(width, height);
	}
	
	@Override
	public void dispose() {
		world.dispose();
	}

}
