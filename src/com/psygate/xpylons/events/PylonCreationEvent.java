package com.psygate.xpylons.events;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PylonCreationEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	private final List<Block> pylonblocks;
	private final Player player;
	private final Block base;

	public PylonCreationEvent(Block base, List<Block> blocks, Player player) {
		this.pylonblocks = blocks;
		this.player = player;
		this.base = base;
	}

	public List<Block> getPylonblocks() {
		return pylonblocks;
	}

	public Player getPlayer() {
		return player;
	}

	public Block getBase() {
		return base;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
