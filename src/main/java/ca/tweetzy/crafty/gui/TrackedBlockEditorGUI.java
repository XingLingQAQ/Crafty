package ca.tweetzy.crafty.gui;

import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class TrackedBlockEditorGUI extends CraftyPagedGUI<Drop> {

	private final TrackedBlock trackedBlock;

	public TrackedBlockEditorGUI(Gui parent, @NonNull Player player, @NonNull final TrackedBlock trackedBlock) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eEditing Block", 6, trackedBlock.getDrops());
		this.trackedBlock = trackedBlock;
		draw();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setItem(0, 4, QuickItem.of(this.trackedBlock.getBlock()).make());

		drawDefaultDrops();
		drawBlockedWorlds();
	}

	private void drawDefaultDrops() {
		// default drops
		setButton(getRows() - 1, 2, QuickItem
				.of(this.trackedBlock.getOptions().dropDefaultItems() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE)
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
	protected ItemStack makeDisplayItem(Drop object) {
		return null;
	}

	@Override
	protected void onClick(Drop object, GuiClickEvent clickEvent) {

	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
