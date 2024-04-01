package ca.tweetzy.crafty.gui.drops.mob;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.ValidMob;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.drops.block.TrackedBlockEditorGUI;
import ca.tweetzy.crafty.gui.selector.MobSelectorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class MobDropListGUI extends CraftyPagedGUI<TrackedMob> {

	public MobDropListGUI(Gui parent, @NonNull Player player) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> Mob Drops", 6, Crafty.getMobDropManager().getValues());
		draw();
	}

	@Override
	protected void drawFixed() {
		applyThemeBorder();

		// add button
		setButton(5, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lNew Mob</GRADIENT:26d5ed>")
				.lore(
						"&8Used to edit a new entity",
						"&7Opens the entity selector to pick a",
						"&7mob to track/edit",
						"",
						"&e&lClick &8» &7to open mob picker"
				)
				.make(), click -> click.manager.showGUI(click.player, new MobSelectorGUI(this, click.player, selected -> {

			if (Crafty.getMobDropManager().isTracked(selected.getEntityType())) return;
			Crafty.getMobDropManager().trackMob(selected.getEntityType(), created -> click.manager.showGUI(click.player, new MobDropListGUI(this.parent, click.player)));
		})));

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(TrackedMob mob) {
		return QuickItem
				.of(ValidMob.valueOf(mob.getEntity().name()).getHeadTexture())
				.name("<GRADIENT:3dcf50>" + ChatUtil.capitalizeFully(mob.getEntity()) + "</GRADIENT:26d5ed>")
				.lore(
						"&7This mob is currently tracked",
						"",
						"&e&lLeft Click &8» &7to edit mob",
						"&c&lRight Click &8» &cto delete mob"
				)
				.make();
	}

	@Override
	protected void onClick(TrackedMob trackedMob, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT)
			click.manager.showGUI(click.player, new TrackedMobEditorGUI(this, click.player, trackedMob));

		if (click.clickType == ClickType.RIGHT)
			trackedMob.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					click.manager.showGUI(click.player, new MobDropListGUI(this.parent, click.player));
			});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
