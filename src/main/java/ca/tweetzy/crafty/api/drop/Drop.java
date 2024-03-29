package ca.tweetzy.crafty.api.drop;

import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Drop {

	public abstract ItemStack getItem();

	public abstract void setItem(@NonNull final ItemStack newDrop);

	public abstract double getDropChance();

	public abstract void setDropChance(final double chance);

	public abstract List<String> getCommands();

}
