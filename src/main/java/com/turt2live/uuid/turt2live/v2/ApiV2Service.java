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
import com.turt2live.uuid.turt2live.Turt2LivePlayerRecord;
import com.turt2live.uuid.turt2live.Turt2LiveService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a service for http://uuid.turt2live.com
 * <p/>
 * This service has a bulk lookup limit of 50 players and will not include
 * duplicates or skipped records in returning sets. Additionally, the player
 * history will include the player's current name. Bulk lookups will return
 * a version 1 compatible player record and not a full version 2 record.
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
        if (json == null) return null;

        JSONObject jsonValue = (JSONObject) JSONValue.parse(json);
        if (jsonValue.containsKey("uuid") && jsonValue.containsKey("name")
                && jsonValue.containsKey("offline-uuid") && jsonValue.containsKey("expires-in")
                && jsonValue.containsKey("expires-on")) {
            String uuidStr = (String) jsonValue.get("uuid");
            String name = (String) jsonValue.get("name");
            String offlineStr = (String) jsonValue.get("offline-uuid");
            Object expiresOn = jsonValue.get("expires-on");
            Object expiresIn = jsonValue.get("expires-in");
            boolean cached = jsonValue.containsKey("source") && ((String) jsonValue.get("source")).equalsIgnoreCase("cache");

            if (name.equals("unknown") || uuidStr.equals("unknown")) return null;
            if (expiresOn == null) expiresOn = "0";
            if (expiresIn == null) expiresIn = "0";

            long expOn, expIn;

            try {
                if (expiresOn instanceof String) expOn = Long.parseLong((String) expiresOn);
                else expOn = (Long) expiresOn;
                expOn *= 1000; // Milliseconds

                if (expiresIn instanceof String) expIn = Long.parseLong((String) expiresIn);
                else expIn = (Long) expiresIn;
            } catch (Exception ignored) {
                return null; // Connection problem or other issue
            }

            UUID uuid = UUID.fromString(uuidStr);
            UUID offlineUuid = UUID.fromString(offlineStr);

            return new Turt2LivePlayerRecord(uuid, name, offlineUuid, expIn, expOn, cached);
        }

        return null;
    }

    @Override
    public PlayerRecord doLookup(UUID uuid) {
        return parsePlayerRecord(doUrlRequest(getConnectionUrl() + "/name/" + convertUuid(uuid)));
    }

    @Override
    public PlayerRecord doLookup(String playerName) {
        if (playerName == null) throw new IllegalArgumentException();
        return parsePlayerRecord(doUrlRequest(getConnectionUrl() + "/uuid/" + playerName));
    }

    @Override
    public String[] getNameHistory(UUID uuid) {
        String response = doUrlRequest(getConnectionUrl() + "/history/" + convertUuid(uuid));
        if (response != null) {
            JSONObject json = (JSONObject) JSONValue.parse(response);

            if (json.containsKey("names")) {
                JSONArray array = (JSONArray) json.get("names");
                String[] names = new String[array.size()];

                int i = 0;
                for (Object o : array) {
                    names[i] = o.toString();
                    i++;
                }

                return names;
            }
        }

        return null;
    }

    @Override
    public List<PlayerRecord> doBulkLookup(UUID... uuids) {
        String list = combine(uuids);
        String response = doUrlRequest(getConnectionUrl() + "/name/list/" + list);

        if (response != null) {
            JSONObject json = (JSONObject) JSONValue.parse(response);

            if (json.containsKey("results")) {
                JSONObject object = (JSONObject) json.get("results");
                List<PlayerRecord> records = new ArrayList<PlayerRecord>();

                for (Object key : object.keySet()) {
                    UUID uuid = UUID.fromString(key.toString());
                    String name = (String) object.get(key);

                    if (uuid == null || name.equals("unknown")) continue;

                    // Note: v2 returns a v1 compatible player record
                    PlayerRecord record = new Turt2LivePlayerRecord(uuid, name);
                    records.add(record);
                }

                return records;
            }
        }

        return null;
    }

    @Override
    public List<PlayerRecord> doBulkLookup(String... playerNames) {
        String list = combine(playerNames);
        String response = doUrlRequest(getConnectionUrl() + "/uuid/list/" + list);

        if (response != null) {
            JSONObject json = (JSONObject) JSONValue.parse(response);

            if (json.containsKey("results")) {
                JSONObject object = (JSONObject) json.get("results");
                List<PlayerRecord> records = new ArrayList<PlayerRecord>();

                for (Object key : object.keySet()) {
                    String name = key.toString();
                    UUID uuid = UUID.fromString((String) object.get(key));

                    if (uuid == null || name.equals("unknown")) continue;

                    PlayerRecord record = new Turt2LivePlayerRecord(uuid, name);
                    records.add(record);
                }

                return records;
            }
        }

        return null;
    }

    private String combine(Object... values) {
        if (values == null || values.length == 0) throw new IllegalArgumentException();

        StringBuilder builder = new StringBuilder();

        for (Object o : values) {
            if (o == null) throw new IllegalArgumentException();

            builder.append(o.toString()).append(";");
        }

        return builder.substring(0, builder.length() - 1);
    }

    @Override
    public PlayerRecord getRandomSample() {
        List<PlayerRecord> records = getRandomSample(1);
        if (records != null && records.size() > 0) return records.get(0);
        return null;
    }

    @Override
    public List<PlayerRecord> getRandomSample(int amount) {
        if (amount <= 0) throw new IllegalArgumentException();

        String response = doUrlRequest(getConnectionUrl() + "/random/" + amount);
        if (response == null || response.length() <= 2) return null;

        JSONArray array = (JSONArray) ((JSONObject) JSONValue.parse(response)).get("results");
        List<PlayerRecord> records = new ArrayList<PlayerRecord>();

        for (Object o : array) {
            PlayerRecord record = parsePlayerRecord(o.toString());

            if (record != null) records.add(record);
        }

        return records;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }
}
