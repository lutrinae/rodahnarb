package com.gamejam.rn.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public abstract class AnimatedPhysics {

//	SpriteBatch batch;
	PolygonSpriteBatch batch;
	//ShapeRenderer renderer;
	SkeletonRenderer skeletonRenderer = new SkeletonRenderer();

	TextureAtlas atlas;
	Skeleton skeleton;
	SkeletonData skeletonData;
	SkeletonBounds bounds;

	AnimationState animationState; // Must create in child class, Holds the animation state for a skeleton (current animation, time, etc).
	Array<Animation> animations;

	Camera camera;
	World world;

	Array<Body> bodies = new Array<Body>();
	Array<Fixture> fixtures = new Array<Fixture>();
	Filter filter;

	float lastTime = -1;
	Array<Event> events = new Array<Event>();

	/**
	 * Constructs an AnimatedPhysics object that combines Box2D and Spine skeletal animation. For best results, extend
	 * into your own classes to keep track of states, etc to switch between animations.
	 * 
	 * @param world
	 *            Box2d world for physics object creation
	 * @param camera
	 *            for animation
	 * @param atlasName
	 *            String name of file of atlas (not including the .atlas as will also be used for the .json)
	 * @param position
	 *            Vector2 x y coordinates
	 * @param skeletonSlotsNotBodies
	 *            String list of skeleton parts to exclude when making Box2D bodies
	 * @param categoryBits
	 *            category for Box2D filter
	 * @param maskBits
	 *            mask for Box2D filter
	 */
	public AnimatedPhysics(World world, Camera camera, String atlasName, Vector2 position, Array<String> skeletonSlotsExcludeFromBodies,
			short categoryBits, short maskBits) {

		this.camera = camera;
		this.world = world;
		filter = new Filter();
		filter.categoryBits = categoryBits;
		filter.maskBits = maskBits;
		createSkeletalAnimations(atlasName, position, skeletonSlotsExcludeFromBodies, 0.006f);

	}
	
	public AnimatedPhysics(World world, Camera camera, String atlasName, Vector2 position, Array<String> skeletonSlotsBodiesSubset,
			short categoryBits, short maskBits, float scale) {

		this.camera = camera;
		this.world = world;
		filter = new Filter();
		filter.categoryBits = categoryBits;
		filter.maskBits = maskBits;
		createSkeletalAnimations(atlasName, position, skeletonSlotsBodiesSubset, scale);

	}

	private void createSkeletalAnimations(String spineFileName, Vector2 position, Array<String> skeletonSlotsBodiesSubset, float scale) {

//		batch = new SpriteBatch();
		batch = new PolygonSpriteBatch();
		//renderer = new ShapeRenderer();
		
		atlas = new TextureAtlas(Gdx.files.internal(spineFileName + ".atlas"));

		// create attachments to be initalized as bodies below
		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas) {
			public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
				Box2dRegionAttachment regionAttachment = new Box2dRegionAttachment(name);
				AtlasRegion region = atlas.findRegion(regionAttachment.getName());
				if (region == null)
					throw new RuntimeException("Region not found in atlas: " + regionAttachment + " (region attachment: " + name + ")");
				regionAttachment.setRegion(region);
				return regionAttachment;
			}
			public MeshAttachment newMeshAttachment (Skin skin, String name, String path) {
				Box2dMeshAttachment meshAttachment = new Box2dMeshAttachment(name);
				AtlasRegion region = atlas.findRegion(meshAttachment.getName());
				if (region == null)
					throw new RuntimeException("Region not found in atlas: " + meshAttachment + " (region attachment: " + name + ")");
				meshAttachment.setRegion(region);
//				Gdx.app.log("Attachment name:", meshAttachment.getName());
				return meshAttachment;
			}
		};
