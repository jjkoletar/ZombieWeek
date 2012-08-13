package com.hawkheart.bukkit.zombieweek;

import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieWeekEntityListener implements Listener {
	Random rand = new Random();

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;

		if (event.getDamager() instanceof Zombie) {
			event.setDamage(event.getDamage() * ZombieWeek.damageMultiplier);
			if (event.getEntity() instanceof Player) {
				int chance = rand.nextInt(100) + 1;
				Player p = (Player) event.getEntity();
				if (chance < 10) {

					p.addPotionEffect(new PotionEffect(
							PotionEffectType.WEAKNESS, 20 * 5, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
							20 * 30, 1));
				}
				if (chance < 5) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
							20 * 5, 1));
				}
				if (chance < 2) {
					p.addPotionEffect(new PotionEffect(
							PotionEffectType.CONFUSION, 20 * 30, 2));
					p.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
							20 * 5, 1));
				}
			}
		}
		if (event.getDamager() instanceof Player
				&& event.getEntity() instanceof Zombie) {
			event.setDamage(event.getDamage() / ZombieWeek.healthMultiplier);
		}
	}

	@EventHandler
	public void onEntityCombust(EntityCombustEvent event) {
		if (event.getEntityType() == EntityType.ZOMBIE) {
			event.setCancelled(true);
		}
	}

}
