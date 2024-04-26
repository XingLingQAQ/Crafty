package ca.tweetzy.crafty.database.migrations;

import ca.tweetzy.flight.database.DataMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class _4_RecipeMigration extends DataMigration {

	public _4_RecipeMigration() {
		super(4);
	}

	@Override
	public void migrate(Connection connection, String tablePrefix) throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE " + tablePrefix + "recipe (" +
					"name VARCHAR(256) NOT NULL PRIMARY KEY," +
					"type VARCHAR(32) NOT NULL," +
					"structure TEXT NOT NULL" +
					")");
		}
	}
}
