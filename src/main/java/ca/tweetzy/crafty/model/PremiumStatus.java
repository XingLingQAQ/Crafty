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

import lombok.experimental.UtilityClass;

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
}
