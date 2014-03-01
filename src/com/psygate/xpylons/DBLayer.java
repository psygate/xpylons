package com.psygate.xpylons;

import java.util.List;

import org.bukkit.block.Block;

import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;

public interface DBLayer {
	public void save(XPylon pylon);
	public void delete(XPylon pylon);
	public void delete(XPBlock block);
	public boolean intersects(XPylon pylon);
	public boolean distanceCheck(XPylon pylon);
	public XPBlock getBlock(Block block);
	public XPylon getPylonFor(Block block);
	public List<XPylon> getPylons();
	public void purgeAll();
	public void update(XPylon pylon);
	public XPylon getPylonFor(int x, int y, int z, String world);
}
