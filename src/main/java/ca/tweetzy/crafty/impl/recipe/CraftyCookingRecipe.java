package ca.tweetzy.crafty.impl.recipe;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyBlastFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyCampfireRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftySmokerRecipe;
import ca.tweetzy.flight.utils.SerializeUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Item;
import org.bukkit.inventory.*;

@Getter
@Setter
public abstract class CraftyCookingRecipe extends CustomRecipe {

	private ItemStack input;
	private ItemStack result;
	private float experience;
	private int cookingTime;

	public CraftyCookingRecipe(String id, RecipeType recipeType, ItemStack input, ItemStack result, float experience, int cookingTime) {
		super(id.toLowerCase(), recipeType);
		this.recipeType = recipeType;
		this.input = input;
		this.result = result;
		this.experience = experience;
		this.cookingTime = cookingTime;
	}

	@Override
	public void register() {
		CookingRecipe cookingRecipe = null;
		switch (this.recipeType) {
			case FURNACE -> cookingRecipe = new FurnaceRecipe(this.getKey(), this.input, new RecipeChoice.ExactChoice(this.result), this.experience, this.cookingTime);
			case BLAST_FURNACE -> cookingRecipe = new BlastingRecipe(this.getKey(), this.input, new RecipeChoice.ExactChoice(this.result), this.experience, this.cookingTime);
			case CAMPFIRE -> cookingRecipe = new CampfireRecipe(this.getKey(), this.input, new RecipeChoice.ExactChoice(this.result), this.experience, this.cookingTime);
			case SMOKER -> cookingRecipe = new SmokingRecipe(this.getKey(), this.input, new RecipeChoice.ExactChoice(this.result), this.experience, this.cookingTime);
		}

		CookingRecipe finalCookingRecipe = cookingRecipe;
		Crafty.getInstance().getServer().getScheduler().runTask(Crafty.getInstance(), () -> Crafty.getInstance().getServer().addRecipe(finalCookingRecipe));
	}

	@Override
	public String getJSONString() {
		final JsonObject object = new JsonObject();

		object.addProperty("key", this.id.toLowerCase());
		object.addProperty("input", SerializeUtil.encodeItem(this.input));
		object.addProperty("result", SerializeUtil.encodeItem(this.result));
		object.addProperty("experience", this.experience);
		object.addProperty("cookingTime", this.cookingTime);

		return object.toString();
	}

	public static CraftyCookingRecipe decode(RecipeType type, final String json) {
		final JsonObject object = JsonParser.parseString(json).getAsJsonObject();

		return switch (type) {
			case FURNACE -> new CraftyFurnaceRecipe(
					object.get("key").getAsString(),
					SerializeUtil.decodeItem(object.get("input").getAsString()),
					SerializeUtil.decodeItem(object.get("result").getAsString()),
					object.get("experience").getAsFloat(),
					object.get("cookingTime").getAsInt()
			);
			case BLAST_FURNACE -> new CraftyBlastFurnaceRecipe(
					object.get("key").getAsString(),
					SerializeUtil.decodeItem(object.get("input").getAsString()),
					SerializeUtil.decodeItem(object.get("result").getAsString()),
					object.get("experience").getAsFloat(),
					object.get("cookingTime").getAsInt()
			);
			case CAMPFIRE -> new CraftyCampfireRecipe(
					object.get("key").getAsString(),
					SerializeUtil.decodeItem(object.get("input").getAsString()),
					SerializeUtil.decodeItem(object.get("result").getAsString()),
					object.get("experience").getAsFloat(),
					object.get("cookingTime").getAsInt()
			);
			case SMOKER -> new CraftySmokerRecipe(
					object.get("key").getAsString(),
					SerializeUtil.decodeItem(object.get("input").getAsString()),
					SerializeUtil.decodeItem(object.get("result").getAsString()),
					object.get("experience").getAsFloat(),
					object.get("cookingTime").getAsInt()
			);
			default -> throw new IllegalStateException("Unexpected value: " + type);
		};
	}
}
