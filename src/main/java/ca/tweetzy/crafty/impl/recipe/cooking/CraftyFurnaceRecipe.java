package ca.tweetzy.crafty.impl.recipe.cooking;

import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.impl.recipe.CraftyCookingRecipe;
import org.bukkit.inventory.ItemStack;

public final class CraftyFurnaceRecipe extends CraftyCookingRecipe {

	public CraftyFurnaceRecipe(String id, ItemStack input, ItemStack result, float experience, int cookingTime) {
		super(id, RecipeType.FURNACE, input, result, experience, cookingTime);
	}
}
