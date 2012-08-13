package com.hawkheart.bukkit.zombieweek;


import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ZWSpawnZombieTask implements Runnable {
	private ZombieWeek zw;
	
	private Random rand1;
	
	public ZWSpawnZombieTask(ZombieWeek zw) {
		this.zw = zw;
		rand1 = new Random();
	}
	
	@Override
	public void run() {
		zw.getServer().broadcastMessage("NEW WAVE SPAWNING...");
		
		for (Player p : zw.getServer().getOnlinePlayers()) {
			Location playerLocation = p.getLocation();
			int i = 0;
			while (i < 25) {
				boolean negX = rand1.nextBoolean();
				boolean negZ = rand1.nextBoolean();
				
				int nextX = rand1.nextInt(43) + 8;
				int nextZ = rand1.nextInt(43) + 8;
				
				if (negX) {
					nextX = -nextX;
				}
				
				if (negZ) nextX = -nextZ;
				
				Location loc = new Location(playerLocation.getWorld(), playerLocation.getX() + nextX, playerLocation.getY(), playerLocation.getZ() + nextZ);
				while (loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).isEmpty()) {
					loc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
				}
				
				int currentY = loc.getBlockY();
				boolean hasSpawned = false;
				while (hasSpawned == false) {
				loc = new Location(playerLocation.getWorld(), playerLocation.getX() + nextX, currentY, playerLocation.getZ() + nextZ);
				if (loc.getBlock().isEmpty() && loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()).isEmpty()) {
					loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
					i++;
					hasSpawned = true;
				}
				currentY++;
				}
			}
		}
		zw.getServer().broadcastMessage("WAVE DONE.");
	}

}
