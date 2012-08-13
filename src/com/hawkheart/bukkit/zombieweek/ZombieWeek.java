package com.hawkheart.bukkit.zombieweek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieWeek extends JavaPlugin {
	private boolean currentlyEnabled;

	public static int healthMultiplier;
	public static int damageMultiplier;

	public void onEnable() {
		currentlyEnabled = true;

		this.saveDefaultConfig();
		this.loadConfig();

		this.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(this, new ZWSpawnZombieTask(this),
						0L, 6000L);

		this.getServer().getPluginManager()
				.registerEvents(new ZombieWeekEntityListener(), this);
		this.getServer().getPluginManager()
				.registerEvents(new ZombieWeekPlayerListener(), this);
	}

	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		currentlyEnabled = false;
	}

	private String[] getHelpMessage() {
		String[] msg = new String[2];
		msg[0] = ChatColor.DARK_BLUE + "Usage: /zw <command>";
		msg[1] = ChatColor.DARK_RED + "Commands: enable, disable, wave";

		return msg;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (command.getName().equalsIgnoreCase("zw")) {
			switch (args.length) {
			case 1:
				if (args[0].equalsIgnoreCase("enable")) {
					handleEnable(sender);
				} else if (args[0].equalsIgnoreCase("disable")) {
					handleDisable(sender);
				} else if (args[0].equalsIgnoreCase("wave")) {
					handleWave(sender);
				} else if (args[0].equalsIgnoreCase("reload")) {
					this.handleReload(sender);
				} else
					sender.sendMessage(getHelpMessage());
			default:
				sender.sendMessage(getHelpMessage());
			}
			return true;
		}
		return false;
	}

	private void handleReload(CommandSender sender) {
		if (!sender.hasPermission("zw.reload")) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You do not have permission for this command.");
			return;
		}
		this.loadConfig();

		sender.sendMessage("Reloaded config!");
	}

	private void handleEnable(CommandSender sender) {
		if (!sender.hasPermission("zw.disablereload")) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You do not have permission for this command.");
			return;
		}
		if (currentlyEnabled == false) {
			this.getServer()
					.getScheduler()
					.scheduleSyncRepeatingTask(this,
							new ZWSpawnZombieTask(this), 0L, 6000L);
			currentlyEnabled = true;
			sender.sendMessage(ChatColor.DARK_AQUA + "Enabled!");
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Already Enabled");
		}
	}

	private void handleDisable(CommandSender sender) {
		if (!sender.hasPermission("zw.disablereload")) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You do not have permission for this command");
			return;
		}
		if (currentlyEnabled == true) {
			this.getServer().getScheduler().cancelTasks(this);
			sender.sendMessage(ChatColor.GREEN + "Disabled!");
			currentlyEnabled = false;
		} else {
			sender.sendMessage(ChatColor.BLUE + "Already disabled!");
		}
	}

	private void handleWave(CommandSender sender) {
		if (!sender.hasPermission("zw.forcewave")) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You do not have permission for this command.");
			return;
		}
		sender.sendMessage(ChatColor.BLACK + "FORCING WAVE.");
		this.getServer().getScheduler()
				.scheduleSyncDelayedTask(this, new ZWSpawnZombieTask(this), 0L);
	}

	private void loadConfig() {
		this.reloadConfig();

		healthMultiplier = this.getConfig().getInt(
				"combat.multipliers.zombiehealth");
		damageMultiplier = this.getConfig().getInt(
				"combat.multipliers.zombiedamage");

		System.out.println("Health multiplier " + healthMultiplier
				+ ", damage multiplier " + damageMultiplier);
	}

}
