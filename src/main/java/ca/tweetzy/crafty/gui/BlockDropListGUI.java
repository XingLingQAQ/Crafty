package ca.tweetzy.crafty.gui;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.selector.BlockSelectorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class BlockDropListGUI extends CraftyPagedGUI<TrackedBlock> {

	public BlockDropListGUI(Gui parent, @NonNull Player player) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> Block Drops", 6, Crafty.getBlockDropManager().getValues());
		draw();
	}

	@Override
	protected void drawFixed() {
		applyThemeBorder();

		// add button
		setButton(5, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lNew Block</GRADIENT:26d5ed>")
				.lore(
						"&8Used to edit a new block",
						"&7Opens the block selector to pick a",
						"&7block to track/edit",
						"",
						"&e&lClick &8» &7to open block picker"
				)
				.make(), click -> click.manager.showGUI(click.player, new BlockSelectorGUI(this, click.player, selected -> {

			if (Crafty.getBlockDropManager().isTracked(selected)) return;
			Crafty.getBlockDropManager().trackBlock(selected, created -> click.manager.showGUI(click.player, new BlockDropListGUI(this.parent, click.player)));
		})));

		applyBackExit();
	}

	@Override
	protected ItemStack makeDisplayItem(TrackedBlock object) {
		return QuickItem
				.of(object.getBlock())
				.lore(
						"&7This block is currently tracked",
						"",
						"&e&lLeft Click &8» &7to edit block",
						"&c&lRight Click &8» &cto delete block"
				)
				.make();
	}

	@Override
	protected void onClick(TrackedBlock trackedBlock, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT)
			click.manager.showGUI(click.player, new TrackedBlockEditorGUI(this, click.player, trackedBlock));

		if (click.clickType == ClickType.RIGHT)
			trackedBlock.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					click.manager.showGUI(click.player, new BlockDropListGUI(this.parent, click.player));
			});
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
