package ca.tweetzy.crafty.gui.crafting;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import ca.tweetzy.crafty.gui.drops.block.BlockDropListGUI;
import ca.tweetzy.crafty.gui.selector.BlockSelectorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class CustomRecipeListGUI extends CraftyPagedGUI<CustomRecipe> {

	private final Gui parent;

	public CustomRecipeListGUI(Gui parent, @NonNull Player player) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eAll Recipes", 6, Crafty.getRecipeManager().getValues());
		this.parent = parent;
		draw();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
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
				.make(), click -> click.manager.showGUI(click.player, new CraftingTableRecipeGUI(this, click.player, new CraftingTableRecipe("recipename"))));

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(CustomRecipe recipe) {
		if (recipe instanceof final CraftingTableRecipe craftingTableRecipe) {
			return QuickItem.of(craftingTableRecipe.getResult()).lore("&cClick to delete").make();
		}

		return null;
	}

	@Override
	protected void onClick(CustomRecipe recipe, GuiClickEvent event) {
		recipe.unStore(result -> event.manager.showGUI(event.player, new CustomRecipeListGUI(this.parent, this.player)));
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
