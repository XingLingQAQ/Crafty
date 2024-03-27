package ca.tweetzy.crafty;

import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.command.CommandManager;
import ca.tweetzy.flight.database.DataMigrationManager;
import ca.tweetzy.flight.database.DatabaseConnector;
import ca.tweetzy.flight.database.SQLiteConnector;
import ca.tweetzy.flight.gui.GuiManager;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.crafty.commands.CraftyCommand;
import ca.tweetzy.crafty.database.DataManager;
import ca.tweetzy.crafty.settings.Settings;
import ca.tweetzy.crafty.settings.Translations;

public final class Crafty extends FlightPlugin {

	@SuppressWarnings("FieldCanBeLocal")
	private DatabaseConnector databaseConnector;
	private DataManager dataManager;

	private final CommandManager commandManager = new CommandManager(this);
	private final GuiManager guiManager = new GuiManager(this);

	@Override
	protected void onFlight() {
		Settings.init();
		Translations.init();

		Common.setPrefix(Settings.PREFIX.getStringOr("&8[&eCrafty&8]"));

		// Set up the database if enabled
		this.databaseConnector = new SQLiteConnector(this);
		this.dataManager = new DataManager(this.databaseConnector, this);

		final DataMigrationManager dataMigrationManager = new DataMigrationManager(this.databaseConnector, this.dataManager);

		// run migrations for tables
		dataMigrationManager.runMigrations();

		// setup vault

		// gui system
		this.guiManager.init();

		// managers

		// listeners

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

	public static GuiManager getGuiManager() {
		return getInstance().guiManager;
	}

	public static DataManager getDataManager() {
		return getInstance().dataManager;
	}
}
