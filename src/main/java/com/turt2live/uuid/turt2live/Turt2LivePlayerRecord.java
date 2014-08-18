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

package com.turt2live.uuid.turt2live;

import com.turt2live.uuid.PlayerRecord;

import java.util.UUID;

/**
 * Represents a player record from a turt2live UUID service
 *
 * @author turt2live
 */
public class Turt2LivePlayerRecord implements PlayerRecord {

    private UUID uuid;
    private UUID offlineUuid;
    private String name;
    private long expiresOn;
    private long expiresIn;
    private boolean cached;

    /**
     * Constructor for version 1 API
     *
     * @param uuid the uuid, cannot be null
     * @param name the name, cannot be null
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public Turt2LivePlayerRecord(UUID uuid, String name) {
        this(uuid, name, null, -1, -1, false);
    }

    /**
     * Constructor for version 2 API
     *
     * @param uuid        the uuid, cannot be null
     * @param name        the name, cannot be null
     * @param offlineUuid the offline uuid
     * @param expiresIn   the time until this record expires, in seconds
     * @param expiresOn   the time this record expires on as a UNIX millisecond timestamp
     * @param cached      true if this record was cached, false otherwise
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public Turt2LivePlayerRecord(UUID uuid, String name, UUID offlineUuid, long expiresIn, long expiresOn, boolean cached) {
        if (uuid == null || name == null) throw new IllegalArgumentException();

        this.uuid = uuid;
        this.name = name;
        this.offlineUuid = offlineUuid;
        this.expiresIn = expiresIn;
        this.expiresOn = expiresOn;
        this.cached = cached;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public UUID getOfflineUuid() {
        return offlineUuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCached() {
        return cached;
    }

    @Override
    public long getExpirationTime() {
        return expiresOn;
    }

    @Override
    public long getTimeLeft() {
        return expiresIn;
    }
}
