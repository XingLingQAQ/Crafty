package ca.tweetzy.crafty.gui.recipe;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.CraftyMainAdminGUI;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.crafty.impl.recipe.CraftyCookingRecipe;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class CookingRecipeGUI extends CraftyBaseGUI {
	private final Gui parent;
	private final CraftyCookingRecipe recipe;
	private final boolean isEditing;

	public CookingRecipeGUI(Gui parent, @NonNull Player player, CraftyCookingRecipe recipe, boolean isEditing) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &e" + ChatUtil.capitalizeFully(recipe.getRecipeType()) + " Recipe", 6);
		this.parent = parent;
		this.recipe = recipe;
		this.isEditing = isEditing;

		setAcceptsItems(true);
		setUnlocked(2, 2);
		setUnlocked(2, 6);

		draw();
	}

	@Override
	protected void draw() {
		applyThemeBorder();

		setItem(2, 2, this.recipe.getInput() != null ? this.recipe.getInput() : QuickItem.of(CompMaterial.AIR).make());
		setItem(2, 6, this.recipe.getResult() != null ? this.recipe.getResult() : QuickItem.of(CompMaterial.AIR).make());

		drawNameButton();
		drawCookTimeButton();
		drawXPButton();
		drawCreateButton();

		applyBackExit();
	}

	private void drawNameButton() {
		setButton(3, 4, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:3dcf50>&lRecipe Name</GRADIENT:26d5ed>")
				.lore(
						"&8The key identifier for the recipe",
						"&7This has to be unique regardless of",
						"&7the recipe type (ie. crafting, furnace, etc)",
						"",
						"&7Current Name&F: &e" + this.recipe.getId().toLowerCase(),
						"",
						this.isEditing ? "&cCannot update name once created!" : "&e&lClick &8» &7To change name"
				)
				.make(), click -> {

			if (this.isEditing) return;

			updateRecipeObject();

			new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lRecipe Name</GRADIENT:26d5ed>", "&7Enter the &erecipe name &7into chat.") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, CookingRecipeGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());

					if (Crafty.getRecipeManager().get(string) != null) {
						Common.tell(click.player, "&cThe recipe id&F: &4" + string + " &cis already in use!");
						return false;
					}

					CookingRecipeGUI.this.recipe.setId(string);
					click.manager.showGUI(click.player, new CookingRecipeGUI(CookingRecipeGUI.this.parent, click.player, CookingRecipeGUI.this.recipe, CookingRecipeGUI.this.isEditing));
					return true;
				}
			};
		});
	}

	private void drawXPButton() {
		setButton(getRows() - 1, 2, QuickItem
				.of(CompMaterial.EXPERIENCE_BOTTLE)
				.name("<GRADIENT:3dcf50>&lExperience</GRADIENT:26d5ed>")
				.lore(
						"&8How much experience for recipe",
						"&7This value is how much exp should be",
						"&7given to the player when it's made",
						"",
						"&7Current Exp&F: &e" + this.recipe.getExperience(),
						"",
						"&e&lClick &8» &7To change exp"
				)
				.make(), click -> {


			updateRecipeObject();

			new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lRecipe Experience</GRADIENT:26d5ed>", "&7Enter the &eexperience amount &7into chat.") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, CookingRecipeGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());
					if (!MathUtil.isDouble(string)) {
						Common.tell(player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
						return false;
					}

					final double value = Double.parseDouble(string);
					// NaN check
					if (Double.isNaN(value)) {
						Common.tell(player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
						return false;
					}


					CookingRecipeGUI.this.recipe.setExperience((float) value);
					click.manager.showGUI(click.player, new CookingRecipeGUI(CookingRecipeGUI.this.parent, click.player, CookingRecipeGUI.this.recipe, CookingRecipeGUI.this.isEditing));
					return true;
				}
			};
		});
	}

	private void drawCookTimeButton() {
		final double cookTimeSec = (double) this.recipe.getCookingTime() / 20;

		setButton(getRows() - 1, 6, QuickItem
				.of(CompMaterial.CLOCK)
				.name("<GRADIENT:3dcf50>&lCook Time</GRADIENT:26d5ed>")
				.lore(
						"&8How long should it take?",
						"&7This value is how for how long it should",
						"&7take the recipe to cook/smelt",
						"",
						"&7Current Time&F: &e" + this.recipe.getCookingTime() + " ticks &F(&e" + cookTimeSec + "s&f)",
						"",
						"&e&lClick &8» &7To change time"
				)
				.make(), click -> {


			updateRecipeObject();

			new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lRecipe Time</GRADIENT:26d5ed>", "&7Enter the &ecook time &7into chat.") {

				@Override
				public void onExit(Player player) {
					click.manager.showGUI(click.player, CookingRecipeGUI.this);
				}

				@Override
				public boolean onResult(String string) {
					string = ChatColor.stripColor(string.toLowerCase());
					if (!MathUtil.isInt(string)) {
						Common.tell(player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
						return false;
					}

					final int value = Integer.parseInt(string);

					CookingRecipeGUI.this.recipe.setCookingTime(value);
					click.manager.showGUI(click.player, new CookingRecipeGUI(CookingRecipeGUI.this.parent, click.player, CookingRecipeGUI.this.recipe, CookingRecipeGUI.this.isEditing));
					return true;
				}
			};
		});
	}

	private void drawCreateButton() {
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name(this.isEditing ? "<GRADIENT:3dcf50>&lSave</GRADIENT:26d5ed>" : "<GRADIENT:3dcf50>&lCreate</GRADIENT:26d5ed>")
				.lore("&e&lClick &8» &7to " + (this.isEditing ? "save" : "create") + " recipe")
				.make(), click -> {

			updateRecipeObject();

			if (!this.isEditing) {
				if ((recipe.getResult() == null || recipe.getResult().getType() == CompMaterial.AIR.parseMaterial()) || (recipe.getInput() == null || recipe.getInput().getType() == CompMaterial.AIR.parseMaterial())) {
					Common.tell(click.player, "&cPlease provide a input and result item!");
					return;
				}

				if (Crafty.getRecipeManager().get(this.recipe.getId()) != null) {
					Common.tell(click.player, "&cThe current recipe id specified already in use!");
					return;
				}
			}

			if (this.isEditing) {
				this.recipe.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						click.manager.showGUI(click.player, new CustomRecipeListGUI(new CraftyMainAdminGUI(click.player), click.player));
				});
				return;
			}

			Crafty.getRecipeManager().addRecipe(recipe, created -> {
				if (created)
					click.manager.showGUI(click.player, new CustomRecipeListGUI(new CraftyMainAdminGUI(click.player), click.player));
			});
		});
	}

	private void updateRecipeObject() {
		this.recipe.setInput(getItem(2, 2));
		this.recipe.setResult(getItem(2, 6));
	}
}
