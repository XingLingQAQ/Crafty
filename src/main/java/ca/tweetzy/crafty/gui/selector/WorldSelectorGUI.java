package ca.tweetzy.crafty.gui.selector;

import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class WorldSelectorGUI extends CraftyPagedGUI<World> {

	private final Consumer<World> selected;

	public WorldSelectorGUI(Gui parent, @NonNull Player player, final Consumer<World> selected) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eSelect World", 6, Bukkit.getWorlds());
		this.selected = selected;
		draw();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();

		this.items.sort(Comparator.comparing(World::getName));
	}

	@Override
	protected ItemStack makeDisplayItem(World world) {
		return QuickItem.of("https://textures.minecraft.net/texture/25485031b37f0d8a4f3b7816eb717f03de89a87f6a40602aef52221cdfaf7488")
				.name("<GRADIENT:3dcf50>&l" + ChatUtil.capitalizeFully(world.getName()) + "</GRADIENT:26d5ed>")
				.lore(
						"&e&lClick &8» &7To select world"
				).make();
	}

	@Override
	protected void onClick(World world, GuiClickEvent click) {
		selected.accept(world);
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
