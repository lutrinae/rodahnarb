package com.gamejam.rn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.game.PhysicsWrapper;
import com.gamejam.rn.game.SceneryPhysics;

public class GameWorld {
	
	Filter sceneryFilter;
	World world;
	
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
    private PolygonSpriteBatch spriteBatch;
	
	public GameWorld(Camera camera) {
		
		this.world = new World(new Vector2(0, -10), true);
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = RNGame.CATEGORY_SCENERY;
		sceneryFilter.maskBits = RNGame.MASK_SCENERY;
		
		float bw = 1.0f; //border width
		
		spriteBatch = new PolygonSpriteBatch();
		backgroundTexture = new Texture("C:/Users/Brad/workspace/GameJams/rodahnarb/core/assets/forest.png");
        backgroundSprite =new Sprite(backgroundTexture);
		
//		SceneryPhysics ground = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)});
		SceneryPhysics ground = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,bw), new Vector2[]{new Vector2(camera.viewportWidth*8, bw)});
//		ground.addSprite(sprite);
//		SceneryPhysics ceiling = new SceneryPhysics(world, new Vector2(bw + camera.viewportWidth/2,camera.viewportHeight-bw), new Vector2[]{new Vector2(camera.viewportWidth/2, bw)});
//		SceneryPhysics wallLeft = new SceneryPhysics(world, new Vector2(bw,camera.viewportHeight/2-bw), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
//		SceneryPhysics wallRight = new SceneryPhysics(world, new Vector2(camera.viewportWidth-bw,camera.viewportHeight/2), new Vector2[]{new Vector2(bw, camera.viewportHeight/2)});
		
		//ball obstacles
		for (int i = 0; i < 200; i++) {
			PhysicsWrapper.createFixture(world, BodyType.DynamicBody, Shape.Type.Circle, new Vector2(camera.viewportWidth/4 + (float)(Math.random() * camera.viewportWidth*8), camera.viewportHeight/4 + (float)(Math.random() * camera.viewportHeight/2)), new Vector2[]{new Vector2(.3f,.3f)},new float[]{}).setFilterData(sceneryFilter);
		}

		//box obstacles
		for (int i = 0; i < 20; i++) {
			PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, new Vector2(camera.viewportWidth/4 + (float)(Math.random() * camera.viewportWidth*8), 3), new Vector2[]{new Vector2(.5f,1f)},new float[]{}).setFilterData(sceneryFilter);
		}
		
	}

	public World getWorld() {
		return world;
	}
	
	public void render() {
		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		backgroundSprite.draw(spriteBatch);
		spriteBatch.end();
	}
}