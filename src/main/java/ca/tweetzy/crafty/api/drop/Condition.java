package ca.tweetzy.crafty.api.drop;

import ca.tweetzy.crafty.api.sync.Jsonable;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.Common;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public interface Condition extends Jsonable {

	int getMinimumPlayers();

	void setMinimumPlayers(final int minimumPlayers);

	int getMaximumPlayers();

	void setMaximumPlayers(final int maximumPlayers);

	String getMobName();

	void setMobName(final String mobName);

	boolean isMobNameRequired();

	void setMobNameRequired(final boolean enabled);

	ItemStack getItem();

	void setItem(final ItemStack item);

	boolean isItemRequired();

	void setItemRequired(final boolean required);

	String getPermission();

	void setPermission(final String permission);

	boolean isPermissionRequired();

	void setPermissionRequired(final boolean required);

	List<Enchantment> getRequiredEnchants();

	List<PotionEffectType> getRequiredPotionEffects();

	default boolean meetsConditions(BlockBreakEvent event) {
		final Player player = event.getPlayer();
		return meetsShared(player);
	}

	default boolean meetsConditions(LivingEntity entity, Player killer) {
		boolean meets = meetsShared(killer);
		if (isMobNameRequired()) {
			if (entity.getCustomName() == null || !entity.getCustomName().equalsIgnoreCase(Common.colorize(getMobName()))) meets = false;
		}

		return meets;
	}

	private boolean meetsShared(final Player player) {
		final int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();

		// online player count check
		boolean meets = true;
		if (!(onlinePlayers >= getMinimumPlayers() && onlinePlayers <= getMaximumPlayers()))
			meets = false;

		// check held item
		final ItemStack hand = player.getInventory().getItemInMainHand();

		if (isItemRequired() && hand != null && hand.getType() != CompMaterial.AIR.parseMaterial()) {
			if (!matchMeta(getItem(), hand))
				meets = false;
		}

		if (!isItemRequired() && hand != null && hand.getType() != CompMaterial.AIR.parseMaterial()) {
			if (!getRequiredEnchants().isEmpty()) {
				if (hand.getEnchantments().isEmpty()) {
					meets = false;
				} else {
					for (Enchantment requiredEnchant : getRequiredEnchants()) {
						if (!hand.containsEnchantment(requiredEnchant)) {
							meets = false;
							break;
						}
					}
				}
			}
		}

		if (isPermissionRequired() && !player.hasPermission(getPermission())) {
			meets = false;
		}

		if (!getRequiredPotionEffects().isEmpty()) {
			if (player.getActivePotionEffects().isEmpty()) {
				meets = false;
			} else {
				for (PotionEffectType requiredPotionEffect : getRequiredPotionEffects()) {
					if (!player.hasPotionEffect(requiredPotionEffect)) {
						meets = false;
						break;
					}

				}
			}
		}

		return meets;
	}


	private boolean matchMeta(ItemStack a, ItemStack b) {
		if (a == null || b == null) return a == null && b == null;
		if (a.isSimilar(b)) return true;

		final boolean sameType = a.getType() == b.getType();
		final boolean hasMeta = a.hasItemMeta() == b.hasItemMeta();

		if (!hasMeta && sameType)
			 return true;

		// this lowkey sucks, but it works until I find an easier way to fix this
		JsonObject aObj = JsonParser.parseString(a.getItemMeta().getAsString()).getAsJsonObject();
		JsonObject bObj = JsonParser.parseString(b.getItemMeta().getAsString()).getAsJsonObject();

		if (aObj.has("Damage"))
			aObj.remove("Damage");

		if (bObj.has("Damage"))
			bObj.remove("Damage");

		return sameType && hasMeta && aObj.toString().equalsIgnoreCase(bObj.toString());
	}

}