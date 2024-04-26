package ca.tweetzy.crafty.model.manager;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.manager.KeyValueManager;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public final class CustomRecipeManager extends KeyValueManager<String, CustomRecipe> {

	public CustomRecipeManager() {
		super("Custom Recipe");
	}

	public void addRecipe(@NonNull final CustomRecipe recipe, @NonNull final Consumer<Boolean> created) {
		recipe.store(storedRecipe -> {
			if (storedRecipe != null) {
				add(recipe.getId(), recipe);
				recipe.register();
				created.accept(true);
			} else {
				created.accept(false);
			}
		});
	}

	@Override
	public void load() {
		clear();

		Crafty.getDataManager().getCustomRecipes((error, result) -> {
			if (error == null)
				result.forEach(recipe -> {
					add(recipe.getId(), recipe);
					recipe.register();

					//
					Bukkit.getOnlinePlayers().forEach(player -> {
						if (!player.hasDiscoveredRecipe(recipe.getKey()))
							player.discoverRecipe(recipe.getKey());
					});
				});
		});
	}
}
