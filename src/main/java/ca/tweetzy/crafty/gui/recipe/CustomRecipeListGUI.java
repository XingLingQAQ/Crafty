package ca.tweetzy.crafty.gui.recipe;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.crafty.impl.recipe.CraftyCookingRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyBlastFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyCampfireRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftySmokerRecipe;
import ca.tweetzy.crafty.model.StringUtil;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.ItemUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CustomRecipeListGUI extends CraftyPagedGUI<CustomRecipe> {

	private final Gui parent;
	private RecipeType recipeType;

	public CustomRecipeListGUI(Gui parent, @NonNull Player player, RecipeType recipeType) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eAll Recipes", 6, new ArrayList<>(Crafty.getRecipeManager().getValues()));
		this.parent = parent;
		this.recipeType = recipeType;
		draw();
	}

	public CustomRecipeListGUI(Gui parent, @NonNull Player player) {
		this(parent, player, RecipeType.ALL);
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();

		this.items = new ArrayList<>(Crafty.getRecipeManager().getValues());

		if (this.recipeType != RecipeType.ALL)
			this.items = this.items.stream().filter(recipe -> recipe.getRecipeType() == this.recipeType).collect(Collectors.toList());
	}

	@Override
	protected void drawFixed() {

		// add button
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lNew Recipe</GRADIENT:26d5ed>")
				.lore(
						"",
						"&e&lClick &8» &7to recipe creator"
				)
				.make(), click -> click.manager.showGUI(click.player, new RecipeTypeSelectGUI(this, click.player, selected -> {

			if (selected == RecipeType.CRAFTING)
				click.manager.showGUI(click.player, new CraftingTableRecipeGUI(this, this.player, new CraftingTableRecipe("craftingrecipe"), false));
			else
				click.manager.showGUI(click.player, new CookingRecipeGUI(this, this.player, switch (selected) {
					case FURNACE -> new CraftyFurnaceRecipe("furnacerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
					case BLAST_FURNACE -> new CraftyBlastFurnaceRecipe("blastfurnacerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
					case CAMPFIRE -> new CraftyCampfireRecipe("campfirerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
					case SMOKER -> new CraftySmokerRecipe("smokerrecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
					default -> throw new IllegalStateException("Unexpected value: " + selected);
				}, false));
		})));

		setButton(getRows() - 1, 6, QuickItem.of(switch (this.recipeType) {
					case ALL -> CompMaterial.REPEATER;
					case CRAFTING -> CompMaterial.CRAFTING_TABLE;
					case FURNACE -> CompMaterial.FURNACE;
					case BLAST_FURNACE -> CompMaterial.BLAST_FURNACE;
					case CAMPFIRE -> CompMaterial.CAMPFIRE;
					case SMOKER -> CompMaterial.SMOKER;
				})
				.name("<GRADIENT:3dcf50>&lFilter</GRADIENT:26d5ed>")
				.lore(
						"&8Used to filter recipes",
						"",
						"&7Current Filter&F: &e" + ChatUtil.capitalizeFully(this.recipeType) + " Recipes",
						"",
						"&e&lClick &8» &7to navigate filter"
				)
				.make(), click -> {

			this.recipeType = this.recipeType.next();
			draw();
		});

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(CustomRecipe recipe) {
		final List<String> baseLore = List.of("&e&lLeft Click &8» &7to edit recipe", "&4&lRight Click &8» &cTo delete recipe");

		if (recipe instanceof final CraftingTableRecipe craftingTableRecipe) {
			final List<String> itemMapping = new ArrayList<>();
			craftingTableRecipe.getMapping().forEach((item, character) -> itemMapping.add("&e" + character + " &7- &b" + ItemUtil.getItemName(item)));

			return QuickItem
					.of(craftingTableRecipe.getResult())
					.name("<GRADIENT:3dcf50>&l" + recipe.getId() + "</GRADIENT:26d5ed>")
					.lore("&e" + ChatUtil.capitalizeFully(recipe.getRecipeType()) + " Recipe", "&7Format&f:")
					.lore(StringUtil.divideIntoChunks(craftingTableRecipe.getFormat(), 3))
					.lore("", "&7Item Mapping")
					.lore(itemMapping)
					.lore("")
					.lore(baseLore)
					.make();
		}

		if (recipe instanceof final CraftyCookingRecipe cookingRecipe) {
			final double cookTimeSec = (double) cookingRecipe.getCookingTime() / 20;

			return QuickItem
					.of(cookingRecipe.getResult())
					.name("<GRADIENT:3dcf50>&l" + recipe.getId() + "</GRADIENT:26d5ed>")
					.lore("&e" + ChatUtil.capitalizeFully(recipe.getRecipeType()) + " Recipe")
					.lore(
							"&7Input Item&F: &b" + ItemUtil.getItemName(cookingRecipe.getInput()),
							"&7Result Item&F: &b" + ItemUtil.getItemName(cookingRecipe.getResult()),
							"",
							"&7Cooking Time&F: &e" + cookTimeSec,
							"&7Experience&F: &e" + cookingRecipe.getExperience(),
							""
					)
					.lore(baseLore)
					.make();
		}


		return null;
	}

	@Override
	protected void onClick(CustomRecipe recipe, GuiClickEvent event) {
		if (event.clickType == ClickType.LEFT)
			if (recipe.getRecipeType() != RecipeType.CRAFTING)
				event.manager.showGUI(event.player, new CookingRecipeGUI(this.parent, event.player, (CraftyCookingRecipe) recipe, true));
			else
				event.manager.showGUI(event.player, new CraftingTableRecipeGUI(this.parent, event.player, (CraftingTableRecipe) recipe, true));

		if (event.clickType == ClickType.RIGHT)
			recipe.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					event.manager.showGUI(event.player, new CustomRecipeListGUI(this.parent, event.player));
			});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
