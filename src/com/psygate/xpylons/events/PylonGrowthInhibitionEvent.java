package com.psygate.xpylons.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.psygate.xpylons.entities.XPylon;

public class PylonGrowthInhibitionEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private final Block growingBlock;
	private final XPylon inhibitingPylon;
	private double leechedXP;

	public PylonGrowthInhibitionEvent(Block growingBlock, XPylon inhibitingPylon, double xp) {
		super();
		this.growingBlock = growingBlock;
		this.inhibitingPylon = inhibitingPylon;
		this.leechedXP = xp;
	}

	public Block getGrowingBlock() {
		return growingBlock;
	}

	public XPylon getInhibitingPylon() {
		return inhibitingPylon;
	}

	public double getLeechedXP() {
		return leechedXP;
	}

	public void setLeechedXP(int leechedXP) {
		this.leechedXP = leechedXP;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
}
