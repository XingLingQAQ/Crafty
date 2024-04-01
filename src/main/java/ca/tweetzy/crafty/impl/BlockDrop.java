package ca.tweetzy.crafty.impl;

import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public final class BlockDrop extends Drop {

	private CompMaterial parentBlock;
	private boolean dropOnNatural;
	private boolean dropOnPlaced;

	public BlockDrop(@NonNull final UUID id, CompMaterial parentBlock, @NonNull final ItemStack item, final double chance, boolean dropOnNatural, boolean dropOnPlaced, List<String> commands) {
		super(id, DropType.BLOCK, item, chance, commands);
		this.dropOnNatural = dropOnNatural;
		this.dropOnPlaced = dropOnPlaced;
		this.parentBlock = parentBlock;
	}

	public BlockDrop(@NonNull final UUID id, CompMaterial parentBlock, @NonNull final ItemStack item, final double chance, boolean dropOnNatural, boolean dropOnPlaced) {
		this(id, parentBlock, item, chance, dropOnNatural, dropOnPlaced, new ArrayList<>());
	}

	public BlockDrop(@NonNull final UUID id, CompMaterial parentBlock, @NonNull final ItemStack item, final double chance) {
		this(id, parentBlock, item, chance, true, false, new ArrayList<>());
	}

	public static BlockDrop empty(CompMaterial parentBlock) {
		return new BlockDrop(UUID.randomUUID(), parentBlock, CompMaterial.STONE.parseItem(), 50, true, true, new ArrayList<>());
	}
}
