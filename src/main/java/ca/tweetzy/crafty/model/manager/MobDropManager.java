package ca.tweetzy.crafty.model.manager;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.api.manager.KeyValueManager;
import ca.tweetzy.crafty.impl.CraftyTrackedMob;
import ca.tweetzy.crafty.impl.CraftyTrackedOptions;
import ca.tweetzy.crafty.impl.MobDrop;
import lombok.NonNull;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class MobDropManager extends KeyValueManager<EntityType, TrackedMob> {

	public MobDropManager() {
		super("Mob Drop");
	}

	public boolean isTracked(@NonNull final EntityType mob) {
		return this.managerContent.containsKey(mob);
	}

	public void trackMob(@NonNull final EntityType mob, @NonNull final Consumer<Boolean> created) {
		if (this.managerContent.containsKey(mob)) return;

		final TrackedMob trackedMob = new CraftyTrackedMob(mob, new CraftyTrackedOptions(true, new ArrayList<>()), new ArrayList<>());

		trackedMob.store(storedMob -> {
			if (storedMob != null) {
				add(mob, storedMob);
				created.accept(true);
			} else {
				created.accept(false);
			}
		});
	}

	@Override
	public void load() {
		clear();

		Crafty.getDataManager().getTrackedMobs((error, mobs) -> {
			if (error == null)
				for (TrackedMob mob : mobs) {
					Crafty.getDataManager().getTrackedDropsByMob(mob.getEntity(), (dropError, drops) -> {
						if (dropError == null)
							drops.forEach(drop -> mob.getDrops().add((MobDrop) drop));
					});

					add(mob.getEntity(), mob);
				}
		});
	}
}
