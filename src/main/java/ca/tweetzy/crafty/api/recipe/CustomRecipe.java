package ca.tweetzy.crafty.api.recipe;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.sync.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@AllArgsConstructor
public abstract class CustomRecipe implements Storeable<CustomRecipe>, Identifiable<String>, Jsonable, Synchronize {

	@Setter
	protected String id;

	@Getter
	protected RecipeType recipeType;

	public NamespacedKey getKey() {
		return new NamespacedKey(Crafty.getInstance(), id.toLowerCase());
	}

	@NotNull
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void store(@NonNull Consumer<CustomRecipe> stored) {
		Crafty.getDataManager().insertCustomRecipe(this, (ex, result) -> {
			if (ex == null) {
				stored.accept(result);
			}
		});
	}

	@Override
	public void unStore(@Nullable Consumer<SynchronizeResult> syncResult) {
		Crafty.getDataManager().deleteCustomRecipe(this, (error, res) -> {
			if (error == null && res) {
				if (syncResult != null)
					syncResult.accept(SynchronizeResult.SUCCESS);
				Crafty.getRecipeManager().remove(this.getId());
				Crafty.getInstance().getServer().removeRecipe(this.getKey());

				// un-discover recipe
				Bukkit.getOnlinePlayers().forEach(player -> {
					if (player.hasDiscoveredRecipe(this.getKey()))
						player.undiscoverRecipe(this.getKey());
				});

			} else if (syncResult != null)
				syncResult.accept(SynchronizeResult.FAILURE);
		});
	}

	public abstract void register();
}
