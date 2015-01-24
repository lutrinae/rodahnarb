package com.gamejam.rodohnarb.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.gamejam.rodohnarb.RodohNarb;

public class PhysicsWrapper {

	Fixture baseFixture;

	SpriteBatch batch;
	ShapeRenderer renderer;
	SkeletonRenderer skeletonRenderer = new SkeletonRenderer();

	TextureAtlas atlas;
	Skeleton skeleton;
	Animation animation;

	public PhysicsWrapper(World world, BodyType bodyType, Type shapeType, Vector2 position, Vector2[] vertices, float[] massCoefficients) {

		baseFixture = createFixture(world, bodyType, shapeType, position, vertices, massCoefficients);
	}

	public static Fixture createFixture(World world, BodyType bodytype, Type shapeType, Vector2 position, Vector2[] vertices, float[] massCoefficients) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodytype;
		bodyDef.position.set(position);
		Body body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circleShape = new CircleShape();
		PolygonShape polyShape = new PolygonShape();

		switch (shapeType) {
		case Chain:
			break;
		case Circle:
			circleShape.setRadius(vertices[0].x);
			fixtureDef.shape = circleShape;
			break;
		case Edge:
			break;
		case Polygon:
			if (vertices.length == 1) {
				polyShape.setAsBox(vertices[0].x, vertices[0].y); // if one vert, set as box
			} else {
				polyShape.set(vertices);
			}
			fixtureDef.shape = polyShape;
			break;
		default:
			break;
		}

		Filter filter = new Filter();
		filter.categoryBits = RodohNarb.CATEGORY_PLAYER;
		filter.maskBits = RodohNarb.MASK_PLAYER;

		if (massCoefficients != null && massCoefficients.length == 3) {
			fixtureDef.density = massCoefficients[0];
			fixtureDef.friction = massCoefficients[1];
			fixtureDef.restitution = massCoefficients[2];
		} else {
			fixtureDef.density = 1.0f;
			fixtureDef.friction = 0.5f;
			fixtureDef.restitution = 0.1f;
		}

		Fixture tempFixture = body.createFixture(fixtureDef);
		circleShape.dispose();
		polyShape.dispose();
		return tempFixture;
	}

}
