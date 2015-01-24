package com.gamejam.rn.game.simulation;

public enum ParallaxLayer {
	
	DEEP_BACKGROUND(0.3f),
	BACKGROUND(0.6f),
	NEAR_BACKGROUND(0.9f),
	
	FAR_NORMAL(1f),
	NORMAL(1f),
	NEAR_NORMAL(1f),
	
	FOREGROUND(1.25f);
	
	public final float positionScale;
	
	ParallaxLayer(float positionScale) {
		this.positionScale = positionScale;
	}
	
	private static final ParallaxLayer[] cachedVals = values();
	
	public static ParallaxLayer[] cachedValues() {
		return cachedVals;
	}
}
