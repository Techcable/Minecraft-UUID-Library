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
 * Represents a simple memory-based player record with no default caching
 * schemes. This simply will only contain a player name and UUID. All other
 * values are their documented defaults.
 *
 * @author turt2live
 */
public class MemoryPlayerRecord implements PlayerRecord {

    private UUID uuid;
    private String name;

    /**
     * Creates a new memory record
     *
     * @param uuid the uuid, cannot be null
     * @param name the name, cannot be null
     */
    public MemoryPlayerRecord(UUID uuid, String name) {
        if (uuid == null || name == null) throw new IllegalArgumentException();
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public UUID getOfflineUuid() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public long getExpirationTime() {
        return -1;
    }

    @Override
    public long getTimeLeft() {
        return -1;
    }
}
