package ca.tweetzy.crafty.listener;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Common;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();

		if (!Crafty.getBlockDropManager().isTracked(CompMaterial.matchCompMaterial(block.getType()))) return;
		final TrackedBlock trackedBlock = Crafty.getBlockDropManager().get(CompMaterial.matchCompMaterial(block.getType()));

		// is the current world blocked??
		if (trackedBlock.getOptions().getBlockedWorlds().contains(block.getWorld().getName())) return;

		if (!trackedBlock.getOptions().dropDefaultItems()) {
			block.getDrops().clear();
			event.setDropItems(false);
		}
	}
}
