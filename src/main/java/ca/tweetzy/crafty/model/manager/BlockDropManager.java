package ca.tweetzy.crafty.model.manager;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.manager.KeyValueManager;
import ca.tweetzy.crafty.impl.BlockDrop;
import ca.tweetzy.crafty.impl.CraftyTrackedBlock;
import ca.tweetzy.crafty.impl.CraftyTrackedOptions;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class BlockDropManager extends KeyValueManager<CompMaterial, TrackedBlock> {

	public BlockDropManager() {
		super("Block Drop");
	}

	public boolean isTracked(@NonNull final CompMaterial block) {
		return this.managerContent.containsKey(block);
	}

	public void trackBlock(@NonNull final CompMaterial block, @NonNull final Consumer<Boolean> created) {
		if (this.managerContent.containsKey(block)) return;

		final TrackedBlock trackedBlock = new CraftyTrackedBlock(block, new CraftyTrackedOptions(true, new ArrayList<>()), new ArrayList<>());

		trackedBlock.store(storedBlock -> {
			if (storedBlock != null) {
				add(block, storedBlock);
				created.accept(true);
			} else {
				created.accept(false);
			}
		});
	}

	@Override
	public void load() {
		clear();

		Crafty.getDataManager().getTrackedBlocks((error, blocks) -> {
			if (error == null)
				for (TrackedBlock block : blocks) {
					Crafty.getDataManager().getTrackedDropsByBlock(block.getBlock(), (dropError, drops) -> {
						if (dropError == null)
							drops.forEach(drop -> block.getDrops().add((BlockDrop) drop));
					});

					add(block.getBlock(), block);
				}
		});
	}
}
