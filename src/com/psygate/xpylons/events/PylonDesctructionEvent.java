package com.psygate.xpylons.events;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.psygate.xpylons.entities.XPylon;

public class PylonDesctructionEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final XPylon brokenPylon;
	private final Block brokenBlock;

	public PylonDesctructionEvent(XPylon xPylon, Block brokenBlock) {
		super();
		this.brokenPylon = xPylon;
		this.brokenBlock = brokenBlock;
	}

	public XPylon getBrokenPylon() {
		return brokenPylon;
	}

	public Block getBrokenBlock() {
		return brokenBlock;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
