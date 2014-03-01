package com.psygate.xpylons.listeners;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.psygate.xpylons.XPylons;
import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;

public class DebugListener implements Listener {
	private Set<Chunk> keeploaded = new HashSet<Chunk>();
	private LinkedList<BlockChange> changes = new LinkedList<DebugListener.BlockChange>();
	private LinkedList<Runnable> task = new LinkedList<Runnable>();
	private LinkedList<Block> tickable = new LinkedList<Block>();

	public DebugListener() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(XPylons.getInstance(), new Runnable() {
			private int cpt = 100;
			private int tick = 0;
			Random rand = new Random();

			@Override
			public void run() {
				if (XPylons.getWatchDog().getlastTicks() < 20) {
					cpt = (cpt - 10 < 1) ? 1 : cpt - 10;
				} else {
					cpt += 5;
				}

				int jobs = 0;

				Iterator<Runnable> taskit = task.iterator();
				while (taskit.hasNext()) {
					taskit.next().run();
					taskit.remove();

					jobs++;
				}
				for (int i = 0; i < cpt; i++) {
					BlockChange ch = changes.poll();
					if (ch != null) {
						ch.getB().setType(ch.getMat());
						jobs++;
					} else {
						break;
					}
				}

				if (tickable.size() > 0) {
					long start = System.nanoTime();
					while (System.nanoTime() - start < TimeUnit.MILLISECONDS.toNanos(1000 / 20)) {
						tickable.get(rand.nextInt(tickable.size()));
					}
				}

				tick++;
				if (tick % 20 * 5 == 0 && jobs > 0) {
					System.out.println("CPT: " + cpt);
				}

				if (tick % 20 == 0 && jobs > 0) {
					System.out.println("Jobs per tick: " + jobs);
				}

				for (Chunk c : keeploaded) {
					if (!c.isLoaded()) {
						c.load(true);
					}
				}
			}
		}, 1, 1);
	}

	@EventHandler
	public void generate(final AsyncPlayerChatEvent ev) {
		if (ev.getPlayer().getName().equals("psygate")) {
			if (ev.getMessage().startsWith("generatepylons")) {
				int num = 10;
				if (ev.getMessage().split(" ").length >= 1) {
					num = Integer.parseInt(ev.getMessage().split(" ")[1]);

				}

				final int dec = num;
				ev.getPlayer().sendMessage("Generating " + dec + " pylons.");
				genpylons(ev.getPlayer(), dec);
			} else if (ev.getMessage().equals("unloadchunks")) {
				Iterator<Chunk> chunkit = keeploaded.iterator();
				while (chunkit.hasNext()) {
					chunkit.next().unload();
					chunkit.remove();
				}
			}
		}
	}

	private void genpylons(Player player, int num) {
		final World w = player.getWorld();
		final Random rand = new Random();
		for (int i = 0; i < num; i++) {
			task.add(new Runnable() {

				@Override
				public void run() {
					int x = rand.nextInt(60000);
					int z = rand.nextInt(60000);
					Block base = w.getHighestBlockAt(x, z);
					LinkedList<Block> pb = new LinkedList<Block>();
					for (XPBlock xpb : XPylons.getConfiguration().getTemplate()) {
						pb.add(base.getRelative(xpb.getX(), xpb.getY(), xpb.getZ()));
						changes.add(new BlockChange(base.getRelative(xpb.getX(), xpb.getY(), xpb.getZ()), Material
								.getMaterial(xpb.getMaterial())));
					}

					XPylon pylon = new XPylon(pb, base, XPylons.getConfiguration().getPylonBox());
					XPylons.getDBLayer().save(pylon);
					for (int lx = pylon.getLowX(); lx < pylon.getHighX(); lx++) {
						search: for (int lz = pylon.getLowZ(); lz < pylon.getHighZ(); lz++) {
							Block ch = w.getBlockAt(lx, pylon.getCy(), lz);
							for (XPBlock block : pylon.getBlocks()) {
								if (block.getX() == ch.getX()
										&& (block.getY() == ch.getY() || block.getY() == ch.getY() + 1)
										&& block.getZ() == ch.getZ()) {
									continue search;
								}
							}
							if (lx % 5 == 0 && lz % 5 == 0) {
								changes.add(new BlockChange(ch, Material.STATIONARY_WATER));
							} else {
								changes.add(new BlockChange(ch, Material.SOIL));
								changes.add(new BlockChange(ch.getRelative(BlockFace.UP), Material.CROPS));
								tickable.add(ch.getRelative(BlockFace.UP));
							}
							keeploaded.add(ch.getChunk());
						}
					}

				}
			});
		}
	}

	@EventHandler
	public void unloadChunk(ChunkUnloadEvent ev) {
		if (keeploaded.contains(ev.getChunk())) {
			ev.setCancelled(true);
		}
	}

	private class BlockChange {
		private Block b;
		private Material mat;

		public BlockChange(Block b, Material mat) {
			super();
			this.b = b;
			this.mat = mat;
		}

		public Block getB() {
			return b;
		}

		public Material getMat() {
			return mat;
		}

	}
}
