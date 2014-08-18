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

package com.turt2live.uuid.turt2live.v1;

import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.turt2live.Turt2LiveService;

import java.util.List;
import java.util.UUID;

/**
 * Represents a service for http://uuid.turt2live.com/v1
 * <p/>
 * This service has a bulk lookup limit of 50 players and will not include
 * duplicates or skipped records in returning sets. Additionally, the player
 * history will not include the player's current name.
 * <p/>
 * Records returned from this API service are not known to expire or to be
 * cached and instead have default values returned.
 */
public class ApiV1Service implements Turt2LiveService {

    private final String connectionUrl = "http://uuid.turt2live.com/api/v1";
    private final String serviceName = "turt2live v1";

    @Override
    public String convertUuid(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");
        return uuid.toString().replace("-", "");
    }

    @Override
    public UUID convertUuid(String uuid) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");
        if (uuid.length() != 32) return null;

        String dashed = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);

        UUID retUuid = null;
        try {
            retUuid = UUID.fromString(dashed);
        } catch (Exception ignored) {
        }

        return retUuid;
    }

    @Override
    public String getConnectionUrl() {
        return connectionUrl;
    }

    @Override
    public PlayerRecord doLookup(UUID uuid) {
        return null;
    }

    @Override
    public PlayerRecord doLookup(String playerName) {
        return null;
    }

    @Override
    public String[] getNameHistory(UUID uuid) {
        return new String[0];
    }

    @Override
    public List<PlayerRecord> doBulkLookup(UUID... uuids) {
        return null;
    }

    @Override
    public List<PlayerRecord> doBulkLookup(String... playerNames) {
        return null;
    }

    @Override
    public PlayerRecord getRandomSample() {
        return null;
    }

    @Override
    public List<PlayerRecord> getRandomSample(int amount) {
        return null;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }
}
