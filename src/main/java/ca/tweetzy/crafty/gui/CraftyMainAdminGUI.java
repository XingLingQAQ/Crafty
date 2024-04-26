package ca.tweetzy.crafty.gui;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.drops.block.BlockDropListGUI;
import ca.tweetzy.crafty.gui.drops.mob.MobDropListGUI;
import ca.tweetzy.crafty.gui.recipe.CustomRecipeListGUI;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;

public final class CraftyMainAdminGUI extends CraftyBaseGUI {

	public CraftyMainAdminGUI(@NonNull Player player) {
		super(null, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &fv&7" + Crafty.getInstance().getVersion(), 5);
		draw();
	}

	@Override
	protected void draw() {

		// border
		applyThemeBorder();

		setButton(2, 2, QuickItem
				.of(CompMaterial.GRASS_BLOCK)
				.name("<GRADIENT:3dcf50>&LBlock Drops</GRADIENT:26d5ed>")
				.lore("&7Used to edit block drops", "", "&e&lClick &8» &7to edit drops.")
				.make(), click -> click.manager.showGUI(click.player, new BlockDropListGUI(this, click.player)));

		// mob drop editor
		setButton(2, 4, QuickItem
				.of(CompMaterial.BLAZE_SPAWN_EGG)
				.name("<GRADIENT:3dcf50>&LMob Drops</GRADIENT:26d5ed>")
				.lore("&7Used to edit mob drops", "", "&e&lClick &8» &7to edit drops.")
				.make(), click -> click.manager.showGUI(click.player, new MobDropListGUI(this, click.player)));

		setButton(2, 6, QuickItem
				.of(CompMaterial.CRAFTING_TABLE)
				.name("<GRADIENT:3dcf50>&LCustom Recipes</GRADIENT:26d5ed>")
				.lore("&7Used to make custom recipes", "", "&e&lClick &8» &7to edit recipes.")
				.make(), click -> click.manager.showGUI(click.player, new CustomRecipeListGUI(this, click.player)));

		// Discord Button
		setButton(getRows() - 1, 3, QuickItem.of("http://textures.minecraft.net/texture/4d42337be0bdca2128097f1c5bb1109e5c633c17926af5fb6fc20000011aeb53")
				.name("<GRADIENT:3dcf50>&lDiscord</GRADIENT:26d5ed>")
				.lore(
						"&8Ask questions, Get support",
						"&7Need help with &eCrafty&7?, Join our",
						"&7Discord server to ask questions.",
						"",
						"&8» &e&ldiscord.tweetzy.ca"
				)
				.make(), click -> {

			click.gui.close();
			Common.tellNoPrefix(click.player,
					"&8&m-----------------------------------------------------",
					"",
					ChatUtil.centerMessage("&E&lCrafty Support"),
					ChatUtil.centerMessage("&bhttps://discord.tweetzy.ca"),
					"&8&m-----------------------------------------------------"
			);
		});


		// Patron
		setButton(getRows() - 1, 4, QuickItem.of(CompMaterial.DIAMOND)
				.name("<GRADIENT:3dcf50>&LPatreon</GRADIENT:26d5ed>")
				.lore(
						"&8Support me on Patreon",
						"&7By supporting me on Patreon you will",
						"&7be helping me be able to continue updating",
						"&7and creating free plugins.",
						"",
						"&e&lClick &8» &7To view Patreon"
				)
				.glow(true)
				.make(), click -> {

			click.gui.close();
			Common.tellNoPrefix(click.player,
					"&8&m-----------------------------------------------------",
					"",
					ChatUtil.centerMessage("&E&lTweetzy Patreon"),
					ChatUtil.centerMessage("&bhttps://patreon.tweetzy.ca"),
					"&8&m-----------------------------------------------------"
			);
		});

		// More Plugins Button
		setButton(getRows() - 1, 5, QuickItem.of("http://textures.minecraft.net/texture/b94ac36d9a6fbff1c558941381e4dcf595df825913f6c383ffaa71b756a875d3")
				.name("<GRADIENT:3dcf50>&LMore Plugins</GRADIENT:26d5ed>")
				.lore(
						"&8View more of my plugins",
						"&7Like &eCrafty&7? Take a look at my other",
						"&7plugins, you might like them.",
						"",
						"&e&lClick &8» &7To view Plugins"
				)
				.make(), click -> click.manager.showGUI(click.player, new PluginListGUI(player)));
	}
}
