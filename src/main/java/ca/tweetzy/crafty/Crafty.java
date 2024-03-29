package ca.tweetzy.crafty;

import ca.tweetzy.crafty.commands.CraftyCommand;
import ca.tweetzy.crafty.database.DataManager;
import ca.tweetzy.crafty.database.migrations._1_InitialMigration;
import ca.tweetzy.crafty.listener.BlockListener;
import ca.tweetzy.crafty.model.manager.BlockDropManager;
import ca.tweetzy.crafty.settings.Settings;
import ca.tweetzy.crafty.settings.Translations;
import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.command.CommandManager;
import ca.tweetzy.flight.database.DataMigrationManager;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.SQLiteConnector;
import ca.tweetzy.flight.gui.GuiManager;
import ca.tweetzy.flight.utils.Common;

public final class Crafty extends FlightPlugin {

	@SuppressWarnings("FieldCanBeLocal")
	private DatabaseConnector databaseConnector;
	private DataManager dataManager;

	private final CommandManager commandManager = new CommandManager(this);
	private final GuiManager guiManager = new GuiManager(this);

	//======================= MANAGERS =========================
	private final BlockDropManager blockDropManager = new BlockDropManager();
	//==========================================================

	@Override
	protected void onFlight() {
		Settings.init();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getStringOr("<GRADIENT:3dcf50>&LCrafty</GRADIENT:26d5ed> &8»"));

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager,
				new _1_InitialMigration()
		);

		// run migrations for tables
		dataMigrationManager.runMigrations();

		// setup vault

		// gui system
		this.guiManager.init();

		// managers
		this.blockDropManager.load();

		// listeners
		getServer().getPluginManager().registerEvents(new BlockListener(), this);

		// setup commands
		this.commandManager.registerCommandDynamically(new CraftyCommand());
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

	public static BlockDropManager getBlockDropManager() {
		return getInstance().blockDropManager;
	}

	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}

	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}
}
