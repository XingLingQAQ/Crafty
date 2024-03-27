package ca.tweetzy.crafty.settings;

import ca.tweetzy.flight.settings.TranslationEntry;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.crafty.Crafty;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class Translations extends TranslationManager {

	public Translations(@NonNull JavaPlugin plugin) {
		super(plugin);
		this.mainLanguage = Settings.LANGUAGE.getString();
	}

	public static TranslationEntry GUI_SHARED_ITEMS_BACK_BUTTON_NAME = create("gui.shared buttons.back button.name", "<GRADIENT:65B1B4>&LGo Back</GRADIENT:2B6F8A>");
	public static TranslationEntry GUI_SHARED_ITEMS_BACK_BUTTON_LORE = create("gui.shared buttons.back button.lore",
			"&e&l%left_click% &7to go back"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_EXIT_BUTTON_NAME = create("gui.shared buttons.exit button.name", "<GRADIENT:65B1B4>&LExit</GRADIENT:2B6F8A>");
	public static TranslationEntry GUI_SHARED_ITEMS_EXIT_BUTTON_LORE = create("gui.shared buttons.exit button.lore",
			"&e&l%left_click% &7to exit menu"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON_NAME = create("gui.shared buttons.previous button.name", "<GRADIENT:65B1B4>&lPrevious Page</GRADIENT:2B6F8A>");
	public static TranslationEntry GUI_SHARED_ITEMS_PREVIOUS_BUTTON_LORE = create("gui.shared buttons.previous button.lore",
			"&e&l%left_click% &7to go back a page"
	);

	public static TranslationEntry GUI_SHARED_ITEMS_NEXT_BUTTON_NAME = create("gui.shared buttons.next button.name", "<GRADIENT:65B1B4>&lNext Page</GRADIENT:2B6F8A>");
	public static TranslationEntry GUI_SHARED_ITEMS_NEXT_BUTTON_LORE = create("gui.shared buttons.next button.lore",
			"&e&l%left_click% &7to go to next page"
	);

	public static void init() {
		new Translations(Crafty.getInstance()).setup();
	}
}
