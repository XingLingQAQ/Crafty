package ca.tweetzy.crafty.gui.drops;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.gui.CraftyMainAdminGUI;
import ca.tweetzy.crafty.gui.drops.block.BlockDropListGUI;
import ca.tweetzy.crafty.gui.drops.block.TrackedBlockEditorGUI;
import ca.tweetzy.crafty.gui.drops.mob.MobDropListGUI;
import ca.tweetzy.crafty.gui.drops.mob.TrackedMobEditorGUI;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.crafty.impl.drop.BlockDrop;
import ca.tweetzy.crafty.impl.drop.MobDrop;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.template.MaterialPickerGUI;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.ChatUtil;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public final class DropEditorGUI extends CraftyBaseGUI {

	private final Gui parent;
	private final Drop drop;
	private TrackedBlock trackedBlock;
	private TrackedMob trackedMob;
	private DropEditorMode editorMode;

	private Drop.DropType type;

	public DropEditorGUI(Gui parent, @NonNull Player player, Drop drop, TrackedBlock trackedBlock, DropEditorMode editorMode) {
		super(parent, player, String.format("%s &7> %s &7> %s", "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed>", "&e" + ChatUtil.capitalizeFully(trackedBlock.getBlock()), editorMode == DropEditorMode.ADD ? "&aNew" : "&eEdit"), 6);
		this.parent = parent;
		this.drop = drop;
		this.trackedBlock = trackedBlock;
		this.editorMode = editorMode;

		this.type = this.drop instanceof BlockDrop ? Drop.DropType.BLOCK : Drop.DropType.MOB;

		setAcceptsItems(true);
		draw();
	}

	public DropEditorGUI(Gui parent, @NonNull Player player, Drop drop, TrackedMob trackedMob, DropEditorMode editorMode) {
		super(parent, player, String.format("%s &7> %s &7> %s", "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed>", "&e" + ChatUtil.capitalizeFully(trackedMob.getEntity()), editorMode == DropEditorMode.ADD ? "&aNew" : "&eEdit"), 6);
		this.parent = parent;
		this.drop = drop;
		this.trackedMob = trackedMob;
		this.editorMode = editorMode;
		this.type = this.drop instanceof BlockDrop ? Drop.DropType.BLOCK : Drop.DropType.MOB;
		setAcceptsItems(true);
		draw();
	}


	@Override
	protected void draw() {
		applyThemeBorder();
		applyBackExit();

		drawSharedOptions();

		if (this.type == Drop.DropType.BLOCK)
			drawBlockOptions();

		if (this.type == Drop.DropType.MOB)
			drawMobOptions();

		// create / save button
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name(this.editorMode == DropEditorMode.ADD ? "<GRADIENT:3dcf50>&lAdd</GRADIENT:26d5ed>" : "<GRADIENT:3dcf50>&LSave</GRADIENT:26d5ed>")
				.lore(this.editorMode == DropEditorMode.ADD ? "&e&lClick &8» &7to add drop" : "&e&lClick &8» &7to save changes")
				.make(), click -> {

			if (this.trackedBlock != null) {
				if (this.editorMode == DropEditorMode.ADD) {
					this.trackedBlock.getDrops().add((BlockDrop) this.drop);
					Crafty.getDropManager().addDrop(drop, created -> click.manager.showGUI(click.player, new TrackedBlockEditorGUI(new BlockDropListGUI(new CraftyMainAdminGUI(click.player), click.player), click.player, this.trackedBlock)));
				}

				if (this.editorMode == DropEditorMode.EDIT)
					drop.sync(result -> click.manager.showGUI(click.player, new TrackedBlockEditorGUI(new BlockDropListGUI(new CraftyMainAdminGUI(click.player), click.player), click.player, this.trackedBlock)));
			}

			if (this.trackedMob != null) {
				if (this.editorMode == DropEditorMode.ADD) {
					this.trackedMob.getDrops().add((MobDrop) this.drop);
					Crafty.getDropManager().addDrop(drop, created -> click.manager.showGUI(click.player, new TrackedMobEditorGUI(new MobDropListGUI(new CraftyMainAdminGUI(click.player), click.player), click.player, this.trackedMob)));
				}

				if (this.editorMode == DropEditorMode.EDIT)
					drop.sync(result -> click.manager.showGUI(click.player, new TrackedMobEditorGUI(new MobDropListGUI(new CraftyMainAdminGUI(click.player), click.player), click.player, this.trackedMob)));
			}

		});
	}

	private void drawMobOptions() {
		// mobDrop
		final MobDrop mobDrop = (MobDrop) this.drop;

		// Drop on natural
		setButton(2, 2, QuickItem
				.of(mobDrop.isDropOnNatural() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("<GRADIENT:3dcf50>&LDrop On Natural</GRADIENT:26d5ed>")
				.lore(
						"&8Toggles drops on natural spawns",
						"&7Natural spawners are considered mobs that",
						"&7were spawned in during world 'naturally'.",
						"&7so mobs not from spawners, eggs, splitting",
						"",
						"&7Current&F: " + (mobDrop.isDropOnNatural() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle option."
				)
				.make(), click -> {

			mobDrop.setDropOnNatural(!mobDrop.isDropOnNatural());
			drawMobOptions();
		});

		// Drop on spawner
		setButton(3, 2, QuickItem
				.of(mobDrop.isDropFromSpawner() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("<GRADIENT:3dcf50>&LDrop From Spawner</GRADIENT:26d5ed>")
				.lore(
						"&8Toggles drops on from spawners",
						"&7This means that if enabled the mobs that",
						"&7were spawned in by a mob spawner will",
						"&7also be able to drop the item.",
						"",
						"&7Current&F: " + (mobDrop.isDropFromSpawner() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle option."
				)
				.make(), click -> {

			mobDrop.setDropFromSpawner(!mobDrop.isDropFromSpawner());
			drawMobOptions();
		});

		// Drop on egg
		setButton(3, 3, QuickItem
				.of(mobDrop.isDropFromEgg() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("<GRADIENT:3dcf50>&LDrop From Egg</GRADIENT:26d5ed>")
				.lore(
						"&8Toggles drops on from eggs",
						"&7This means that if enabled the mobs that",
						"&7were spawned in by a their spawn egg",
						"&7also be able to drop the item.",
						"",
						"&7Current&F: " + (mobDrop.isDropFromEgg() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle option."
				)
				.make(), click -> {

			mobDrop.setDropFromEgg(!mobDrop.isDropFromEgg());
			drawMobOptions();
		});
	}

	private void drawBlockOptions() {
		// blockDrop
		final BlockDrop blockDrop = (BlockDrop) this.drop;

		// Drop on natural
		setButton(2, 2, QuickItem
				.of(blockDrop.isDropOnNatural() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("<GRADIENT:3dcf50>&LDrop On Natural</GRADIENT:26d5ed>")
				.lore(
						"&8Toggles drops on natural blocks",
						"&7Natural blocks are considered blocks that",
						"&7were spawned in during world generation.",
						"&7pretty much blocks not placed by players.",
						"",
						"&7Current&F: " + (blockDrop.isDropOnNatural() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle option."
				)
				.make(), click -> {

			blockDrop.setDropOnNatural(!blockDrop.isDropOnNatural());
			drawBlockOptions();
		});

		// Drop on place
		setButton(3, 2, QuickItem
				.of(blockDrop.isDropOnPlaced() ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.RED_STAINED_GLASS_PANE)
				.name("<GRADIENT:3dcf50>&LDrop On Place</GRADIENT:26d5ed>")
				.lore(
						"&8Toggles drops on place blocks",
						"&7This option is to enabled the drop if",
						"&7the block was placed by a player.",
						"&7If disabled, newly placed blocks will ignore drops.",
						"",
						"&7Current&F: " + (blockDrop.isDropOnPlaced() ? "&aEnabled" : "&cDisabled"),
						"",
						"&e&lClick &8» &7to toggle option."
				)
				.make(), click -> {

			blockDrop.setDropOnPlaced(!blockDrop.isDropOnPlaced());
			drawBlockOptions();
		});
	}

	private void drawSharedOptions() {
		drawDropItem();
		drawChance();
		drawCommands();
		drawConditions();
	}

	private void drawDropItem() {
		setButton(1, 4, QuickItem
				.of(this.drop.getItem())
				.lore(
						"",
						"&e&lLeft Click &8» &7to open material picker",
						"&b&lRight Click &8» &7with item to set"
				)
				.make(), click -> {

			if (click.clickType == ClickType.RIGHT) {
				final ItemStack cursor = click.cursor;
				if (cursor != null && cursor.getType() != CompMaterial.AIR.parseMaterial()) {
					final ItemStack newIcon = cursor.clone();
					newIcon.setAmount(1);

					this.drop.setItem(newIcon);
					drawDropItem();
				}
			}

			if (click.clickType == ClickType.LEFT) {
				click.manager.showGUI(click.player, new MaterialPickerGUI(this, null, "", (event, selected) -> {
					this.drop.setItem(selected);
					if (this.trackedBlock != null) {
						click.manager.showGUI(click.player, new DropEditorGUI(DropEditorGUI.this.parent, click.player, DropEditorGUI.this.drop, DropEditorGUI.this.trackedBlock, DropEditorGUI.this.editorMode));
					}

					if (this.trackedMob != null) {
						click.manager.showGUI(click.player, new DropEditorGUI(DropEditorGUI.this.parent, click.player, DropEditorGUI.this.drop, DropEditorGUI.this.trackedMob, DropEditorGUI.this.editorMode));

					}
				}));
			}
		});
	}

	private void drawChance() {
		setButton(3, this.type == Drop.DropType.BLOCK ? 4 : 5, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:3dcf50>&lDrop Chance</GRADIENT:26d5ed>")
				.lore(
						"&8Used to change drop chance",
						"&7This is the percentage chance of the",
						"&7item being dropped from the " + (this.drop instanceof BlockDrop ? "block." : "mob."),
						"",
						"&7Current Chance&f: &a" + this.drop.getChance() + "&f%",
						"",
						"&e&lClick &8» &7to change chance"
				).make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lDrop Chance</GRADIENT:26d5ed>", "&7Enter new &edrop chance &7into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, DropEditorGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				// filter out colour
				string = ChatColor.stripColor(string);
				// validate
				if (!MathUtil.isDouble(string)) {
					Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
					return false;
				}

				final double chance = Double.parseDouble(string);

				// verify not NaN
				if (Double.isNaN(chance)) {
					Common.tell(click.player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
					return false;
				}

				DropEditorGUI.this.drop.setChance(chance);
				if (DropEditorGUI.this.trackedBlock != null)
					click.manager.showGUI(click.player, new DropEditorGUI(DropEditorGUI.this.parent, click.player, DropEditorGUI.this.drop, DropEditorGUI.this.trackedBlock, DropEditorGUI.this.editorMode));

				if (DropEditorGUI.this.trackedMob != null)
					click.manager.showGUI(click.player, new DropEditorGUI(DropEditorGUI.this.parent, click.player, DropEditorGUI.this.drop, DropEditorGUI.this.trackedMob, DropEditorGUI.this.editorMode));

				return true;
			}
		});
	}

	private void drawCommands() {
		setButton(3, 6, QuickItem
				.of(CompMaterial.WRITTEN_BOOK)
				.name("<GRADIENT:3dcf50>&lCommands</GRADIENT:26d5ed>")
				.lore(
						"&8View all commands",
						"&7Opens a menu to view all drop commands",
						"&7you can also add/remove in that menu.",
						"",
						"&e&lClick &8» &7to view commands"
				)
				.hideTags(true).make(), click -> click.manager.showGUI(click.player, new CommandsListGUI(this, click.player, this.drop)));
	}

	private void drawConditions() {
		setButton(2, 6, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:3dcf50>&LConditions</GRADIENT:26d5ed>")
				.lore(
						"&8View all conditions",
						"&7Conditions are literally just requirements",
						"&7that must be meet for the chance to roll.",
						"",
						"&e&lClick &8» &7to view conditions"
				)
				.hideTags(true).make(), click -> click.manager.showGUI(click.player, new DropConditionsGUI(this, click.player, this.drop)));
	}

	public enum DropEditorMode {

		ADD, EDIT
	}
}
