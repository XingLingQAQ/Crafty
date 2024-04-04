package ca.tweetzy.crafty.gui.drops.block;

import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.drops.BlockedWorldsListGUI;
import ca.tweetzy.crafty.gui.drops.DropEditorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.crafty.impl.BlockDrop;
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

public final class TrackedBlockEditorGUI extends CraftyPagedGUI<BlockDrop> {

	private final TrackedBlock trackedBlock;

	public TrackedBlockEditorGUI(Gui parent, @NonNull Player player, @NonNull final TrackedBlock trackedBlock) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eEditing Block", 6, trackedBlock.getDrops());
		this.trackedBlock = trackedBlock;
		draw();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
		this.items = this.trackedBlock.getDrops();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setItem(0, 4, QuickItem.of(this.trackedBlock.getBlock()).make());

		drawDefaultDrops();
		drawAddButton();
		drawBlockedWorlds();
	}

	private void drawDefaultDrops() {
		// default drops
		setButton(getRows() - 1, 2, QuickItem
				.of(this.trackedBlock.getOptions().dropDefaultItems() ? CompMaterial.SLIME_BALL : CompMaterial.FIRE_CHARGE)
				.name("<GRADIENT:3dcf50>&lDefault Drops</GRADIENT:26d5ed>")
				.lore(
						"&8Used to toggle default drops",
						"&7Default drops means the normal vanilla",
						"&7drops you would expect",
						"",
						"&7Default Drops&f: " + (this.trackedBlock.getOptions().dropDefaultItems() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle default drops."
				)
				.make(), click -> {

			this.trackedBlock.getOptions().setDropDefaultItems(!this.trackedBlock.getOptions().dropDefaultItems());
			this.trackedBlock.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					drawDefaultDrops();
			});
		});
	}

	private void drawAddButton() {
		// new drop button
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lNew Drop</GRADIENT:26d5ed>")
				.lore(
						"&8Used to add more drops",
						"&7Opens a menu to add a new",
						"&7drop to this block.",
						"",
						"&e&lClick &8» &7to add drop"
				)
				.make(), click -> click.manager.showGUI(click.player, new DropEditorGUI(this, click.player, BlockDrop.empty(this.trackedBlock.getBlock()), this.trackedBlock, DropEditorGUI.DropEditorMode.ADD)));
	}

	private void drawBlockedWorlds() {
		setButton(getRows() - 1, 6, QuickItem
				.of("https://textures.minecraft.net/texture/25485031b37f0d8a4f3b7816eb717f03de89a87f6a40602aef52221cdfaf7488")
				.name("<GRADIENT:3dcf50>&lBlocked Worlds</GRADIENT:26d5ed>")
				.lore(
						"&8Used to block in worlds",
						"&7By adding worlds to the blocked list",
						"&7Crafty will not override any settings.",
						"",
						"&e&lClick &8» &7to edit blocked worlds"
				).make(), click -> click.manager.showGUI(click.player, new BlockedWorldsListGUI(this, click.player, this.trackedBlock)));
	}

	@Override
	protected ItemStack makeDisplayItem(BlockDrop drop) {
		return QuickItem
				.of(drop.getItem())
				.lore(
						"&8&m------------------------------",
						"",
						"&7Drop Chance&F: &a" + drop.getChance() + "&F%",
						"",
						"&e&lLeft Click &8» &7to edit drop",
						"&4&lRight Click &8» &cto delete drop",
						"&8&m------------------------------"
				)
				.make();
	}

	@Override
	protected void onClick(BlockDrop drop, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT)
			click.manager.showGUI(click.player, new DropEditorGUI(this, click.player, drop, this.trackedBlock, DropEditorGUI.DropEditorMode.EDIT));

		if (click.clickType == ClickType.RIGHT) {
			drop.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					this.trackedBlock.getDrops().remove(drop);

				click.manager.showGUI(click.player, new TrackedBlockEditorGUI(this.parent, click.player, this.trackedBlock));
			});

		}

	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
