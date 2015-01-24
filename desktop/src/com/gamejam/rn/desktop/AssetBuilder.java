package com.gamejam.rn.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetBuilder {

	public static void main(String[] args) {
		TexturePacker.process("../rawart/environment", "../core/assets", "environment");
	}

}
