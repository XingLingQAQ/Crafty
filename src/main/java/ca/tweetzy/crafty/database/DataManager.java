package ca.tweetzy.crafty.database;


import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.impl.CraftyTrackedBlock;
import ca.tweetzy.crafty.impl.CraftyTrackedOptions;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.database.Callback;
import ca.tweetzy.flight.database.DataManagerAbstract;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.UpdateCallback;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DataManager extends DataManagerAbstract {

	public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
		super(databaseConnector, plugin);
	}

	public void insertTrackedBlock(@NonNull final TrackedBlock trackedBlock, final Callback<TrackedBlock> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "tracked_block (block, default_drops, blocked_worlds) VALUES (?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "tracked_block WHERE block = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, trackedBlock.getBlock().name());

				preparedStatement.setString(1, trackedBlock.getBlock().name());
				preparedStatement.setBoolean(2, trackedBlock.getOptions().dropDefaultItems());
				preparedStatement.setString(3, String.join(";;;", trackedBlock.getOptions().getBlockedWorlds()));

				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractTrackedBlock(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateTrackedBlock(@NonNull final TrackedBlock trackedBlock, final Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "UPDATE " + this.getTablePrefix() + "tracked_block SET default_drops = ?, blocked_worlds = ? WHERE block = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setBoolean(1, trackedBlock.getOptions().dropDefaultItems());
				preparedStatement.setString(2, String.join(";;;", trackedBlock.getOptions().getBlockedWorlds()));
				preparedStatement.setString(3, trackedBlock.getBlock().name());

				int result = preparedStatement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteTrackedBlock(@NonNull final TrackedBlock trackedBlock, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "tracked_block WHERE block = ?")) {
				statement.setString(1, trackedBlock.getBlock().name());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void getTrackedBlocks(@NonNull final Callback<List<TrackedBlock>> callback) {
		final List<TrackedBlock> trackedBlocks = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "tracked_block")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final TrackedBlock trackedBlock = extractTrackedBlock(resultSet);
					trackedBlocks.add(trackedBlock);
				}

				callback.accept(null, trackedBlocks);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}


	private TrackedBlock extractTrackedBlock(final ResultSet resultSet) throws SQLException {
		return new CraftyTrackedBlock(
				CompMaterial.matchCompMaterial(resultSet.getString("block")).get(),
				new CraftyTrackedOptions(
						resultSet.getBoolean("default_drops"),
						new ArrayList<>(List.of(resultSet.getString("blocked_worlds").split(";;;")))
				),
				new ArrayList<>()
		);
	}

	private void resolveUpdateCallback(@Nullable UpdateCallback callback, @Nullable Exception ex) {
		if (callback != null) {
			callback.accept(ex);
		} else if (ex != null) {
			ex.printStackTrace();
		}
	}

	private void resolveCallback(@Nullable Callback<?> callback, @NotNull Exception ex) {
		if (callback != null) {
			callback.accept(ex, null);
		} else {
			ex.printStackTrace();
		}
	}
}
