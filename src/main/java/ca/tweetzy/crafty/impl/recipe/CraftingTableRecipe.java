package ca.tweetzy.crafty.impl.recipe;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.model.StringUtil;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.SerializeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;

@Getter
@Setter
public final class CraftingTableRecipe extends CustomRecipe {

	private boolean shapeless;
	private boolean requireStackCounts;
	private String format;
	private HashMap<ItemStack, Character> mapping;
	private ItemStack result;

	public CraftingTableRecipe(String id, boolean shapeless, boolean requireStackCounts, String format, HashMap<ItemStack, Character> mapping, ItemStack result) {
		super(id.toLowerCase(), RecipeType.CRAFTING);
		this.shapeless = shapeless;
		this.requireStackCounts = requireStackCounts;
		this.format = format;
		this.mapping = mapping;
		this.result = result;
	}

	public CraftingTableRecipe(String id) {
		this(id, false, false, "         ", new HashMap<>(), QuickItem.of(CompMaterial.DIAMOND_SWORD).make());
	}

	@Override
	public void register() {
		if (this.shapeless) {
			final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(this.getKey(), this.result);
			this.mapping.forEach((item, character) -> shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(item)));

			Crafty.getInstance().getServer().getScheduler().runTask(Crafty.getInstance(), () -> {
				Crafty.getInstance().getServer().addRecipe(shapelessRecipe);
				Bukkit.getOnlinePlayers().forEach(player -> {
					if (!player.hasDiscoveredRecipe(this.getKey()))
						player.discoverRecipe(this.getKey());
				});
			});

		} else {
			final ShapedRecipe shapedRecipe = new ShapedRecipe(this.getKey(), this.result);
			shapedRecipe.shape(StringUtil.divideIntoChunks(this.format, 3).toArray(new String[0]));

			this.mapping.forEach((item, character) -> shapedRecipe.setIngredient(character, new RecipeChoice.ExactChoice(item)));

			// register the item
			Crafty.getInstance().getServer().getScheduler().runTask(Crafty.getInstance(), () -> {
				Crafty.getInstance().getServer().addRecipe(shapedRecipe);

				Bukkit.getOnlinePlayers().forEach(player -> {
					if (!player.hasDiscoveredRecipe(this.getKey()))
						player.discoverRecipe(this.getKey());
				});
			});
		}
	}

	@Override
	public String getJSONString() {
		final JsonObject object = new JsonObject();

		object.addProperty("key", this.id.toLowerCase());
		object.addProperty("shapeless", this.shapeless);
		object.addProperty("requireStackCounts", this.requireStackCounts);
		object.addProperty("format", this.format);

		final JsonArray mappingArr = new JsonArray();
		this.mapping.forEach((item, character) -> {
			JsonObject ingredientMap = new JsonObject();
			ingredientMap.addProperty("character", character);
			ingredientMap.addProperty("item", SerializeUtil.encodeItem(item));

			mappingArr.add(ingredientMap);
		});

		object.add("mapping", mappingArr);
		object.addProperty("result", SerializeUtil.encodeItem(this.result));

		return object.toString();
	}

	public static CraftingTableRecipe decode(final String json) {
		final JsonObject object = JsonParser.parseString(json).getAsJsonObject();
		HashMap<ItemStack, Character> mapping = new HashMap<>();

		JsonArray mappingArray = object.get("mapping").getAsJsonArray();
		mappingArray.forEach(element -> {
			final JsonObject obj = element.getAsJsonObject();
			mapping.put(SerializeUtil.decodeItem(obj.get("item").getAsString()), obj.get("character").getAsString().charAt(0));
		});

		//String id, boolean shapeless, boolean requireStackCounts, String format, HashMap<ItemStack, Character> mapping, ItemStack result
		return new CraftingTableRecipe(
				object.get("key").getAsString(),
				object.get("shapeless").getAsBoolean(),
				object.get("requireStackCounts").getAsBoolean(),
				object.get("format").getAsString(),
				mapping,
				SerializeUtil.decodeItem(object.get("result").getAsString())
		);
	}
}
