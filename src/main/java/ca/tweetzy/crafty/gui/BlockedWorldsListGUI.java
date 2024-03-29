package ca.tweetzy.crafty.gui;

import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.drop.TrackedOptions;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.selector.WorldSelectorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public final class BlockedWorldsListGUI extends CraftyPagedGUI<String> {

	private TrackedBlock trackedBlock;

	public BlockedWorldsListGUI(Gui parent, @NonNull Player player, final TrackedBlock trackedBlock) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eBlocked Worlds", 6, trackedBlock.getOptions().getBlockedWorlds().stream().filter(world -> !world.isBlank()).collect(Collectors.toList()));
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

		// add button
		setButton(5, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lAdd World</GRADIENT:26d5ed>")
				.lore(
						"&8Used to block world",
						"&7Opens the world selector to pick a",
						"&7world to add to blocked list.",
						"",
						"&e&lClick &8» &7to open world picker"
				)
				.make(), click -> click.manager.showGUI(click.player, new WorldSelectorGUI(this, click.player, selected -> {

			if (this.trackedBlock.getOptions() != null && !this.trackedBlock.getOptions().getBlockedWorlds().contains(selected.getName())) {
				this.trackedBlock.getOptions().getBlockedWorlds().add(selected.getName());

				this.trackedBlock.sync(result -> {
					if (result == SynchronizeResult.SUCCESS)
						click.manager.showGUI(click.player, new BlockedWorldsListGUI(this.parent, click.player, this.trackedBlock));
				});
			}
		})));
	}

	@Override
	protected ItemStack makeDisplayItem(String world) {
		return QuickItem.of("https://textures.minecraft.net/texture/25485031b37f0d8a4f3b7816eb717f03de89a87f6a40602aef52221cdfaf7488")
				.name("<GRADIENT:3dcf50>&l" + ChatUtil.capitalizeFully(world) + "</GRADIENT:26d5ed>")
				.lore(
						"&8This world is blocked",
						"&7Since this world is blocked, no",
						"&7overrides/drops will be active.",
						"",
						"&e&lClick &8» &cTo delete world"
				).make();
	}

	@Override
	protected void onClick(String world, GuiClickEvent click) {
		if (this.trackedBlock != null) {
			this.trackedBlock.getOptions().getBlockedWorlds().remove(world);
			this.trackedBlock.sync(result -> {
				if (result == SynchronizeResult.SUCCESS)
					click.manager.showGUI(click.player, new BlockedWorldsListGUI(this.parent, click.player, this.trackedBlock));
			});
		}
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
