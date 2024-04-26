package ca.tweetzy.crafty.impl.drop;

import ca.tweetzy.crafty.api.drop.Condition;
import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public final class MobDrop extends Drop {

	private EntityType parentMob;
	private boolean dropOnNatural;
	private boolean dropFromSpawner;
	private boolean dropFromEgg;
	private Condition condition;

	public MobDrop(@NonNull final UUID id, EntityType parentMob, @NonNull final ItemStack item, final double chance, List<String> commands, boolean dropOnNatural, boolean dropFromSpawner, boolean dropFromEgg, Condition condition) {
		super(id, DropType.MOB, item, chance, commands, condition);
		this.parentMob = parentMob;
		this.dropOnNatural = dropOnNatural;
		this.dropFromSpawner = dropFromSpawner;
		this.dropFromEgg = dropFromEgg;
		this.condition = condition;
	}

	public MobDrop(@NonNull final UUID id, EntityType parentMob, @NonNull final ItemStack item, final double chance, boolean dropOnNatural, boolean dropFromSpawner, boolean dropFromEgg) {
		this(id, parentMob, item, chance, new ArrayList<>(), dropOnNatural, dropFromSpawner, dropFromEgg, DropCondition.template());
	}

	public MobDrop(@NonNull final UUID id, EntityType parentMob, @NonNull final ItemStack item, final double chance) {
		this(id, parentMob, item, chance, new ArrayList<>(), true, true, true, DropCondition.template());
	}

	public static MobDrop empty(EntityType parentMob) {
		return new MobDrop(UUID.randomUUID(), parentMob, CompMaterial.STONE.parseItem(), 50, new ArrayList<>(), true, true, true, DropCondition.template());
	}
}
