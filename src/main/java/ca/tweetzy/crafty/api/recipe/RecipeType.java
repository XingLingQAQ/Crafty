package ca.tweetzy.crafty.api.recipe;

import ca.tweetzy.crafty.api.sync.Navigable;

public enum RecipeType implements Navigable<RecipeType> {

	ALL, // JUST HERE FOR FILTERING

	CRAFTING,
	FURNACE,
	BLAST_FURNACE,
	CAMPFIRE,
	SMOKER;

	@Override
	public Class<RecipeType> enumClass() {
		return RecipeType.class;
	}
}
