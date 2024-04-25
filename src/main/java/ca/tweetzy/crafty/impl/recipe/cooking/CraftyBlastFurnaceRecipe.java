package ca.tweetzy.crafty.impl.recipe.cooking;

import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.impl.recipe.CraftyCookingRecipe;
import org.bukkit.inventory.ItemStack;

public final class CraftyBlastFurnaceRecipe extends CraftyCookingRecipe {

	public CraftyBlastFurnaceRecipe(String id, ItemStack input, ItemStack result, float experience, int cookingTime) {
		super(id, RecipeType.BLAST_FURNACE, input, result, experience, cookingTime);
	}
}
