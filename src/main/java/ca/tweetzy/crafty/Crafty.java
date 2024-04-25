package ca.tweetzy.crafty;

import ca.tweetzy.crafty.commands.CraftyCommand;
import ca.tweetzy.crafty.commands.NewCommand;
import ca.tweetzy.crafty.database.DataManager;
import ca.tweetzy.crafty.database.migrations._1_InitialMigration;
import ca.tweetzy.crafty.database.migrations._2_DropsMigration;
import ca.tweetzy.crafty.database.migrations._3_TrackedMobMigration;
import ca.tweetzy.crafty.database.migrations._4_RecipeMigration;
import ca.tweetzy.crafty.listener.BlockListener;
import ca.tweetzy.crafty.listener.EntityListener;
import ca.tweetzy.crafty.listener.PlayerListener;
import ca.tweetzy.crafty.model.manager.BlockDropManager;
import ca.tweetzy.crafty.model.manager.CustomRecipeManager;
import ca.tweetzy.crafty.model.manager.DropManager;
import ca.tweetzy.crafty.model.manager.MobDropManager;
import ca.tweetzy.crafty.settings.Settings;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.command.CommandManager;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.database.DataMigrationManager;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.SQLiteConnector;
import ca.tweetzy.flight.gui.GuiManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public final class Crafty extends FlightPlugin {

	@SuppressWarnings("FieldCanBeLocal")
	private DatabaseConnector databaseConnector;
	private DataManager dataManager;

	private final CommandManager commandManager = new CommandManager(this);
	private final GuiManager guiManager = new GuiManager(this);

	//======================= MANAGERS =========================
	private final BlockDropManager blockDropManager = new BlockDropManager();
	private final MobDropManager mobDropManager = new MobDropManager();
	private final DropManager dropManager = new DropManager();
	private final CustomRecipeManager recipeManager = new CustomRecipeManager();

	//==========================================================

	private NamespacedKey userPlacedBlockKey;
	private NamespacedKey creatureSpawnKey;

	@Override
	protected void onFlight() {
		Settings.init();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getStringOr("<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &8»"));

		// create name spaced key
		this.userPlacedBlockKey = new NamespacedKey(this, "CraftyPlacedBlock");
		this.creatureSpawnKey = new NamespacedKey(this, "CraftySpawnedMob");

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration(),
				new _2_DropsMigration(),
				new _3_TrackedMobMigration(),
				new _4_RecipeMigration()
		);

		// run migrations for tables
		dataMigrationManager.runMigrations();

		// gui system
		this.guiManager.init();

		// managers
		this.blockDropManager.load();
		this.mobDropManager.load();
		this.recipeManager.load();

		// listeners
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
		getServer().getPluginManager().registerEvents(new EntityListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);

		// setup commands
		this.commandManager.registerCommandDynamically(new CraftyCommand()).addSubCommands(new NewCommand());
	}

	@Override
	protected int getBStatsId() {
		return 21430;
	}

	@Override
	protected void onSleep() {
		shutdownDataManager(this.dataManager);
	}

	public static Crafty getInstance() {
		return (Crafty) FlightPlugin.getInstance();
	}

	public static NamespacedKey getPlacedBlockKey() {
		return getInstance().userPlacedBlockKey;
	}

	public static NamespacedKey getCreatureSpawnKey() {
		return getInstance().creatureSpawnKey;
	}

	public static BlockDropManager getBlockDropManager() {
		return getInstance().blockDropManager;
	}

	public static MobDropManager getMobDropManager() {
		return getInstance().mobDropManager;
	}

	public static DropManager getDropManager() {
		return getInstance().dropManager;
	}

	public static CustomRecipeManager getRecipeManager() {
		return getInstance().recipeManager;
	}

	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}

	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}
}
