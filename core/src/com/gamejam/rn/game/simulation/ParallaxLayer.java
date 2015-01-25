package com.gamejam.rn.game.simulation;

public enum ParallaxLayer {
	
	STATIC_BACKGROUND(0f),
	DEEP_BACKGROUND(0.1f, 0f),
	FAR_BACKGROUND(0.2f, 0.1f),
	BACKGROUND(0.4f, 0.3f),
	NEAR_BACKGROUND(0.7f, 0.5f),
	
	FAR_NORMAL(1f),
	NORMAL(1f),
	NEAR_NORMAL(1f),
	
	FOREGROUND(1.25f);
	
	public final float xPositionScale;
	public final float yPositionScale;
	
	ParallaxLayer(float positionScale) {
		this.xPositionScale = positionScale;
		this.yPositionScale = positionScale;
	}
	
	ParallaxLayer(float xPositionScale, float yPositionScale) {
		this.xPositionScale = xPositionScale;
		this.yPositionScale = yPositionScale;
	}
	
	private static final ParallaxLayer[] cachedVals = values();
	
	public static ParallaxLayer[] cachedValues() {
		return cachedVals;
	}
}
