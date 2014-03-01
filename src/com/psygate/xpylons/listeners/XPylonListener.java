package com.psygate.xpylons.listeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.psygate.xpylons.XPylons;
import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;
import com.psygate.xpylons.events.PylonCreationEvent;
import com.psygate.xpylons.events.PylonDesctructionEvent;
import com.psygate.xpylons.events.PylonGrowthInhibitionEvent;
import com.psygate.xpylons.geometry.AABox;

public class XPylonListener implements Listener {
	private Random rand = new Random();

	@EventHandler
	public void checkPylonCreation(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack stack = ev.getPlayer().getItemInHand();
			if (stack != null && stack.getType() == XPylons.getConfiguration().getActivationMaterial()) {
				if (XPylons.getConfiguration().isPylon(ev.getClickedBlock())) {
					Block base = ev.getClickedBlock();

					Player player = ev.getPlayer();
					PylonCreationEvent pce = new PylonCreationEvent(base, getBlocks(base), player);
					Bukkit.getPluginManager().callEvent(pce);
					if (!pce.isCancelled()) {
						int cx = base.getX();
						int cy = base.getY();
						int cz = base.getZ();
						String worldUID = base.getWorld().getUID().toString();
						AABox box = XPylons.getConfiguration().getPylonBox();

						XPylon pylon = new XPylon(pce.getPylonblocks(), cx, cy, cz, worldUID, box);
						if (!XPylons.getDBLayer().distanceCheck(pylon)) {
							ev.getPlayer().sendMessage(ChatColor.RED + "Pylon cannot be created here.");
						} else {
							XPylons.getDBLayer().save(pylon);
							ev.getPlayer().sendMessage(ChatColor.GREEN + "Pylon created.");
						}
					}
				}
			}
		}
	}

	private List<Block> getBlocks(Block base) {
		LinkedList<Block> out = new LinkedList<Block>();
		List<XPBlock> templ = XPylons.getConfiguration().getPylonBlockTemplate();
		for (XPBlock b : templ) {
			out.add(base.getRelative(b.getX(), b.getY(), b.getZ()));
		}

		return out;
	}

	@EventHandler
	public void checkPylonDestruction(BlockBreakEvent ev) {
		XPBlock b = XPylons.getDBLayer().getBlock(ev.getBlock());
		if (b != null) {
			ev.getPlayer().sendMessage(ChatColor.RED + "Pylon broken.");
			Bukkit.getPluginManager().callEvent(new PylonDesctructionEvent(b.getPylon(), ev.getBlock()));
			XPylons.getDBLayer().delete(b.getPylon());
		}
	}

	@EventHandler
	public void xpleech(BlockGrowEvent ev) {
		if (rand.nextFloat() < XPylons.getConfiguration().getGrowthStopChance()) {
			XPylon pylon = XPylons.getDBLayer().getPylonFor(ev.getBlock());
			if (pylon != null) {
				double xp = XPylons.getConfiguration().getXpPerGrowth();
				PylonGrowthInhibitionEvent pgie = new PylonGrowthInhibitionEvent(ev.getBlock(), pylon, xp);
				Bukkit.getPluginManager().callEvent(pgie);
				if (!pgie.isCancelled()) {
					pylon.setXp(pylon.getXp() + pgie.getLeechedXP());
					XPylons.getDBLayer().update(pylon);
				}
			}
		}
		
	}
}
