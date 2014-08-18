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

package com.turt2live.uuid.turt2live.v2;

import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.turt2live.Turt2LiveService;

import java.util.List;
import java.util.UUID;

/**
 * Represents a service for http://uuid.turt2live.com/v1
 * <p/>
 * This service has a bulk lookup limit of 50 players and will not include
 * duplicates or skipped records in returning sets. Additionally, the player
 * history will include the player's current name.
 */
public class ApiV2Service extends Turt2LiveService {

    private final String connectionUrl = "http://uuid.turt2live.com/api/v2";
    private final String serviceName = "turt2live v2";

    @Override
    public String getConnectionUrl() {
        return connectionUrl;
    }

    @Override
    protected PlayerRecord parsePlayerRecord(String json) {
        return null;
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
