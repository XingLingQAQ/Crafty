package ca.tweetzy.crafty.listener;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.impl.BlockDrop;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		final Player player = event.getPlayer();
		final Block block = event.getBlock();

		if (!Crafty.getBlockDropManager().isTracked(CompMaterial.matchCompMaterial(block.getType()))) return;
		final TrackedBlock trackedBlock = Crafty.getBlockDropManager().get(CompMaterial.matchCompMaterial(block.getType()));

		// is the current world blocked??
		if (trackedBlock.getOptions().getBlockedWorlds().contains(block.getWorld().getName())) return;
		// check if the tracked options allow drops on player placed blocks

		if (!trackedBlock.getOptions().dropDefaultItems()) {
			block.getDrops().clear();
			event.setDropItems(false);
		}


		for (BlockDrop drop : trackedBlock.getDrops()) {
			//todo check for natural spawn and player placed

			if (chanceBasedOnPercentage((float) drop.getChance())) {
				block.getWorld().dropItemNaturally(block.getLocation(), drop.getItem());
				// execute commands
				drop.getCommands().forEach( command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replacer.replaceVariables(command, "player", player.getName())));
			}
		}

	}

	public boolean chanceBasedOnPercentage(float percentage) {
		// Convert the percentage to a value between 0.0 and 1.0
		float probability = percentage / 100.0f;

		// Generate a random number between 0.0 and 1.0
		double randomValue = Math.random();

		// Return true if the random value is less than or equal to the probability
		return randomValue <= probability;
	}
}
