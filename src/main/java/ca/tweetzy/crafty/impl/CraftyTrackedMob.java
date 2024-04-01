package ca.tweetzy.crafty.impl;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.api.drop.TrackedOptions;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public final class CraftyTrackedMob implements TrackedMob {

	private final EntityType mob;
	private TrackedOptions options;
	private List<MobDrop> drops;

	@Override
	public EntityType getEntity() {
		return this.mob;
	}

	@Override
	public TrackedOptions getOptions() {
		return this.options;
	}

	@Override
	public List<MobDrop> getDrops() {
		return this.drops;
	}

	@Override
	public void store(@NonNull Consumer<TrackedMob> stored) {
		Crafty.getDataManager().insertTrackedMob(this, (ex, result) -> {
			if (ex == null) {
				stored.accept(result);
			}
		});
	}

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().deleteTrackedMob(this, (error, updateStatus) -> {
			if (updateStatus) {
				Crafty.getMobDropManager().remove(this.mob);
			}

			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});

	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().updateTrackedMob(this, (error, updateStatus) -> {
			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});
	}
}
