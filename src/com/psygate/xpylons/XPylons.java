package com.psygate.xpylons;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.psygate.xpylons.commands.AdminCommands;
import com.psygate.xpylons.commands.PlayerCommands;
import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;
import com.psygate.xpylons.listeners.DebugListener;
import com.psygate.xpylons.listeners.XPylonListener;
import com.psygate.xpylons.watchutils.WatchDog;

public class XPylons extends JavaPlugin {
	private static XPylons instance;
	private Configuration conf;
	private WatchDogConfiguration wconf;
	private WatchDog dog;
	private DBLayer dblayer;
	
	public XPylons() {
		instance = this;
	}

	@Override
	public void onEnable() {
		instance = this;
		conf = new Configuration(this);
		wconf = new WatchDogConfiguration(this);
//		dog = new WatchDog();
//		dog.setActive(wconf.isActive());
		// Check DB model
		try {
			for (Class<?> cl : getDatabaseClasses()) {
				getDatabase().find(cl).findRowCount();
			}
		} catch (Exception e) {
			removeDDL();
			installDDL();
		}
		regListener(new XPylonListener());
		
		//DEBUG
//		dog.setActive(true);
//		regListener(new DebugListener());
		//DEBUG
		
		regCom(new PlayerCommands());
		regCom(new AdminCommands());
		dblayer = conf.getDBLayer();
	}

	private void regCom(XPylonCommand coms) {
		for (String command : coms.getCommands()) {
			getCommand(command).setExecutor(coms);
		}
	}

	private void regListener(Listener li) {
		getServer().getPluginManager().registerEvents(li, this);
	}

	@SuppressWarnings("serial")
	@Override
	public List<Class<?>> getDatabaseClasses() {
		return new LinkedList<Class<?>>() {
			{
				add(XPBlock.class);
				add(XPylon.class);
			}
		};
	}

	public static XPylons getInstance() {
		return instance;
	}

	public static Configuration getConfiguration() {
		return instance.conf;
	}

	public static WatchDogConfiguration getWatchDogConfiguration() {
		return instance.wconf;
	}

	public static DBLayer getDBLayer() {
		return instance.dblayer;
	}

	public static EbeanServer getDB() {
		return instance.getDatabase();
	}
	
	public static WatchDog getWatchDog() {
		return instance.dog;
		
	}
}
