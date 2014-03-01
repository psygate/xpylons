package com.psygate.xpylons;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.world.WorldUnloadEvent;

import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;

public class PassThroughLayer implements DBLayer {

	@Override
	public void save(XPylon pylon) {
		XPylons.getDB().save(pylon);
	}

	@Override
	public void delete(XPylon pylon) {
		XPylons.getDB().delete(pylon);
	}

	@Override
	public void delete(XPBlock block) {
		XPylons.getDB().delete(block);
	}

	@Override
	public boolean intersects(XPylon pylon) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean distanceCheck(XPylon pylon) {
		int rows = XPylons.getDB().find(XPylon.class).where().eq("worlduid", pylon.getWorldUID())
				.eq("centerx", pylon.getCx()).eq("centery", pylon.getCy()).eq("centerz", pylon.getCz()).findRowCount();
		if (rows > 0) {
			return false;
		}

		rows = XPylons.getDB().find(XPylon.class).where().eq("worlduid", pylon.getWorldUID())
				.ge("highx", pylon.getLowX()).le("lowx", pylon.getHighX()).ge("highy", pylon.getLowY())
				.le("lowy", pylon.getHighY()).ge("highz", pylon.getLowZ()).le("lowz", pylon.getHighZ()).findRowCount();

		if (rows > 0) {
			return false;
		}

		int lowx = pylon.getCx() - XPylons.getConfiguration().getMinPylonDistance();
		int lowy = pylon.getCy() - XPylons.getConfiguration().getMinPylonDistance();
		int lowz = pylon.getCz() - XPylons.getConfiguration().getMinPylonDistance();

		int highx = pylon.getCx() + XPylons.getConfiguration().getMinPylonDistance();
		int highy = pylon.getCy() + XPylons.getConfiguration().getMinPylonDistance();
		int highz = pylon.getCz() + XPylons.getConfiguration().getMinPylonDistance();

		List<XPylon> check = XPylons.getDB().find(XPylon.class).where().eq("worlduid", pylon.getWorldUID())
				.ge("highx", lowx).le("lowx", highx).ge("highy", lowy).le("lowy", highy).ge("highz", lowz)
				.le("lowz", highz).findList();

		int mindist = XPylons.getConfiguration().getMinPylonDistance();
		mindist *= mindist;
		for (XPylon p : check) {
			if (distance(p, pylon) < mindist) {
				return false;
			}
		}

		return true;
	}

	private int distance(XPylon p, XPylon pylon) {
		int x = p.getCx() - pylon.getCx();
		int y = p.getCy() - pylon.getCy();
		int z = p.getCz() - pylon.getCz();

		return x * x + y * y + z * z;
	}

	@Override
	public XPBlock getBlock(Block block) {
		XPBlock blocks = XPylons.getDB().find(XPBlock.class).where()
				.eq("worlduid", block.getWorld().getUID().toString()).eq("x", block.getX()).eq("y", block.getY())
				.eq("z", block.getZ()).findUnique();

		return blocks;
	}

	@Override
	public XPylon getPylonFor(Block block) {
		return XPylons.getDB().find(XPylon.class).where().eq("worlduid", block.getWorld().getUID().toString())
				.ge("highx", block.getX()).le("lowx", block.getX()).ge("highy", block.getY()).le("lowy", block.getY())
				.ge("highz", block.getZ()).le("lowz", block.getZ()).findUnique();
	}

	@Override
	public List<XPylon> getPylons() {
		return XPylons.getDB().find(XPylon.class).findList();
	}

	@Override
	public void purgeAll() {
		List<XPylon> ps = XPylons.getDB().find(XPylon.class).findList();
		XPylons.getDB().delete(ps);
	}

	@Override
	public void update(XPylon pylon) {
		XPylons.getDB().save(pylon);

	}

	@Override
	public XPylon getPylonFor(int x, int y, int z, String world) {
		return XPylons.getDB().find(XPylon.class).where().eq("worlduid", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
	}
}
