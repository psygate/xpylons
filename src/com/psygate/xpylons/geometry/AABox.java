package com.psygate.xpylons.geometry;

public class AABox {
	private final int width, height, depth;

	public AABox(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}
}
