package ca.tweetzy.crafty.impl.drop;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.drop.TrackedOptions;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public final class CraftyTrackedBlock implements TrackedBlock {

	private final CompMaterial block;
	private TrackedOptions options;
	private List<BlockDrop> drops;

	@Override
	public CompMaterial getBlock() {
		return this.block;
	}

	@Override
	public TrackedOptions getOptions() {
		return this.options;
	}

	@Override
	public List<BlockDrop> getDrops() {
		return this.drops;
	}

	@Override
	public void store(@NonNull Consumer<TrackedBlock> stored) {
		Crafty.getDataManager().insertTrackedBlock(this, (ex, result) -> {
			if (ex == null) {
				stored.accept(result);
			}
		});
	}

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().deleteTrackedBlock(this, (error, updateStatus) -> {
			if (updateStatus) {
				Crafty.getBlockDropManager().remove(this.block);
			}

			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});

	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().updateTrackedBlock(this, (error, updateStatus) -> {
			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});
	}
}
