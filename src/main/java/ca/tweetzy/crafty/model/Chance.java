package ca.tweetzy.crafty.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Chance {

	public boolean byPercentage(float percentage) {
		// Convert the percentage to a value between 0.0 and 1.0
		float probability = percentage / 100.0f;

		// Generate a random number between 0.0 and 1.0
		double randomValue = Math.random();

		// Return true if the random value is less than or equal to the probability
		return randomValue <= probability;
	}
}
