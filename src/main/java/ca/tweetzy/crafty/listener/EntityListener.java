package ca.tweetzy.crafty.listener;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.impl.MobDrop;
import ca.tweetzy.crafty.model.Chance;
import ca.tweetzy.flight.utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public final class EntityListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onEntitySpawn(final CreatureSpawnEvent event) {
		final Entity entity = event.getEntity();
		entity.getPersistentDataContainer().set(Crafty.getCreatureSpawnKey(), PersistentDataType.STRING, event.getSpawnReason().name());
	}

	@EventHandler
	public void onEntityKill(final EntityDeathEvent event) {
		final LivingEntity entity = event.getEntity();
		// if the entity isn't tracked then ignore
		if (!Crafty.getMobDropManager().isTracked(entity.getType())) return;

		// grab tracked mob
		final TrackedMob trackedMob = Crafty.getMobDropManager().get(entity.getType());

		// is the current world blocked??
		if (trackedMob.getOptions().getBlockedWorlds().contains(entity.getWorld().getName())) return;

		// enable vanilla drops?
		if (!trackedMob.getOptions().dropDefaultItems()) {
			event.getDrops().clear();
		}

		// is the killer even a player
		if (entity.getKiller() == null) return;
		final Player killer = entity.getKiller();

		// grab pdc
		SpawnReason spawnReason = SpawnReason.NATURAL;
		if (entity.getPersistentDataContainer().has(Crafty.getCreatureSpawnKey(), PersistentDataType.STRING)) {
			final String possibleReason = entity.getPersistentDataContainer().get(Crafty.getCreatureSpawnKey(), PersistentDataType.STRING);
			assert possibleReason != null;
			spawnReason = Enum.valueOf(SpawnReason.class, possibleReason);
		}

		for (MobDrop drop : trackedMob.getDrops()) {
			// mob spawned by egg and drop is false
			if (!drop.isDropFromEgg() && (spawnReason == SpawnReason.EGG || spawnReason == SpawnReason.SPAWNER_EGG)) {
				continue;
			}

			// mob spawned from spawner and spawner drops is false
			if (!drop.isDropFromSpawner() && spawnReason == SpawnReason.SPAWNER) {
				continue;
			}

			// natural mobs
			if (Chance.byPercentage((float) drop.getChance())) {
				if (drop.getCondition().meetsConditions(entity, killer)) {
					entity.getWorld().dropItemNaturally(entity.getLocation(), drop.getItem());
					// execute commands

					// if killer from player
					drop.getCommands().forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replacer.replaceVariables(command, "player", killer.getName())));
				}
			}
		}

		// remove key afterwards
		if (entity.getPersistentDataContainer().has(Crafty.getCreatureSpawnKey(), PersistentDataType.STRING))
			entity.getPersistentDataContainer().remove(Crafty.getCreatureSpawnKey());
	}

}
