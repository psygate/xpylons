package com.psygate.xpylons.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.psygate.xpylons.XPylonCommand;
import com.psygate.xpylons.XPylons;
import com.psygate.xpylons.entities.XPylon;
import com.psygate.xpylons.events.PylonXPRetrievalEvent;

public class PlayerCommands implements CommandExecutor, XPylonCommand {
	private final static String[] commands = { "xpylonsshowxp", "xpylonsretrievexp", "xpylonsstore" };

	@Override
	public String[] getCommands() {
		return commands;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equals(commands[0])) {
			showXPto(sender);
		} else if (command.getName().equals(commands[1])) {
			retrieveXP(sender, args);
		} else if (command.getName().equals(commands[2])) {
			storeXP(sender, args);
		}

		return true;
	}

	private void storeXP(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			int pexp = player.getTotalExperience();
			int xp = pexp;
			if (args.length == 1) {
				try {
					xp = Integer.parseInt(args[0]);
				} catch (Exception e) {
					sender.sendMessage(XPylons.getInstance().getCommand(commands[2]).getUsage());
					return;
				}
			} else {
				sender.sendMessage(XPylons.getInstance().getCommand(commands[2]).getUsage());
				return;
			}

			if (xp <= 0) {
				sender.sendMessage(ChatColor.RED + "Only positive amounts of xp can be transfered.");
			}

			XPylon pylon = XPylons.getDBLayer().getPylonFor(player.getLocation().getBlock());
			if (pylon == null) {
				sender.sendMessage(ChatColor.RED + "You must be close to a pylon to use this command.");
				return;
			}

			if (xp > player.getTotalExperience()) {
				sender.sendMessage(ChatColor.RED + "You don't have enough xp to do that.");
				return;
			}

			// Bukkit.getPluginManager().callEvent(new
			// PlayerExpChangeEvent((Player) sender, -xp));
			player.setTotalExperience(0);
			player.setLevel(0);
			player.setExp(0);
			if (pexp - xp > 0) {
				player.giveExp(pexp - xp);
				player.setExp(pexp - xp);
			}
			// player.setTotalExperience(0);
			// sender.sendMessage(pexp + " " + xp + " : " + (pexp - xp));
			// player.giveExp(pexp - xp);

			pylon.setXp(pylon.getXp() + xp);
			XPylons.getDBLayer().save(pylon);

			sender.sendMessage(ChatColor.GREEN + "Transfered " + xp + " xp to the pylon.");

		} else {
			sender.sendMessage(ChatColor.RED + "Only players can do that.");
		}
	}

	private void retrieveXP(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			XPylon pylon = XPylons.getDBLayer().getPylonFor(p.getLocation().getBlock());
			if (pylon == null) {
				sender.sendMessage(ChatColor.RED + "No pylon close by.");
			} else if (args.length != 1) {
				sender.sendMessage(XPylons.getInstance().getCommand(commands[1]).getUsage());
			} else {
				int retrieve = 0;
				try {
					retrieve = Integer.parseInt(args[0]);
					if (retrieve <= 0) {

						sender.sendMessage(ChatColor.RED + "Only positive amounts of xp can be transfered.");

						return;
					}
					double xp = pylon.getXp();
					if (retrieve > xp) {
						pylon.setXp(0);
						XPylons.getDBLayer().update(pylon);
						retrieve = (int) xp;
					} else if (retrieve <= xp) {
						pylon.setXp(xp - retrieve);
						XPylons.getDBLayer().update(pylon);
					}
					PylonXPRetrievalEvent pxre = new PylonXPRetrievalEvent(p, pylon, retrieve);
					Bukkit.getPluginManager().callEvent(pxre);
					if (pxre.isCancelled()) {
						sender.sendMessage(ChatColor.RED + "Cannot retrieve xp.");
					} else {
						sender.sendMessage(ChatColor.GREEN + "Retrieved " + retrieve + " xp.");
						p.giveExp(retrieve);
					}

				} catch (Exception e) {
					sender.sendMessage(XPylons.getInstance().getCommand(commands[1]).getUsage());
				}
			}

		} else {
			sender.sendMessage(ChatColor.RED + "Only players can retrieve xp.");
		}
	}

	private void showXPto(CommandSender sender) {
		if (sender instanceof Player) {
			XPylon pylon = XPylons.getDBLayer().getPylonFor(((Player) sender).getLocation().getBlock());
			if (pylon == null) {
				sender.sendMessage(ChatColor.RED + "No pylon close by.");
			} else {
				sender.sendMessage(ChatColor.BLUE + "Accumulated XP: " + pylon.getXp());
			}
		} else if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(ChatColor.BLUE + "Pylon listing:");
			List<XPylon> pylons = XPylons.getDBLayer().getPylons();
			if (pylons.size() == 0) {
				sender.sendMessage(ChatColor.RED + "-- NO PYLONS --");
			} else {
				for (XPylon pylon : pylons) {
					sender.sendMessage(ChatColor.WHITE + pylonString(pylon));
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Whatever you are, I can't process your request.");
		}
	}

	private String pylonString(XPylon pylon) {
		String worldname = Bukkit.getWorld(UUID.fromString(pylon.getWorldUID())).getName();
		return "(" + pylon.getId() + ")" + "[" + pylon.getCx() + "," + pylon.getCy() + "," + pylon.getCz() + ","
				+ worldname + "] XP: " + pylon.getXp();
	}
}
