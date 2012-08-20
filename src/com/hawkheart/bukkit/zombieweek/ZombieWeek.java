package com.hawkheart.bukkit.zombieweek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieWeek extends JavaPlugin {
	private boolean currentlyEnabled;

	public static int healthMultiplier;
	public static int damageMultiplier;
	
	public static String waveStart;
	public static String waveEnd;
	
	public static boolean useWaveStartMessage;
	public static boolean useWaveEndMessage;
	
	public static int zombiesPerWave;
	public static int secondsBetweenWaves;
	
	public static int spawnAreaStart;
	public static int spawnAreaEnd;
	
	public static boolean enableWave;
	
	public void onEnable() {
		this.saveDefaultConfig();
		this.loadConfig();
		
		this.enableRepeating();

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
		msg[1] = ChatColor.DARK_RED + "Commands: enable, disable, wave, reload";

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
				} else {
					sender.sendMessage(getHelpMessage());
				}
				break;
			default:
				sender.sendMessage(getHelpMessage());
				break;
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
			this.enableRepeating();
			sender.sendMessage(ChatColor.DARK_AQUA + "Enabled!");
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Already Enabled");
		}
	}
	
	private void enableRepeating() {
		this.getServer().getScheduler().cancelTasks(this);
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ZWSpawnZombieTask(this), 0L, secondsBetweenWaves * 20L);
		
		currentlyEnabled = true;
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
		sender.sendMessage(ChatColor.DARK_BLUE + "FORCING WAVE.");
		this.getServer().getScheduler()
				.scheduleSyncDelayedTask(this, new ZWSpawnZombieTask(this), 0L);
	}

	private void loadConfig() {
		this.reloadConfig();

		healthMultiplier = this.getConfig().getInt(
				"combat.zombiehealth");
		damageMultiplier = this.getConfig().getInt(
				"combat.zombiedamage");
		
		
		useWaveStartMessage = this.getConfig().getBoolean("messages.enable.wavebegin");
		useWaveEndMessage = this.getConfig().getBoolean("messages.enable.wavedone");
		
		waveStart = this.getConfig().getString("messages.wavebegin");
		waveEnd = this.getConfig().getString("messages.wavedone");
		
		zombiesPerWave = this.getConfig().getInt("wave.zombies");
		secondsBetweenWaves = this.getConfig().getInt("wave.secondsbetween");
		
		spawnAreaStart = this.getConfig().getInt("wave.spawn.startdistance");
		spawnAreaEnd = this.getConfig().getInt("wave.spawn.enddistance");
		
		enableWave = this.getConfig().getBoolean("wave.enable");
		
		if (secondsBetweenWaves <= 0) {
			throw new IllegalArgumentException("Time between waves cannot be less than one second.");
		}
		
		
		this.enableRepeating();

		System.out.println("Health multiplier " + healthMultiplier
				+ ", damage multiplier " + damageMultiplier);
		
	}

}
