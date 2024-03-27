package ca.tweetzy.crafty.api.sync;

public interface Matchable {

	boolean isMatch(final String keyword);

	String getName();
}
