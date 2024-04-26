package ca.tweetzy.crafty.gui.recipe;

import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class RecipeTypeSelectGUI extends CraftyPagedGUI<RecipeType> {

	private final Consumer<RecipeType> selected;

	public RecipeTypeSelectGUI(Gui parent, @NonNull Player player, final Consumer<RecipeType> selected) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eSelect Recipe Type", 6, new ArrayList<>(List.of(RecipeType.values())));
		this.selected = selected;
		draw();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
		this.items.sort(Comparator.comparing(RecipeType::name));
	}

	@Override
	protected ItemStack makeDisplayItem(RecipeType recipeType) {
		return QuickItem.of(switch (recipeType) {
					case CRAFTING -> CompMaterial.CRAFTING_TABLE;
					case FURNACE -> CompMaterial.FURNACE;
					case BLAST_FURNACE -> CompMaterial.BLAST_FURNACE;
					case CAMPFIRE -> CompMaterial.CAMPFIRE;
					case SMOKER -> CompMaterial.SMOKER;
				})
				.name("<GRADIENT:3dcf50>&l" + ChatUtil.capitalizeFully(recipeType.name()) + "</GRADIENT:26d5ed>")
				.lore(
						"&e&lClick &8» &7To select recipe type"
				).make();
	}

	@Override
	protected void onClick(RecipeType recipeType, GuiClickEvent click) {
		selected.accept(recipeType);
	}

	@Override
	protected List<Integer> fillSlots() {
		return List.of(20, 21, 22, 23, 24, 31);
	}
}
