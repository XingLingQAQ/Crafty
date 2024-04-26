/*
 * Auction House
 * Copyright 2018-2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.crafty.model;

import ca.tweetzy.crafty.Crafty;
import ca.tweetzy.flight.utils.Common;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@UtilityClass
public final class PremiumStatus {

	String TIMESTAMP = "%%__TIMESTAMP__%%";
	String USER = "%%__USER__%%";
	String USERNAME = "%%__USERNAME__%%";
	String POLYMART = "%%__POLYMART__%%";

	String RESOURCE = "%%__RESOURCE__%%";
	String NONCE = "%%__NONCE__%%";

	public boolean isUnlicensed() {
		return USER.startsWith("%%__USE") && USER.endsWith("R__%%");
	}

	public boolean isPolymartDownload() {
		return POLYMART.equalsIgnoreCase("1");
	}

	public boolean isSpigot() {
		return !isUnlicensed() && !isPolymartDownload();
	}

	public void thank() {
		if (!isSpigot()) {
			Common.tell(Bukkit.getConsoleSender(), "&cCannot detect user-id :( Please download from Spigot");
		}

		Bukkit.getServer().getScheduler().runTaskAsynchronously(Crafty.getInstance(), () -> {
			try {
				URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getAuthor&id=" + USER);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();

				//Getting the response code
				int responseCode = conn.getResponseCode();

				if (responseCode != 200) {
					return;
				}

				String inline = "";
				Scanner scanner = new Scanner(url.openStream());

				//Write all the JSON data into a string using a scanner
				while (scanner.hasNext()) {
					inline += scanner.nextLine();
				}

				//Close the scanner
				scanner.close();

				//Using the JSON simple library parse the string into a json object
				final JsonObject object = JsonParser.parseString(inline).getAsJsonObject();
				final String username = object.get("username").getAsString();

				Common.tell(Bukkit.getConsoleSender(), String.format("&aHi %s&f, &athank you for purchasing the plugin. I appreciate it &6<3", username));

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
}
