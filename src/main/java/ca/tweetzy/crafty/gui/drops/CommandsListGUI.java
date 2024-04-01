package ca.tweetzy.crafty.gui.drops;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.gui.template.CraftyPagedGUI;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.events.GuiClickEvent;
import ca.tweetzy.flight.gui.helper.InventoryBorder;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.flight.utils.input.TitleInput;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class CommandsListGUI extends CraftyPagedGUI<String> {

	private final Gui parent;
	private final Drop drop;

	public CommandsListGUI(Gui parent, @NonNull Player player, Drop drop) {
		super(parent, player, "<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &7> &eCommands", 6, drop.getCommands());
		this.parent = parent;
		this.drop = drop;
		draw();
	}

	@Override
	protected void prePopulate() {
		applyThemeBorder();
	}

	@Override
	protected void drawFixed() {
		applyBackExit();

		// add command button
		setButton(getRows() - 1, 4, QuickItem
				.of(CompMaterial.LIME_DYE)
				.name("<GRADIENT:3dcf50>&lAdd Command</GRADIENT:26d5ed>")
				.lore(
						"&8Used to add command",
						"&7Commands are executed at the same time",
						"&7as the item drop. They also use the",
						"&7drop percentage.",
						"",
						"&e&lClick &8» &7to add command"
				).make(), click -> new TitleInput(Crafty.getInstance(), click.player, "<GRADIENT:3dcf50>&lNew Command</GRADIENT:26d5ed>", "&7Enter new &ecommand chance &7into chat.") {

			@Override
			public void onExit(Player player) {
				click.manager.showGUI(click.player, CommandsListGUI.this);
			}

			@Override
			public boolean onResult(String string) {
				CommandsListGUI.this.drop.getCommands().add(string);
				click.manager.showGUI(click.player, new CommandsListGUI(CommandsListGUI.this.parent, click.player, CommandsListGUI.this.drop));
				return true;
			}
		});
	}

	@Override
	protected ItemStack makeDisplayItem(String command) {
		return QuickItem.of(CompMaterial.PAPER).name(command).lore(
				"&4&lClick &8» &cto delete command"
		).make();
	}

	@Override
	protected void onClick(String command, GuiClickEvent click) {
		this.drop.getCommands().remove(command);
		draw();
	}

	@Override
	protected List<Integer> fillSlots() {
		return InventoryBorder.getInsideBorders(6);
	}
}
