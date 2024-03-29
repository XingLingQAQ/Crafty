package ca.tweetzy.crafty.impl;

import ca.tweetzy.crafty.api.drop.Drop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;


@AllArgsConstructor
public final class BlockDrop extends Drop {

	private ItemStack item;
	private double chance;
	private List<String> commands;

	@Getter
	@Setter
	private boolean dropOnNatural;

	@Getter
	@Setter
	private boolean dropOnPlaced;

	@Override
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public void setItem(@NonNull ItemStack newDrop) {
		this.item = newDrop;
	}

	@Override
	public double getDropChance() {
		return this.chance;
	}

	@Override
	public void setDropChance(double chance) {
		this.chance = chance;
	}

	@Override
	public List<String> getCommands() {
		return this.commands;
	}
}
