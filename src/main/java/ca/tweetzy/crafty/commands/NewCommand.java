package ca.tweetzy.crafty.commands;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.crafting.CraftingTableRecipeGUI;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class NewCommand extends Command {

	private final List<String> VALID_RECIPE_OPTIONS = List.of("crafting", "furnace", "blastfurnace", "brewing", "smoker", "campfire");

	public NewCommand() {
		super(AllowedExecutor.PLAYER, "new");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		final Player player = (Player) sender;
		if (args.length < 1) return ReturnType.INVALID_SYNTAX;

		final String recipeOption = args[0].toLowerCase();

		if (!VALID_RECIPE_OPTIONS.contains(recipeOption)) {
			tell(player, "not a valid option");
			return ReturnType.FAIL;
		}

		switch (recipeOption) {
			case "crafting" -> Crafty.getGuiManager().showGUI(player, new CraftingTableRecipeGUI(null, player, new CraftingTableRecipe("craftingrecipe")));
			default -> {}
		}


		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		if (args.length == 1)
			return this.VALID_RECIPE_OPTIONS;
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "crafty.command.new";
	}

	@Override
	public String getSyntax() {
		return "new <recipe type>";
	}

	@Override
	public String getDescription() {
		return "Used to quickly open recipe creation menu";
	}
}
