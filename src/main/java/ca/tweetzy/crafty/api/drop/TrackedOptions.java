package ca.tweetzy.crafty.api.drop;

import java.util.List;

public interface TrackedOptions {

	boolean dropDefaultItems();

	void setDropDefaultItems(final boolean drop);

	List<String> getBlockedWorlds();

}
