package ca.tweetzy.crafty.gui.selector;

import ca.tweetzy.crafty.api.ValidMob;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public final class MobSelectorGUI extends CraftyPagedGUI<ValidMob> {

	private final Consumer<ValidMob> selected;

	public MobSelectorGUI(Gui parent, @NonNull Player player, final Consumer<ValidMob> selected) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eSelect Mob", 6, List.of(ValidMob.values()));
		this.selected = selected;
	}

	@Override
	protected ItemStack makeDisplayItem(ValidMob mob) {
		return null;
	}

	@Override
	protected void onClick(ValidMob mob, GuiClickEvent click) {
		selected.accept(mob);
	}
}
