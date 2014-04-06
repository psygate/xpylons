package com.psygate.xpylons.geometry;

/**
 * General simple interface for a basic Axis-Align Bounding Box.
 * 
 * @author psygate
 * 
 */
public interface SimpleAABB {
	/**
	 * Getter for the lower-x coordinate.
	 * 
	 * @return Lower X coordiante.
	 */
	public int getX();

	/**
	 * Getter for the lower-y coordinate.
	 * 
	 * @return Lower y coordiante.
	 */
	public int getY();

	/**
	 * Getter for the lower-z coordinate.
	 * 
	 * @return Lower Z coordiante.
	 */
	public int getZ();

	/**
	 * Getter for the width of the aabb.
	 * 
	 * @return Width of the aabb.
	 */
	public int getWidth();

	/**
	 * Getter for the height of the aabb.
	 * 
	 * @return Height of the aabb.
	 */
	public int getHeight();

	/**
	 * Getter for the depth of the aabb.
	 * 
	 * @return Depth of the aabb.
	 */
	public int getDepth();

	/**
	 * Returns the maximum x coordinate of the aabb.
	 * 
	 * @return Maximum X coordinate of the aabb.
	 */
	public int getMaxX();

	/**
	 * Returns the maximum y coordinate of the aabb.
	 * 
	 * @return Maximum Y coordinate of the aabb.
	 */
	public int getMaxY();

	/**
	 * Returns the maximum z coordinate of the aabb.
	 * 
	 * @return Maximum Z coordinate of the aabb.
	 */
	public int getMaxZ();

	/**
	 * Check if this aabb intersects the other aabb.
	 * 
	 * @param aabb
	 *            The aabb to check intersection for.
	 * @return True if this aabb intersects the other aabb.
	 */
	public boolean intersects(SimpleAABB aabb);

	/**
	 * Check if the point is in the aabb.
	 * 
	 * @param x
	 *            X coordinate of the point.
	 * @param y
	 *            Y coordinate of the point.
	 * @param z
	 *            Z coordinate of the point.
	 * @return True, if the point lies within aabb.
	 */
	public boolean contains(int x, int y, int z);
}
