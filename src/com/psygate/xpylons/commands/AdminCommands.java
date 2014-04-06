package com.psygate.xpylons.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.psygate.xpylons.XPylonCommand;
import com.psygate.xpylons.XPylons;
import com.psygate.xpylons.entities.XPBlock;
import com.psygate.xpylons.entities.XPylon;

public class AdminCommands implements XPylonCommand {
	private static final String[] commands = { "xpylonslist", "xpylonspurge", "xpylonstemplate", "xpylonssetxp" };
	private LinkedList<String> purger = new LinkedList<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "Only operators can do that. Infraction logged.");
		} else {
			if (command.getName().equals(commands[0])) {
				listPylons(sender);
			} else if (command.getName().equals(commands[1])) {
				purge(sender, args);
			} else if (command.getName().equals(commands[2])) {
				setTemplate(sender);
			} else if (command.getName().equals(commands[3])) {
				setXPforPylon(sender, args);
			}
		}

		return true;
	}

	// CHECK FOR WORLD UID
	private void setXPforPylon(CommandSender sender, String[] args) {
		int action = 0; // 0 = set, 1 = add, 2 = subtract
		if (args.length != 5 && args.length != 1) {
			sender.sendMessage(XPylons.getInstance().getCommand(commands[3]).getUsage());
		} else if (args.length == 5) {
			int xp, x, y, z;
			String world;
			try {
				xp = Integer.parseInt(args[0]);
				if (args[0].startsWith("+")) {
					action = 1;
				} else if (args[0].startsWith("-")) {
					action = 2;
				} else {
					action = 0;
				}
				x = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);
				z = Integer.parseInt(args[3]);
				world = args[4];
			} catch (Exception e) {
				sender.sendMessage(XPylons.getInstance().getCommand(commands[3]).getUsage());
				return;
			}
			XPylon pylon = XPylons.getDBLayer().getPylonFor(x, y, z, world);
			if (pylon == null) {
				world = Bukkit.getServer().getWorld(world).getUID().toString();
			}

			pylon = XPylons.getDBLayer().getPylonFor(x, y, z, world);
			if (pylon == null) {
				sender.sendMessage(ChatColor.RED + "Pylon not found.");
				return;
			}
			if (action == 0) {
				pylon.setXp(xp);
			} else {
				pylon.setXp(pylon.getXp() + xp);
			}
			XPylons.getDBLayer().update(pylon);
			sender.sendMessage(ChatColor.GREEN + "Added " + xp + " xp to the pylon.");

		} else if (args.length == 1) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only player admins can issue this command in this form.");
				return;
			}
			int xp;
			try {
				xp = Integer.parseInt(args[0]);
			} catch (Exception e) {
				sender.sendMessage(XPylons.getInstance().getCommand(commands[3]).getUsage());
				return;
			}

			XPylon pylon = XPylons.getDBLayer().getPylonFor(((Player) sender).getLocation().getBlock());
			if (pylon == null) {
				sender.sendMessage(ChatColor.RED + "Pylon not found. You are not close to a pylon.");
				return;
			}

			pylon.setXp(pylon.getXp() + xp);
			XPylons.getDBLayer().update(pylon);

			sender.sendMessage(ChatColor.GREEN + "Added " + xp + " xp to the pylon.");
		} else {
			sender.sendMessage(XPylons.getInstance().getCommand(commands[3]).getUsage());
		}
	}

	@SuppressWarnings("deprecation")
	private void setTemplate(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can do that.");
		} else {
			LinkedList<Block> template = new LinkedList<Block>();
			@SuppressWarnings("deprecation")
			Block base = ((Player) sender).getTargetBlock(null, 10);
			if (base == null) {
				sender.sendMessage("No template in sight. Look at it and try again.");
			} else {
				Stack<Block> explore = new Stack<Block>();
				explore.push(base);
				while (!explore.isEmpty()) {
					Block cur = explore.pop();
					template.add(cur);
					for (BlockFace face : BlockFace.values()) {
						Block next = cur.getRelative(face);
						if (!cur.equals(next) && !template.contains(next) && next != null && next.getType() != null
								&& next.getType() != Material.AIR) {
							explore.push(next);
							((Player) sender).sendBlockChange(next.getLocation(), Material.REDSTONE_BLOCK, (byte) 0);
						}
					}

					if (template.size() > 100) {
						sender.sendMessage("Template to big. Make sure there is air around it everywhere.");
						return;
					}
				}
			}

			LinkedList<XPBlock> blocks = new LinkedList<XPBlock>();
			for (Block b : template) {
				((Player) sender).sendBlockChange(b.getLocation(), b.getType(), b.getData());
				blocks.add(new XPBlock(b.getX() - base.getX(), b.getY() - base.getY(), b.getZ() - base.getZ(), b
						.getType()));
			}

			XPylons.getConfiguration().setTemplate(blocks);
			sender.sendMessage("Template set.");
		}
	}

	private void purge(CommandSender sender, String[] args) {
		if (!purger.contains(sender.getName())) {
			sender.sendMessage(ChatColor.RED + "IF YOU REALLY WANT TO PURGE ALL PYLONS, TYPE \"/xpylonspurge ALL\"");
			purger.add(sender.getName());
		} else {
			purger.remove(sender.getName());
			if (args.length != 1 || !args[0].equals("ALL")) {
				sender.sendMessage(ChatColor.RED + "IF YOU REALLY WANT TO PURGE ALL PYLONS, TYPE \"/xpylonspurge ALL\"");
				purger.add(sender.getName());
			} else {
				XPylons.getDBLayer().purgeAll();
			}
		}
	}

	private void listPylons(CommandSender sender) {
		sender.sendMessage(ChatColor.BLUE + "Pylons:");

		List<XPylon> pylons = XPylons.getDBLayer().getPylons();
		if (pylons.size() == 0) {
			sender.sendMessage(ChatColor.RED + "-- NO PYLONS --");
		} else {
			for (XPylon pylon : pylons) {
				sender.sendMessage(ChatColor.WHITE + pylonString(pylon));
			}

		}
	}

	private String pylonString(XPylon pylon) {
		String worldname = Bukkit.getWorld(UUID.fromString(pylon.getWorldUID())).getName();
		return "(" + pylon.getId() + ")" + "[" + pylon.getCx() + "," + pylon.getCy() + "," + pylon.getCz() + ","
				+ worldname + "] XP: " + pylon.getXp();
	}

	@Override
	public String[] getCommands() {
		return commands;
	}

}
