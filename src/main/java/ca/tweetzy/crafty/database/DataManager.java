package ca.tweetzy.crafty.database;


import ca.tweetzy.crafty.api.drop.Drop;
import ca.tweetzy.crafty.api.drop.TrackedBlock;
import ca.tweetzy.crafty.api.drop.TrackedMob;
import ca.tweetzy.crafty.api.recipe.CustomRecipe;
import ca.tweetzy.crafty.api.recipe.RecipeType;
import ca.tweetzy.crafty.impl.drop.*;
import ca.tweetzy.crafty.impl.recipe.CraftingTableRecipe;
import ca.tweetzy.crafty.impl.recipe.CraftyCookingRecipe;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.database.Callback;
import ca.tweetzy.flight.database.DataManagerAbstract;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.UpdateCallback;
import ca.tweetzy.flight.utils.SerializeUtil;
import lombok.NonNull;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class DataManager extends DataManagerAbstract {

	public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
		super(databaseConnector, plugin);
	}

	/*
	===============================================================================================================================
	TRACKED MOBS
	===============================================================================================================================
	 */
	public void insertTrackedMob(@NonNull final TrackedMob trackedMob, final Callback<TrackedMob> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "tracked_mob (mob, default_drops, blocked_worlds) VALUES (?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "tracked_mob WHERE mob = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, trackedMob.getEntity().name());

				preparedStatement.setString(1, trackedMob.getEntity().name());
				preparedStatement.setBoolean(2, trackedMob.getOptions().dropDefaultItems());
				preparedStatement.setString(3, String.join(";;;", trackedMob.getOptions().getBlockedWorlds()));

				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractTrackedMob(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateTrackedMob(@NonNull final TrackedMob trackedMob, final Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "UPDATE " + this.getTablePrefix() + "tracked_mob SET default_drops = ?, blocked_worlds = ? WHERE mob = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setBoolean(1, trackedMob.getOptions().dropDefaultItems());
				preparedStatement.setString(2, String.join(";;;", trackedMob.getOptions().getBlockedWorlds()));
				preparedStatement.setString(3, trackedMob.getEntity().name());

				int result = preparedStatement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteTrackedMob(@NonNull final TrackedMob trackedMob, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "tracked_mob WHERE mob = ?")) {
				statement.setString(1, trackedMob.getEntity().name());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void getTrackedMobs(@NonNull final Callback<List<TrackedMob>> callback) {
		final List<TrackedMob> trackedMobs = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "tracked_mob")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final TrackedMob trackedMob = extractTrackedMob(resultSet);
					trackedMobs.add(trackedMob);
				}

				callback.accept(null, trackedMobs);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}


	private TrackedMob extractTrackedMob(final ResultSet resultSet) throws SQLException {
		return new CraftyTrackedMob(
				Enum.valueOf(EntityType.class, resultSet.getString("mob")),
				new CraftyTrackedOptions(
						resultSet.getBoolean("default_drops"),
						new ArrayList<>(List.of(resultSet.getString("blocked_worlds").split(";;;")))
				),
				new ArrayList<>()
		);
	}

	/*
	===============================================================================================================================
	TRACKED BLOCKS
	===============================================================================================================================
	 */

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

	/*
	===============================================================================================================================
	DROPS
	===============================================================================================================================
	 */

	public void insertTrackedDrop(@NonNull final Drop drop, final Callback<Drop> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "drop (id, type, block, entity, item, chance, commands, drop_on_natural, drop_on_placed, drop_from_spawner, drop_from_egg, conditions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "drop WHERE id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, drop.getId().toString());

				preparedStatement.setString(1, drop.getId().toString());
				preparedStatement.setString(2, drop.getDropType().name());

				// block drop specific
				if (drop instanceof final BlockDrop blockDrop) {
					preparedStatement.setString(3, blockDrop.getParentBlock().name());
					preparedStatement.setString(4, null);

					preparedStatement.setBoolean(8, blockDrop.isDropOnNatural());
					preparedStatement.setBoolean(9, blockDrop.isDropOnPlaced());
					preparedStatement.setBoolean(10, false);
					preparedStatement.setBoolean(11, false);
				}

				// mob drop specific
				if (drop instanceof final MobDrop mobDrop) {
					preparedStatement.setString(3, null);
					preparedStatement.setString(4, mobDrop.getParentMob().name());
					preparedStatement.setBoolean(8, mobDrop.isDropOnNatural());
					preparedStatement.setBoolean(9, false);
					preparedStatement.setBoolean(10, mobDrop.isDropFromSpawner());
					preparedStatement.setBoolean(11, mobDrop.isDropFromEgg());
				}

				preparedStatement.setString(5, SerializeUtil.encodeItem(drop.getItem()));
				preparedStatement.setDouble(6, drop.getChance());
				preparedStatement.setString(7, String.join(";;;", drop.getCommands()));

				preparedStatement.setString(12, drop.getCondition().getJSONString());

				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractTrackedDrop(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void updateTrackedDrop(@NonNull final Drop drop, final Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "UPDATE " + this.getTablePrefix() + "drop SET item = ?, chance = ?, commands = ?, drop_on_natural = ?, drop_on_placed = ?, drop_from_spawner = ?, drop_from_egg = ?, conditions = ? WHERE id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setString(1, SerializeUtil.encodeItem(drop.getItem()));
				preparedStatement.setDouble(2, drop.getChance());
				preparedStatement.setString(3, String.join(";;;", drop.getCommands()));

				// block drop specific
				if (drop instanceof final BlockDrop blockDrop) {
					preparedStatement.setBoolean(4, blockDrop.isDropOnNatural());
					preparedStatement.setBoolean(5, blockDrop.isDropOnPlaced());
					preparedStatement.setBoolean(6, false);
					preparedStatement.setBoolean(7, false);
				}

				// mob drop specific
				if (drop instanceof final MobDrop mobDrop) {
					preparedStatement.setBoolean(4, mobDrop.isDropOnNatural());
					preparedStatement.setBoolean(5, false);
					preparedStatement.setBoolean(6, mobDrop.isDropFromSpawner());
					preparedStatement.setBoolean(7, mobDrop.isDropFromEgg());
				}

				preparedStatement.setString(8, drop.getCondition().getJSONString());
				preparedStatement.setString(9, drop.getId().toString());

				int result = preparedStatement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteTrackedDrop(@NonNull final Drop drop, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "drop WHERE id = ?")) {
				statement.setString(1, drop.getId().toString());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void getTrackedDrops(@NonNull final Callback<List<Drop>> callback) {
		final List<Drop> drops = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "drop")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final Drop drop = extractTrackedDrop(resultSet);
					drops.add(drop);
				}

				callback.accept(null, drops);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void getTrackedDropsByBlock(@NonNull final CompMaterial block, @NonNull final Callback<List<Drop>> callback) {
		final List<Drop> drops = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "drop WHERE block = ?")) {
				statement.setString(1, block.name());

				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final Drop drop = extractTrackedDrop(resultSet);
					drops.add(drop);
				}

				callback.accept(null, drops);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	public void getTrackedDropsByMob(@NonNull final EntityType mob, @NonNull final Callback<List<Drop>> callback) {
		final List<Drop> drops = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "drop WHERE entity = ?")) {
				statement.setString(1, mob.name());

				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final Drop drop = extractTrackedDrop(resultSet);
					drops.add(drop);
				}

				callback.accept(null, drops);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}

	private Drop extractTrackedDrop(final ResultSet resultSet) throws SQLException {
		// shared -> id, type, block, entity, item, chance, commands, drop_on_natural, drop_on_placed, drop_from_spawner, drop_from_egg
		final UUID uuid = UUID.fromString(resultSet.getString("id"));
		final Drop.DropType dropType = Enum.valueOf(Drop.DropType.class, resultSet.getString("type"));
		final boolean dropOnNatural = resultSet.getBoolean("drop_on_natural");
		final ItemStack item = SerializeUtil.decodeItem(resultSet.getString("item"));
		final double chance = resultSet.getDouble("chance");
		final List<String> commands = new ArrayList<>(List.of(resultSet.getString("commands").split(";;;")));

		return switch (dropType) {
			case MOB ->
					new MobDrop(uuid, Enum.valueOf(EntityType.class, resultSet.getString("entity")), item, chance, commands, dropOnNatural, resultSet.getBoolean("drop_from_spawner"), resultSet.getBoolean("drop_from_egg"), DropCondition.decodeCondition(resultSet.getString("conditions")));
			case BLOCK ->
					new BlockDrop(uuid, Enum.valueOf(CompMaterial.class, resultSet.getString("block")), item, chance, dropOnNatural, resultSet.getBoolean("drop_on_placed"), commands, DropCondition.decodeCondition(resultSet.getString("conditions")));
		};
	}

	/*
	===============================================================================================================================
	CUSTOM RECIPES
	===============================================================================================================================
	 */
	public void insertCustomRecipe(@NonNull final CustomRecipe recipe, final Callback<CustomRecipe> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "INSERT INTO " + this.getTablePrefix() + "recipe (name, type, structure) VALUES (?, ?, ?)";
			final String fetchQuery = "SELECT * FROM " + this.getTablePrefix() + "recipe WHERE name = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				final PreparedStatement fetch = connection.prepareStatement(fetchQuery);

				fetch.setString(1, recipe.getId().toLowerCase());

				preparedStatement.setString(1, recipe.getId().toLowerCase());
				preparedStatement.setString(2, recipe.getRecipeType().name());
				preparedStatement.setString(3, recipe.getJSONString());


				preparedStatement.executeUpdate();

				if (callback != null) {
					final ResultSet res = fetch.executeQuery();
					res.next();
					callback.accept(null, extractCustomRecipe(res));
				}

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void deleteCustomRecipe(@NonNull final CustomRecipe recipe, Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM " + this.getTablePrefix() + "recipe WHERE name = ?")) {
				statement.setString(1, recipe.getId().toLowerCase());

				int result = statement.executeUpdate();
				callback.accept(null, result > 0);

			} catch (Exception e) {
				resolveCallback(callback, e);
				e.printStackTrace();
			}
		}));
	}

	public void updateCustomRecipe(@NonNull final CustomRecipe recipe, final Callback<Boolean> callback) {
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			final String query = "UPDATE " + this.getTablePrefix() + "recipe SET structure = ? WHERE name = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

				preparedStatement.setString(1, recipe.getJSONString());
				preparedStatement.setString(2, recipe.getId().toLowerCase());

				int result = preparedStatement.executeUpdate();

				if (callback != null)
					callback.accept(null, result > 0);

			} catch (Exception e) {
				e.printStackTrace();
				resolveCallback(callback, e);
			}
		}));
	}

	public void getCustomRecipes(@NonNull final Callback<List<CustomRecipe>> callback) {
		final List<CustomRecipe> customRecipes = new ArrayList<>();
		this.runAsync(() -> this.databaseConnector.connect(connection -> {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + this.getTablePrefix() + "recipe")) {
				final ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					final CustomRecipe drop = extractCustomRecipe(resultSet);
					customRecipes.add(drop);
				}

				callback.accept(null, customRecipes);
			} catch (Exception e) {
				resolveCallback(callback, e);
			}
		}));
	}


	private CustomRecipe extractCustomRecipe(final ResultSet resultSet) throws SQLException {
		final RecipeType recipeType = Enum.valueOf(RecipeType.class, resultSet.getString("type"));
		if (recipeType ==RecipeType.CRAFTING)
			return CraftingTableRecipe.decode(resultSet.getString("structure"));

		return CraftyCookingRecipe.decode(recipeType, resultSet.getString("structure"));
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
