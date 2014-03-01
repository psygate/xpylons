package com.psygate.xpylons.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.psygate.xpylons.entities.XPylon;

public class PylonXPRetrievalEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private final Player player;
	private final XPylon pylon;
	private int XPAmount;

	public PylonXPRetrievalEvent(Player player, XPylon pylon, int xPAmount) {
		super();
		this.player = player;
		this.pylon = pylon;
		this.XPAmount = xPAmount;
	}

	public int getXPAmount() {
		return XPAmount;
	}

	public void setXPAmount(int xPAmount) {
		this.XPAmount = xPAmount;
	}

	public Player getPlayer() {
		return player;
	}

	public XPylon getPylon() {
		return pylon;
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
