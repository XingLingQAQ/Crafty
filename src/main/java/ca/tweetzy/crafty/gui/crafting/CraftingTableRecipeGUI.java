package ca.tweetzy.crafty.gui.crafting;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.CraftyMainAdminGUI;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.crafty.model.StringUtil;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;

public final class CraftingTableRecipeGUI extends CraftyBaseGUI {

	private final Gui parent;
	private final List<Integer> CRAFTING_SLOTS = List.of(10, 11, 12, 19, 20, 21, 28, 29, 30);
	private final CraftingTableRecipe recipe;

	public CraftingTableRecipeGUI(Gui parent, @NonNull Player player, CraftingTableRecipe craftingTableRecipe) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eCrafting Recipe", 5);
		this.parent = parent;
		this.recipe = craftingTableRecipe;
		setAcceptsItems(true);
		CRAFTING_SLOTS.forEach(this::setUnlocked);
		setUnlocked(2, 6);

		draw();
	}

	@Override
	protected void draw() {
		applyThemeBorder();
		applyBackExit();
		// middle stripe
		setItem(1, 4, QuickItem.of(QuickItem.bg(CompMaterial.CYAN_STAINED_GLASS_PANE.parseItem())).glow(true).make());
		setItem(2, 4, QuickItem.of(QuickItem.bg(CompMaterial.LIME_STAINED_GLASS_PANE.parseItem())).glow(true).make());
		setItem(3, 4, QuickItem.of(QuickItem.bg(CompMaterial.CYAN_STAINED_GLASS_PANE.parseItem())).glow(true).make());

		// clear out result slot
		setItem(2, 6, QuickItem.of(CompMaterial.AIR).make());

		// clear out crafting grid shape
		CRAFTING_SLOTS.forEach(slot -> setItem(slot, QuickItem.of(CompMaterial.AIR).make()));

		// re-fill
		if (!this.recipe.getMapping().isEmpty()) {
			for (int i = 0; i < 9; i++) {

				Optional<ItemStack> item = StringUtil.findKeyByValue(this.recipe.getMapping(), this.recipe.getFormat().charAt(i));

				if (item.isPresent())
					setItem(CRAFTING_SLOTS.get(i), item.get());
			}
		}

		if (this.recipe.getResult() != null && this.recipe.getResult().getType() != CompMaterial.AIR.parseMaterial())
			setItem(2, 6, this.recipe.getResult());

		drawNameButton();
		drawShapelessButton();
		drawCreateButton();
	}

	private void drawCreateButton() {
		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.LIME_DYE).name("&e&lCREATE").make(), click -> {
			updateRecipeObject();

			if (isGridEmpty()) {
				Common.tell(click.player, "&cPlease provide at least 1 item in crafting grid.");
				return;
			}

			if (recipe.getResult() == null || recipe.getResult().getType() == CompMaterial.AIR.parseMaterial()) return;

			if (Crafty.getRecipeManager().get(this.recipe.getId()) != null) {
				Common.tell(click.player, "&cPlease update the recipe name, the current one is in use.");
				return;
			}

			Crafty.getRecipeManager().addRecipe(recipe, created -> {
				if (created)
					click.manager.showGUI(click.player, new CustomRecipeListGUI(new CraftyMainAdminGUI(click.player), click.player));
			});
		});
	}

	private void updateRecipeObject() {
		final HashMap<HashMap<ItemStack, Character>, String> formatString = generateMappingFormat();
		this.recipe.setFormat(formatString.values().stream().findFirst().get());
		this.recipe.setMapping(formatString.entrySet().stream().findFirst().get().getKey());
		this.recipe.setResult(getItem(2, 6));
	}

	private boolean isGridEmpty() {
		boolean empty = true;

		for (int slot : CRAFTING_SLOTS){
			final ItemStack slotItem = getItem(slot);
			if (slotItem != null && slotItem.getType() != CompMaterial.AIR.parseMaterial()) {
				empty = false;
				break;
			}
		}

		return empty;
	}

	private HashMap<HashMap<ItemStack, Character>, String> generateMappingFormat() {
		HashMap<ItemStack, Character> CHAR_MAPPING = new HashMap<>();
		StringBuilder format = new StringBuilder();

		char currentChar = 'A';
		for (int craftingSlot : this.CRAFTING_SLOTS) {
			final ItemStack slotItem = getItem(craftingSlot);

			if (slotItem != null && slotItem.getType() != CompMaterial.AIR.parseMaterial()) {

				if (CHAR_MAPPING.containsKey(slotItem)) {
					format.append(CHAR_MAPPING.get(slotItem));
				} else {
					CHAR_MAPPING.put(slotItem, ++currentChar);
					format.append(currentChar);
				}


			} else {
				format.append(" ");
			}
		}

		final HashMap<HashMap<ItemStack, Character>, String> map = new HashMap<>();
		map.put(CHAR_MAPPING, format.toString());

		return map;
	}

	private void drawShapelessButton() {
		setButton(getRows() - 1, 2, QuickItem
				.of(CompMaterial.PRISMARINE_SHARD)
				.name("<GRADIENT:3dcf50>&lShapeless</GRADIENT:26d5ed>")
				.lore(
						"&8Should this be a shapeless recipe",
						"&7A shapeless recipe is a just a recipe",
						"&7where the items can be placed in any position.",
						"",
						"&7Shapeless Mode&F: " + (this.recipe.isShapeless() ? "&eTrue" : "&cFalse"),
						"",
						"&e&lClick &8» &7To toggle shapeless mode"
				).make(), click -> {


			this.recipe.setShapeless(!this.recipe.isShapeless());
			drawShapelessButton();
		});
	}


	private void drawNameButton() {
		setButton(getRows() - 1, 6, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:3dcf50>&lRecipe Name</GRADIENT:26d5ed>")
				.lore(
						"&8The key identifier for the item",
						"&7This has to be unique regardless of",
						"&7the recipe type (ie. crafting, furnace, etc)",
						"",
						"&7Current Name&F: &e" + this.recipe.getId().toLowerCase(),
						"",
						"&e&lClick &8» &7To change name"
				)
				.make(), click -> {


			updateRecipeObject();

			new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lRecipe Name</GRADIENT:26d5ed>", "&7Enter the &erecipe name &7into chat.") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, CraftingTableRecipeGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());

					if (Crafty.getRecipeManager().get(string)!= null) {
						Common.tell(click.player, "&cThe recipe id&F: &4" + string + " &cis already in use!");
						return false;
					}

					CraftingTableRecipeGUI.this.recipe.setId(string);
					click.manager.showGUI(click.player, new CraftingTableRecipeGUI(CraftingTableRecipeGUI.this.parent, click.player, CraftingTableRecipeGUI.this.recipe));
					return true;
				}
			};
		});
	}

	private void drawUseStackCounts() {
		setButton(getRows() - 1, 7, QuickItem.of(CompMaterial.LEVER).make(), click -> {

		});
	}
}
