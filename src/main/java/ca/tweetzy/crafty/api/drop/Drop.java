package ca.tweetzy.crafty.api.drop;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.sync.Identifiable;
import ca.tweetzy.crafty.api.sync.Storeable;
import ca.tweetzy.crafty.api.sync.Synchronize;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public abstract class Drop implements Identifiable<UUID>, Storeable<Drop>, Synchronize {

	protected UUID id;

	protected DropType dropType;
	protected ItemStack item;
	protected double chance;
	protected List<String> commands;

	@Override
	public void store(@NonNull Consumer<Drop> stored) {
		Crafty.getDataManager().insertTrackedDrop(this, (ex, result) -> {
			if (ex == null) {
				stored.accept(result);
			}
		});
	}

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().deleteTrackedDrop(this, (error, updateStatus) -> {
			if (updateStatus) {
				Crafty.getDropManager().remove(this);
			}

			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});

	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().updateTrackedDrop(this, (error, updateStatus) -> {
			if (syncResult != null)
				syncResult.accept(error == null ? updateStatus ? SynchronizeResult.SUCCESS : SynchronizeResult.FAILURE : SynchronizeResult.FAILURE);
		});
	}

	public enum DropType {
		MOB, BLOCK
	}
}
