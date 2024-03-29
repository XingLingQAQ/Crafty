package ca.tweetzy.crafty.api.drop;

import ca.tweetzy.crafty.api.sync.Storeable;
import ca.tweetzy.crafty.api.sync.Synchronize;
import ca.tweetzy.flight.comp.enums.CompMaterial;

import java.util.List;

public interface TrackedBlock extends Synchronize, Storeable<TrackedBlock> {

	CompMaterial getBlock();

	TrackedOptions getOptions();

	List<Drop> getDrops();
}
