package ca.tweetzy.crafty.database.migrations;

import ca.tweetzy.flight.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class _3_TrackedMobMigration extends DataMigration {

	public _3_TrackedMobMigration() {
		super(3);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE " + tablePrefix + "tracked_mob (" +
					"mob VARCHAR(128) NOT NULL," +
					"default_drops BOOLEAN NOT NULL," +
					"blocked_worlds TEXT NOT NULL" +
					")");
		}
	}
}
