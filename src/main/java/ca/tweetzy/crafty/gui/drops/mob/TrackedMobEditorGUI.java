package ca.tweetzy.crafty.gui.drops.mob;

import ca.tweetzy.crafty.api.ValidMob;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.drops.BlockedWorldsListGUI;
import ca.tweetzy.crafty.gui.drops.DropEditorGUI;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.crafty.impl.MobDrop;
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

public final class TrackedMobEditorGUI extends CraftyPagedGUI<MobDrop> {

	private final TrackedMob trackedMob;

	public TrackedMobEditorGUI(Gui parent, @NonNull Player player, @NonNull final TrackedMob trackedMob) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eEditing Mob", 6, trackedMob.getDrops());
		this.trackedMob = trackedMob;
		draw();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
		this.items = this.trackedMob.getDrops();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		setItem(0, 4, QuickItem.of(Enum.valueOf(ValidMob.class, trackedMob.getEntity().name()).getHeadTexture()).name(ChatUtil.capitalizeFully(trackedMob.getEntity())).make());

		drawDefaultDrops();
		drawAddButton();
		drawBlockedWorlds();
	}

	private void drawDefaultDrops() {
		// default drops
		setButton(getRows() - 1, 2, QuickItem
				.of(this.trackedMob.getOptions().dropDefaultItems() ? CompMaterial.SLIME_BALL : CompMaterial.FIRE_CHARGE)
				.name("<GRADIENT:3dcf50>&lDefault Drops</GRADIENT:26d5ed>")
				.lore(
						"&8Used to toggle default drops",
						"&7Default drops means the normal vanilla",
						"&7drops you would expect",
						"",
						"&7Default Drops&f: " + (this.trackedMob.getOptions().dropDefaultItems() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle default drops."
				)
				.make(), click -> {

			this.trackedMob.getOptions().setDropDefaultItems(!this.trackedMob.getOptions().dropDefaultItems());
			this.trackedMob.sync(result -> {
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
						"&7drop to this mob.",
						"",
						"&e&lClick &8» &7to add drop"
				)
				.make(), click -> click.manager.showGUI(click.player, new DropEditorGUI(this, click.player, MobDrop.empty(this.trackedMob.getEntity()), this.trackedMob, DropEditorGUI.DropEditorMode.ADD)));
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
				).make(), click -> click.manager.showGUI(click.player, new BlockedWorldsListGUI(this, click.player, this.trackedMob)));
	}

	@Override
	protected ItemStack makeDisplayItem(MobDrop drop) {
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
	protected void onClick(MobDrop drop, GuiClickEvent click) {
		if (click.clickType == ClickType.LEFT)
			click.manager.showGUI(click.player, new DropEditorGUI(this, click.player, drop, this.trackedMob, DropEditorGUI.DropEditorMode.EDIT));

		if (click.clickType == ClickType.RIGHT) {
			drop.unStore(result -> {
				if (result == SynchronizeResult.SUCCESS)
					this.trackedMob.getDrops().remove(drop);

				click.manager.showGUI(click.player, new TrackedMobEditorGUI(this.parent, click.player, this.trackedMob));
			});

		}

	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
