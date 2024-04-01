package ca.tweetzy.crafty.database.migrations;

import ca.tweetzy.flight.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class _2_DropsMigration extends DataMigration {

	public _2_DropsMigration() {
		super(2);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {

			statement.execute("CREATE TABLE " + tablePrefix + "drop (" +
					"id VARCHAR(48) PRIMARY KEY NOT NULL," +
					"type VARCHAR(16) NOT NULL," +
					"block VARCHAR(128) NULL," +
					"entity VARCHAR(128) NULL," +
					"item TEXT NOT NULL," +
					"chance DOUBLE NOT NULL," +
					"commands TEXT NOT NULL," +
					"drop_on_natural BOOLEAN NOT NULL," +
					"drop_on_placed BOOLEAN NOT NULL," +
					"drop_from_spawner BOOLEAN NOT NULL," +
					"drop_from_egg BOOLEAN NOT NULL," +
					"conditions TEXT NOT NULL DEFAULT '{}'" +
					")");
		}
	}
}
