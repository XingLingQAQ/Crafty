package ca.tweetzy.crafty.model;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class StringUtil {

	public <K, V> Optional<K> findKeyByValue(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return Optional.of(entry.getKey());
			}
		}
		return Optional.empty();
	}

	public List<String> divideIntoChunks(String text, int chunkSize) {
		List<String> results = new ArrayList<>();
		int length = text.length();

		for (int i = 0; i < length; i += chunkSize) {
			results.add(text.substring(i, Math.min(length, i + chunkSize)));
		}

		return results;
	}
}
