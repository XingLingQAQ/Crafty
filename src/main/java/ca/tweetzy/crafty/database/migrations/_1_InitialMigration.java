package ca.tweetzy.crafty.database.migrations;

import ca.tweetzy.flight.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class _1_InitialMigration extends DataMigration {

	public _1_InitialMigration() {
		super(1);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE " + tablePrefix + "tracked_block (" +
					"block VARCHAR(128) NOT NULL," +
					"default_drops BOOLEAN NOT NULL," +
					"blocked_worlds TEXT NOT NULL" +
					")");
		}
	}
}
