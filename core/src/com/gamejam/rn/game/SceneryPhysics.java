package com.gamejam.rn.game;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.gamejam.rn.RNGame;

public class SceneryPhysics {

	Filter sceneryFilter;
	private Body body;
//	private Fixture fixture;
//	private float width;
//	private float height;
	private List<Sprite> sprites;
	
	/**
	 * 
	 * @param world Box2D world
	 * @param position x,y
	 * @param hwvertices vertices with height and width of the polygon or radius of the circle
	 */
	public SceneryPhysics(World world, Vector2 position, Vector2[] hwvertices) {
		
		sceneryFilter = new Filter();
		sceneryFilter.categoryBits = RNGame.CATEGORY_SCENERY;
		sceneryFilter.maskBits = RNGame.MASK_SCENERY;

		Fixture fixture = PhysicsWrapper.createFixture(world, BodyType.StaticBody, Shape.Type.Polygon, position, hwvertices, new float[]{});
		fixture.getBody().setUserData(this);
		fixture.setUserData(this);
		fixture.setFilterData(sceneryFilter);
		
	}
	
	public void addSprite(Sprite sprite) {
		addSprite(sprite, 0f, 0f);
	}
	
	public void addSprite(Sprite sprite, float x, float y) {
		sprites.add(sprite);
		sprite.setOriginCenter();
		sprite.setCenter(body.getWorldCenter().x + x, body.getWorldCenter().y + y);
	}
	
	public void addSprite(Sprite sprite, float x, float y, float width, float height) {
		sprites.add(sprite);
		sprite.setOriginCenter();
		sprite.setScale(width / sprite.getWidth(), height / sprite.getHeight());
		sprite.setCenter(body.getWorldCenter().x + x, body.getWorldCenter().y + y);
	}
	
	public void render(Batch batch) {
		for (Sprite s : sprites) {
			s.draw(batch);
		}
	}

}