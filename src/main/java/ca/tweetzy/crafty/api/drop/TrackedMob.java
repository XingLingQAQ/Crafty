package ca.tweetzy.crafty.api.drop;

import ca.tweetzy.crafty.api.sync.Storeable;
import ca.tweetzy.crafty.api.sync.Synchronize;
import ca.tweetzy.crafty.impl.drop.MobDrop;
import org.bukkit.entity.EntityType;

import java.util.List;

public interface TrackedMob extends Synchronize, Storeable<TrackedMob> {

	EntityType getEntity();

	TrackedOptions getOptions();

	List<MobDrop> getDrops();
}
