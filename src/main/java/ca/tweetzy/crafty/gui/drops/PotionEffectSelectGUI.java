package ca.tweetzy.crafty.gui.drops;

import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.QuickItem;
import lombok.NonNull;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class PotionEffectSelectGUI extends CraftyPagedGUI<PotionEffectType> {

	private final Gui parent;
	private final Drop drop;

	public PotionEffectSelectGUI(Gui parent, @NonNull Player player, @NonNull final Drop drop) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eToggle Potion Effects", 6, StreamSupport.stream(Spliterators.spliteratorUnknownSize(Registry.EFFECT.iterator(), Spliterator.ORDERED), false).collect(Collectors.toList()));
		this.parent = parent;
		this.drop = drop;
		draw();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
	}

	@Override
	protected ItemStack makeDisplayItem(PotionEffectType effect) {

		final QuickItem item = QuickItem.of(CompMaterial.POTION).name("<GRADIENT:3dcf50>&L" + ChatUtil.capitalizeFully(effect.getKey().getKey()) + "</GRADIENT:26d5ed>").hideTags(true);

		if (this.drop.getCondition().getRequiredPotionEffects().contains(effect))
			item.lore("&ePotion Effect Selected");

		item.lore("");
		if (this.drop.getCondition().getRequiredPotionEffects().contains(effect))
			item.lore("&e&lClick &8» &7To remove effect");
		else
			item.lore("&e&lClick &8» &7To add effect");

		return item.make();
	}

	@Override
	protected void onClick(PotionEffectType effect, GuiClickEvent event) {
		if (!this.drop.getCondition().getRequiredPotionEffects().contains(effect))
			this.drop.getCondition().getRequiredPotionEffects().add(effect);
		else
			this.drop.getCondition().getRequiredPotionEffects().remove(effect);

		this.drop.sync(null);
		draw();
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
