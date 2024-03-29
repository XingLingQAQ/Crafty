package ca.tweetzy.crafty.impl;

import ca.tweetzy.crafty.api.drop.TrackedOptions;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public final class CraftyTrackedOptions implements TrackedOptions {

	private boolean defaultDrops;
	private List<String> blockedWorlds;

	@Override
	public boolean dropDefaultItems() {
		return this.defaultDrops;
	}

	@Override
	public void setDropDefaultItems(boolean drop) {
		this.defaultDrops = drop;
	}

	@Override
	public List<String> getBlockedWorlds() {
		return this.blockedWorlds;
	}
}
