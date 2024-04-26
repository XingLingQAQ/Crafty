package ca.tweetzy.crafty.gui.drops;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.api.sync.SynchronizeResult;
import ca.tweetzy.crafty.gui.template.CraftyBaseGUI;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.settings.TranslationManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.MathUtil;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class DropConditionsGUI extends CraftyBaseGUI {

	private final Gui parent;
	private final Drop drop;

	public DropConditionsGUI(Gui parent, @NonNull Player player, Drop drop) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eDrop Conditions", 6);
		this.parent = parent;
		this.drop = drop;
		setAcceptsItems(true);
		draw();
	}

	@Override
	protected void draw() {
		applyThemeBorder();
		applyBackExit();

		drawMinPlayers();
		drawMaxPlayers();
		drawItem();

		if (this.drop.getDropType() == Drop.DropType.MOB)
			drawMobName();

		drawPermission();
		drawEnchants();
		drawPotionEffects();
	}

	private void drawMinPlayers() {
		setButton(2, 2, QuickItem
				.of(CompMaterial.LEVER)
				.name("<GRADIENT:3dcf50>&lMinimum Players</GRADIENT:26d5ed>")
				.lore(
						"&8The minimum amount of players",
						"&7This is the minimum amount of players",
						"&7that will be required for the drop",
						"",
						"&7Current&F: &e" + drop.getCondition().getMinimumPlayers(),
						"",
						"&e&lClick &8» &7To change min players."
				)
				.make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lDrop Condition</GRADIENT:26d5ed>", "&7Enter &eminimum # &7of players into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, DropConditionsGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string);
				if (!returnIfNotInt(click.player, string)) return false;

				final int number = Integer.parseInt(string);
				DropConditionsGUI.this.drop.getCondition().setMinimumPlayers(number);
				syncAndReopen();

				return true;
			}
		});
	}

	private void drawMaxPlayers() {
		setButton(3, 2, QuickItem
				.of(CompMaterial.REPEATER)
				.name("<GRADIENT:3dcf50>&LMaximum Players</GRADIENT:26d5ed>")
				.lore(
						"&8The maximum amount of players",
						"&7This is the maximum amount of players",
						"&7that can be online for the drop",
						"",
						"&7Current&F: &e" + drop.getCondition().getMaximumPlayers(),
						"",
						"&e&lClick &8» &7To change max players."
				)
				.make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lDrop Condition</GRADIENT:26d5ed>", "&7Enter &eMaximum # &7of players into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, DropConditionsGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string);
				if (!returnIfNotInt(click.player, string)) return false;

				final int number = Integer.parseInt(string);
				DropConditionsGUI.this.drop.getCondition().setMaximumPlayers(number);
				syncAndReopen();

				return true;
			}
		});
	}

	private void drawItem() {
		setButton(2, 3, QuickItem
				.of(this.drop.getCondition().getItem())
				.lore(
						"&8&m------------------------------",
						"&7This is the item that the player",
						"&7will need to kill/break the mob/block with.",
						"",
						"&c(!) If the item you put here has enchants",
						"&cyou will need to un-toggle any other ones",
						"&cthat you have selected under the enchant options.",
						"",
						"&e&lClick &8» &7With the item to update"
				)
				.make(), click -> {

			final ItemStack cursor = click.cursor;
			if (cursor != null && cursor.getType() != CompMaterial.AIR.parseMaterial()) {
				final ItemStack newIcon = cursor.clone();
				newIcon.setAmount(1);

				this.drop.getCondition().setItem(newIcon);
				this.drop.sync(null);
				drawItem();
			}
		});

		setButton(3, 3, QuickItem
				.of(this.drop.getCondition().isItemRequired() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE)
				.name("<GRADIENT:3dcf50>&LItem Required</GRADIENT:26d5ed>")
				.lore(
						"&8Used to toggle the item requirement",
						"&7If enabled, then the item set above",
						"&7will be needed to obtain the drop.",
						"",
						"&7Required&F: " + (this.drop.getCondition().isItemRequired() ? "&aTrue" : "&cFalse"),
						"",
						"&e&lClick &8» &7To toggle requirement"
				)
				.make(), click -> {

			this.drop.getCondition().setItemRequired(!this.drop.getCondition().isItemRequired());
			this.drop.sync(null);
			drawItem();
		});
	}

	private void drawMobName() {
		setButton(2, 4, QuickItem
				.of(CompMaterial.NAME_TAG)
				.name("<GRADIENT:3dcf50>&LMob Name</GRADIENT:26d5ed>")
				.lore(
						"&8The name required for the mob",
						"&7This is the name that the killed mob",
						"&7must have had for the drop.",
						"",
						"&7Current&f: &e" + this.drop.getCondition().getMobName(),
						"",
						"&e&lClick &8» &7To edit mob name"
				)
				.make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lDrop Condition</GRADIENT:26d5ed>", "&7Enter &emob name &7into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, DropConditionsGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				DropConditionsGUI.this.drop.getCondition().setMobName(string);
				syncAndReopen();
				return true;
			}
		});

		setButton(3, 4, QuickItem
				.of(this.drop.getCondition().isMobNameRequired() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE)
				.name("<GRADIENT:3dcf50>&LMob Name Required</GRADIENT:26d5ed>")
				.lore(
						"&8Used to toggle mob name requirement",
						"&7If enabled, then the mob name above",
						"&7will be needed to obtain the drop.",
						"",
						"&7Required&F: " + (this.drop.getCondition().isMobNameRequired() ? "&aTrue" : "&cFalse"),
						"",
						"&e&lClick &8» &7To toggle requirement"
				)
				.make(), click -> {

			this.drop.getCondition().setMobNameRequired(!this.drop.getCondition().isMobNameRequired());
			drawMobName();
		});
	}

	private void drawPermission() {
		setButton(2, 5, QuickItem
				.of(CompMaterial.PAPER)
				.name("<GRADIENT:3dcf50>&LPermission</GRADIENT:26d5ed>")
				.lore(
						"&8The permission for the drop",
						"&7This is the permission that player",
						"&7must have had for the drop.",
						"",
						"&7Current&f: &e" + this.drop.getCondition().getPermission(),
						"",
						"&e&lClick &8» &7To edit permission"
				)
				.make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lDrop Condition</GRADIENT:26d5ed>", "&7Enter &epermission &7into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, DropConditionsGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				string = ChatColor.stripColor(string);
				DropConditionsGUI.this.drop.getCondition().setPermission(string);
				syncAndReopen();
				return true;
			}
		});

		setButton(3, 5, QuickItem
				.of(this.drop.getCondition().isPermissionRequired() ? CompMaterial.LIME_DYE : CompMaterial.RED_DYE)
				.name("<GRADIENT:3dcf50>&LPermission Required</GRADIENT:26d5ed>")
				.lore(
						"&8Used to toggle the permission requirement",
						"&7If enabled, then the permission above",
						"&7will be needed to obtain the drop.",
						"",
						"&7Required&F: " + (this.drop.getCondition().isPermissionRequired() ? "&aTrue" : "&cFalse"),
						"",
						"&e&lClick &8» &7To toggle requirement"
				)
				.make(), click -> {

			this.drop.getCondition().setPermissionRequired(!this.drop.getCondition().isPermissionRequired());
			drawPermission();
		});
	}

	private void drawEnchants() {
		setButton(2, 6, QuickItem
				.of(CompMaterial.ENCHANTING_TABLE)
				.name("<GRADIENT:3dcf50>&lEnchantments</GRADIENT:26d5ed>")
				.lore(
						"&8A list of required enchantments",
						"&7You can add/remove required enchantments",
						"&7on the item used to obtain the drop",
						"",
						"&e&lClick &8» &7To edit enchantments"
				)
				.make(), click -> click.manager.showGUI(click.player, new EnchantmentsSelectGUI(this, click.player, this.drop)));
	}

	private void drawPotionEffects() {
		setButton(3, 6, QuickItem
				.of(CompMaterial.SPLASH_POTION)
				.hideTags(true)
				.name("<GRADIENT:3dcf50>&lPotion Effects</GRADIENT:26d5ed>")
				.lore(
						"&8A list of required effects",
						"&7You can add/remove required potion effects",
						"&7the player must have to get the drop",
						"",
						"&e&lClick &8» &7To edit potion effects"
				)
				.make(), click -> click.manager.showGUI(click.player, new PotionEffectSelectGUI(this, click.player, this.drop)));
	}

	private boolean returnIfNotInt(Player player, String string) {
		if (!MathUtil.isInt(string)) {
			Common.tell(player, TranslationManager.string(Translations.NOT_A_NUMBER, "value", string));
			return false;
		}

		return true;
	}

	private void syncAndReopen() {
		this.drop.sync(result -> {
			if (result == SynchronizeResult.FAILURE) {
				Common.tell(this.player, "&cSomething went wrong while saving drop condition");
			}

			Crafty.getGuiManager().showGUI(this.player, new DropConditionsGUI(this.parent, this.player, this.drop));
		});
	}
}
