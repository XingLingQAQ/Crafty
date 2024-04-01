package ca.tweetzy.crafty.model.manager;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.manager.KeyValueManager;
import ca.tweetzy.crafty.api.manager.ListManager;
import ca.tweetzy.crafty.impl.CraftyTrackedBlock;
import ca.tweetzy.crafty.impl.CraftyTrackedOptions;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class DropManager extends ListManager<Drop> {

	public DropManager() {
		super("Drop");
	}

	public void addDrop(@NonNull final Drop drop, @NonNull final Consumer<Boolean> created) {
		drop.store(storedDrop -> {
			if (storedDrop != null) {
				add(drop);
				created.accept(true);
			} else {
				created.accept(false);
			}
		});
	}

	@Override
	public void load() {
		clear();

	}
}
