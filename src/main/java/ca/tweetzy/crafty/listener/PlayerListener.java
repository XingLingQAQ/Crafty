package ca.tweetzy.crafty.listener;

import ca.tweetzy.crafty.Crafty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();

		if (Crafty.getRecipeManager().getValues().isEmpty()) return;
		Crafty.getRecipeManager().getValues().forEach(recipe -> {
			if (!player.hasDiscoveredRecipe(recipe.getKey()))
				player.discoverRecipe(recipe.getKey());
		});
	}
}
