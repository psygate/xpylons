package com.psygate.xpylons.watchutils;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.psygate.xpylons.Configuration;
import com.psygate.xpylons.WatchDogConfiguration;
import com.psygate.xpylons.XPylons;

public class WatchDog implements Runnable {
	private boolean isactive = false;
	private int id;

	private int ticks;
	private long last;
	private long interval = TimeUnit.SECONDS.toMillis(1);
	private int lastTicks = 20;

	public void setActive(boolean setting) {
		if (setting == isactive) {
			return;
		}

		if (setting == true) {
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(XPylons.getInstance(), this, 1, 1);
			last = System.currentTimeMillis();
		} else {
			Bukkit.getScheduler().cancelTask(id);
		}

		isactive = setting;
	}

	public void run() {
		WatchDogConfiguration co = XPylons.getWatchDogConfiguration();
		Configuration pco = XPylons.getConfiguration();
		long now = System.currentTimeMillis();
		ticks++;
		if (now - last >= interval) {
			if (ticks <= co.getStartAtTicks()) {
				float newrate = pco.getGrowthStopChance() - co.getAdjust();
				if (newrate < co.getLowerLimit()) {
					newrate = co.getLowerLimit();
				}
				pco.setGrowthStopChance(newrate);

				float msg = (float) ((Math.floor(newrate * 10000)) / 100);
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp()) {
						p.sendMessage("XPylons rate adjusted down: " + msg + "% (" + ticks + ")");
					}
				}
			}
			
			lastTicks = ticks;
			ticks = 0;
		}
		last = now;
	}
	
	public int getlastTicks() {
		return lastTicks;
	}

}
