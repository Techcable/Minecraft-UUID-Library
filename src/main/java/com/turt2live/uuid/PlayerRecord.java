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

import java.util.UUID;

/**
 * Represents a record for a player from the UUID service
 *
 * @author turt2live
 */
public interface PlayerRecord {

    /**
     * Gets the unique ID for this player
     *
     * @return the unique ID, or null if unknown
     */
    public UUID getUuid();

    /**
     * Gets the offline unique ID for this player. This is a hash of the
     * player's name alongside some other characters defined by Mojang.
     *
     * @return the offline-mode UUID, or null if unknown
     */
    public UUID getOfflineUuid();

    /**
     * Gets the name of this player
     *
     * @return the player's name, or null if unknown
     */
    public String getName();

    /**
     * Gets whether or not this record was cached or pulled from Mojang. If true,
     * the record is within the cache time and was retrieved as such.
     *
     * @return true if cached, false otherwise
     */
    public boolean isCached();

    /**
     * Gets UNIX timestamp, in milliseconds, of when this record will expire from the service
     *
     * @return the UNIX timestamp of expiration
     */
    public long getExpirationTime();

    /**
     * Gets the time remaining for this record as reported by the service. This will always
     * return the same value and will not update dynamically.
     *
     * @return the number of seconds until this record expires, as reported from the service
     */
    public long getTimeLeft();
}
