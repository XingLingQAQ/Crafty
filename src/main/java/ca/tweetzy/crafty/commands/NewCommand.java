package ca.tweetzy.crafty.commands;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.gui.recipe.CookingRecipeGUI;
import ca.tweetzy.crafty.gui.recipe.CraftingTableRecipeGUI;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyBlastFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyCampfireRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftyFurnaceRecipe;
import ca.tweetzy.crafty.impl.recipe.cooking.CraftySmokerRecipe;
import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.QuickItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class NewCommand extends Command {

	private final List<String> VALID_RECIPE_OPTIONS = List.of("crafting", "furnace", "blastfurnace", "smoker", "campfire");

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

		if (recipeOption.equalsIgnoreCase("crafting")) {
			Crafty.getGuiManager().showGUI(player, new CraftingTableRecipeGUI(null, player, new CraftingTableRecipe("craftingrecipe"), false));
			return ReturnType.SUCCESS;
		}

		Crafty.getGuiManager().showGUI(player, new CookingRecipeGUI(null, player, switch (recipeOption) {
			case "furnace" -> new CraftyFurnaceRecipe("furnacerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
			case "blastfurnace" -> new CraftyBlastFurnaceRecipe("blastfurnacerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
			case "campfire" -> new CraftyCampfireRecipe("campfirerecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
			case "smoker" -> new CraftySmokerRecipe("smokerrecipe", QuickItem.of(CompMaterial.PAPER).make(), QuickItem.of(CompMaterial.BLACK_DYE).make(), 20, 20);
			default -> throw new IllegalStateException("Unexpected value: " + recipeOption);
		}, false));

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
