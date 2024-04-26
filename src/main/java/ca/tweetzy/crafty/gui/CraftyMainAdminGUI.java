package ca.tweetzy.crafty.gui;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.recipe.CustomRecipeListGUI;
import ca.tweetzy.crafty.gui.drops.block.BlockDropListGUI;
import ca.tweetzy.crafty.gui.drops.mob.MobDropListGUI;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.crafty.model.PremiumStatus;
import ca.tweetzy.flight.comp.enums.CompMaterial;
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
	}
}
