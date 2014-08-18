/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.uuid;

import java.util.List;
import java.util.UUID;

/**
 * Represents a service provider for collecting UUIDs or other information
 *
 * @author turt2live
 */
public interface ServiceProvider {

    /**
     * Performs a lookup operation on a UUID
     *
     * @param uuid the UUID to lookup, cannot be null
     *
     * @return the player record returned
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public PlayerRecord doLookup(UUID uuid);

    /**
     * Performs a lookup operation on a player name
     *
     * @param playerName the player name to lookup, cannot be null
     *
     * @return the player record returned
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public PlayerRecord doLookup(String playerName);

    /**
     * Gets a player's name history from their UUID
     *
     * @param uuid the UUID to lookup, cannot be null
     *
     * @return the player's past names
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public String[] getNameHistory(UUID uuid);

    /**
     * Performs a bulk lookup operation on the UUIDs supplied. The returned set may not
     * contain duplicates or skipped values. Please see the implementation's service documentation
     * for information.
     *
     * @param uuids the uuids to lookup, no element can be null
     *
     * @return a list of player records
     *
     * @throws java.lang.IllegalArgumentException thrown for null values
     */
    public List<PlayerRecord> doBulkLookup(UUID... uuids);

    /**
     * Performs a bulk lookup operation on the player names supplied. The returned set may not
     * contain duplicates or skipped values. Please see the implementation's service documentation
     * for information.
     *
     * @param playerNames the player names to lookup, no element can be null
     *
     * @return the list of player records
     *
     * @throws java.lang.IllegalArgumentException thrown for null values
     */
    public List<PlayerRecord> doBulkLookup(String... playerNames);

    /**
     * Gets a single, possibly expired, player record from the service
     *
     * @return the player record
     */
    public PlayerRecord getRandomSample();

    /**
     * Gets a random sample of player records from the service. They may be expired
     * and the set returned may not match the amount requested. See the implementation's
     * documentation for more information.
     *
     * @param amount the amount of player records to fetch, cannot be negative or zero
     *
     * @return the player records
     *
     * @throws java.lang.IllegalArgumentException thrown for values less than or equal to zero
     */
    public List<PlayerRecord> getRandomSample(int amount);

    /**
     * Gets the name of the service
     *
     * @return the service's name
     */
    public String getServiceName();
}
