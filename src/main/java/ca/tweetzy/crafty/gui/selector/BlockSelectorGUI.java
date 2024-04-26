package ca.tweetzy.crafty.gui.selector;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.gui.helper.InventorySafeMaterials;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class BlockSelectorGUI extends CraftyPagedGUI<CompMaterial> {

	private final Consumer<CompMaterial> selected;

	public BlockSelectorGUI(Gui parent, @NonNull Player player, final Consumer<CompMaterial> selected) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eSelect Block", 6, InventorySafeMaterials.get().stream().filter(material -> material.parseMaterial().isBlock()).collect(Collectors.toList()));
		this.selected = selected;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.HOPPER)
				.name("<GRADIENT:3dcf50>&LQuick Pick</GRADIENT:26d5ed>")
				.lore("&8Used to 'pick' item faster", "&eDrag and drop item here from", "&eyour inventory to use it.")
				.make(), click -> {

			final ItemStack cursor = click.cursor;
			if (cursor != null && cursor.getType() != CompMaterial.AIR.parseMaterial()) {
				final ItemStack newIcon = cursor.clone();
				newIcon.setAmount(1);

				if (!newIcon.getType().isBlock()) {
					Common.tell(click.player, "&cThat item isn't a valid block!");
					return;
				}

				selected.accept(CompMaterial.matchCompMaterial(newIcon));
			}
		});
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();

		this.items.sort(Comparator.comparing(mat -> ChatUtil.capitalizeFully(mat.parseMaterial())));
	}

	@Override
	protected ItemStack makeDisplayItem(CompMaterial block) {
		final QuickItem quickItem = QuickItem.of(block);

		if (Crafty.getBlockDropManager().isTracked(block))
			quickItem.lore("&c&lAlready Tracked");
		else
			quickItem.lore("&e&lClick &8» &7To select this block");

		return quickItem.make();
	}

	@Override
	protected void onClick(CompMaterial block, GuiClickEvent click) {
		// todo check if they already have that block added

		selected.accept(block);
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
