package ca.tweetzy.crafty.gui.selector;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.ValidMob;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class MobSelectorGUI extends CraftyPagedGUI<ValidMob> {

	private final Consumer<ValidMob> selected;

	public MobSelectorGUI(Gui parent, @NonNull Player player, final Consumer<ValidMob> selected) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eSelect Mob", 6, new ArrayList<>(List.of(ValidMob.values())));
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

		this.items.sort(Comparator.comparing(mob -> ChatUtil.capitalizeFully(mob.getEntityType())));
	}

	@Override
	protected ItemStack makeDisplayItem(ValidMob mob) {
		final QuickItem quickItem = QuickItem.of(mob.getHeadTexture()).name(ChatUtil.capitalizeFully(mob.name()));

		if (Crafty.getMobDropManager().isTracked(mob.getEntityType()))
			quickItem.lore("&c&lAlready Tracked");
		else
			quickItem.lore("&e&lClick &8» &7To select this mob");

		return quickItem.make();
	}

	@Override
	protected void onClick(ValidMob mob, GuiClickEvent click) {
		selected.accept(mob);
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
