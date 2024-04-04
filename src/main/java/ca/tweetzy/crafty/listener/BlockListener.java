package ca.tweetzy.crafty.listener;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.impl.BlockDrop;
import ca.tweetzy.crafty.model.Chance;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
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

		// grab pdc
		final PersistentDataContainer container = new CustomBlockData(block, Crafty.getInstance());

		for (BlockDrop drop : trackedBlock.getDrops()) {
			// block was placed by a player & drop on place is false
			if (!drop.isDropOnPlaced() && container.has(Crafty.getPlacedBlockKey(), PersistentDataType.BOOLEAN)) {
				continue;
			}

			// was placed by user yet natural drops are on so skip as well
			if (drop.isDropOnNatural() && container.has(Crafty.getPlacedBlockKey(), PersistentDataType.BOOLEAN)) {
				continue;
			}

			if (Chance.byPercentage((float) drop.getChance())) {
				block.getWorld().dropItemNaturally(block.getLocation(), drop.getItem());
				// execute commands
				drop.getCommands().forEach( command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replacer.replaceVariables(command, "player", player.getName())));
			}
		}

		// remove key afterwards
		if (container.has(Crafty.getPlacedBlockKey(), PersistentDataType.BOOLEAN))
			container.remove(Crafty.getPlacedBlockKey());

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Block block = event.getBlock();
		// apply pdc value to track placed
		final PersistentDataContainer container = new CustomBlockData(block, Crafty.getInstance());
		container.set(Crafty.getPlacedBlockKey(), PersistentDataType.BOOLEAN, true);
	}


}
