package ca.tweetzy.crafty.gui.template;

import ca.tweetzy.crafty.settings.Settings;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.gui.template.BaseGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CraftyBaseGUI extends BaseGUI {

	protected final Player player;

	public CraftyBaseGUI(Gui parent, @NonNull final Player player, @NonNull String title, int rows) {
		super(parent, title, rows);
		this.player = player;
	}

	public CraftyBaseGUI(Gui parent, @NonNull final Player player, @NonNull String title) {
		super(parent, title);
		this.player = player;
	}

	public CraftyBaseGUI(@NonNull final Player player, @NonNull String title) {
		super(title);
		this.player = player;
	}

	@Override
	protected ItemStack getBackButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_BACK_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_BACK_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_BACK_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getExitButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_EXIT_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_EXIT_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_EXIT_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getPreviousButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_PREVIOUS_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_PREVIOUS_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_PREVIOUS_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected ItemStack getNextButton() {
		return QuickItem
				.of(Settings.GUI_SHARED_ITEMS_NEXT_BUTTON.getItemStack())
				.name(TranslationManager.string(this.player, Translations.GUI_SHARED_ITEMS_NEXT_BUTTON_NAME))
				.lore(TranslationManager.list(this.player, Translations.GUI_SHARED_ITEMS_NEXT_BUTTON_LORE, "left_click", TranslationManager.string(this.player, Translations.MOUSE_LEFT_CLICK)))
				.make();
	}

	@Override
	protected int getPreviousButtonSlot() {
		return 48;
	}

	@Override
	protected int getNextButtonSlot() {
		return 50;
	}

	protected void applyThemeBorder() {
		InventoryBorder.getBorders(getRows()).forEach(slot -> setItem(slot, QuickItem.of(QuickItem.bg(slot % 2 == 0 ? CompMaterial.LIME_STAINED_GLASS_PANE.parseItem() : CompMaterial.CYAN_STAINED_GLASS_PANE.parseItem())).glow(true).make()));
	}
}