//		Gdx.app.log("Created:", this.toString());

		SkeletonJson json = new SkeletonJson(atlasLoader);
		json.setScale(scale);
		skeletonData = json.readSkeletonData(Gdx.files.internal(spineFileName + ".json"));
		animations = skeletonData.getAnimations();
		bounds = new SkeletonBounds(); // Convenience class to do hit detection with bounding boxes, might just use Box2d bodies
		Gdx.app.log("Animations", animations.toString());

		skeleton = new Skeleton(skeletonData);
		skeleton.setX(position.x);
		skeleton.setY(position.y);
		skeleton.updateWorldTransform();

		Vector2 vector = new Vector2();

		Gdx.app.log("Skeleton slots", skeleton.getSlots().toString());

		// Create a box body for each attachment.
		for (Slot slot : skeleton.getSlots()) {
			if (slot.getAttachment() instanceof Box2dRegionAttachment) {
				
				if (skeletonSlotsBodiesSubset != null) {
					if(skeletonSlotsBodiesSubset.contains("INCLUDE", false)) {
						if (!skeletonSlotsBodiesSubset.contains(slot.getAttachment().getName(), false))
							continue;
					} else {
						if(skeletonSlotsBodiesSubset.contains(slot.getAttachment().getName(), false)) 
							continue;
					}
				}
//				if (skeletonSlotsExcludeFromBodies != null && skeletonSlotsExcludeFromBodies.contains(slot.getAttachment().getName(), false))
//					continue;
				Box2dRegionAttachment attachment = (Box2dRegionAttachment) slot.getAttachment();

				PolygonShape boxPoly = new PolygonShape();
				int fudgeScale = 8;
				boxPoly.setAsBox(attachment.getWidth() / fudgeScale * attachment.getScaleX(), attachment.getHeight() / fudgeScale * attachment.getScaleY(),
						vector.set(attachment.getX(), attachment.getY()), attachment.getRotation() * MathUtils.degRad);

				BodyDef boxBodyDef = new BodyDef();
				boxBodyDef.type = BodyType.DynamicBody;
				boxBodyDef.position.set(new Vector2(position.x,position.y));
				attachment.body = world.createBody(boxBodyDef);
				attachment.body.setGravityScale(0);
				attachment.body.setUserData(this);
				bodies.add(attachment.body);

				Fixture newFixture = attachment.body.createFixture(boxPoly, 1);
				newFixture.setRestitution(.5f);
				newFixture.setUserData(attachment);
				fixtures.add(newFixture);

				boxPoly.dispose();
			} else if (slot.getAttachment() instanceof Box2dMeshAttachment) {
				
				//TODO box2d bodies on mesh skeletons
				/*
				if (skeletonSlotsExcludeFromBodies != null && skeletonSlotsExcludeFromBodies.contains(slot.getAttachment().getName(), false))
					continue;
				Box2dMeshAttachment attachment = (Box2dMeshAttachment) slot.getAttachment();
				Gdx.app.log("Attachment vertices", attachment.getVertices().toString());

				PolygonShape boxPoly = new PolygonShape();
//				boxPoly.setAsBox(attachment.getWidth() / 2, attachment.getHeight() / 2,
//						vector.set(attachment.getX(), attachment.getY()), attachment.getRotation() * MathUtils.degRad);

				BodyDef boxBodyDef = new BodyDef();
				boxBodyDef.type = BodyType.DynamicBody;
				boxBodyDef.position.set(new Vector2(position.x,position.y));
				attachment.body = world.createBody(boxBodyDef);
				attachment.body.setGravityScale(0);
				attachment.body.setUserData(this);
				bodies.add(attachment.body);

				Fixture newFixture = attachment.body.createFixture(boxPoly, 1);
				newFixture.setRestitution(.1f);
				newFixture.setUserData(attachment);
				fixtures.add(newFixture);

				boxPoly.dispose();
				*/
			}
		}

		// set Filter for Box2D
		for (Fixture fixture : fixtures) {
			fixture.setFilterData(filter);
		}
	}
	
	public void createAnimationState(AnimationStateData stateData) {
		animationState = new AnimationState(stateData);
	}

	public Animation findAnimation(String animationName) {
		for (Animation animation : animations) {
			if (animation.getName().equals(animationName)) {
				return animation;
			}
		}
		return null;
	}

	public void animate(float time, float delta) {

		startAnimate(time);

		// make your own methods to call start and finish Animate with stuff like this in between
		findAnimation("walk").apply(skeleton, lastTime, time, true, events);
		skeleton.setX(skeleton.getX() + 8 * delta); // move forward delta times

		if (animationState == null) {
			finishAnimate(time);
		} else {
			finishAnimate(time, delta);
		}
	}

	public void startAnimate(float time) {

		if (lastTime == -1) {
			lastTime = time;
		}

		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();
	}

	public void updateAnimate(float delta) {
	    animationState.update(delta);
	    animationState.apply(skeleton);
	    
	}

	public void finishAnimate(float time, float delta) {
	    animationState.update(delta);
	    animationState.apply(skeleton);
	    
	    finishAnimate(time);
	}
	public void finishAnimate(float time) {

		lastTime = time;
		
		skeleton.updateWorldTransform();
		skeletonRenderer.draw(batch, skeleton);

		batch.end();

		// Position each attachment body.
		for (Slot slot : skeleton.getSlots()) {
			if (slot.getAttachment() instanceof Box2dRegionAttachment) {

				Box2dRegionAttachment attachment = (Box2dRegionAttachment) slot.getAttachment();
				if (attachment.body == null)
					continue;
				float x = skeleton.getX() + slot.getBone().getWorldX();
				float y = skeleton.getY() + slot.getBone().getWorldY();
				float rotation = slot.getBone().getWorldRotation();
				attachment.body.setTransform(x, y, rotation * MathUtils.degRad);
				attachment.body.setLinearVelocity(0f, 0f); // for smoothCam

			} else if(slot.getAttachment() instanceof Box2dMeshAttachment) {

				Box2dMeshAttachment attachment = (Box2dMeshAttachment) slot.getAttachment();
				if (attachment.body == null)
					continue;
//				Gdx.app.log("Test", "");
				//FIXME what bone?
				float x = skeleton.getX() + slot.getBone().getWorldX();
				float y = skeleton.getY() + slot.getBone().getWorldY();
				float rotation = slot.getBone().getWorldRotation();
				attachment.body.setTransform(x, y, rotation * MathUtils.degRad);
				attachment.body.setLinearVelocity(0f, 0f); // for smoothCam
			}
		}
	}

	public static class Box2dRegionAttachment extends RegionAttachment {
		Body body;

		public Box2dRegionAttachment(String name) {
			super(name);
		}
		
		public Body getBody() {
			return body;
		}
	}
	public static class Box2dMeshAttachment extends MeshAttachment {
		Body body;

		public Box2dMeshAttachment(String name) {
			super(name);
		}
		
		public Body getBody() {
			return body;
		}
	}
	
	public World getWorld() {
		return world;
	}

	public void resize(Matrix4 projection) {
		batch.setProjectionMatrix(projection);
		//renderer.setProjectionMatrix(projection);
	}
}
