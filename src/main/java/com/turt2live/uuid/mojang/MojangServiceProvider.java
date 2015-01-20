package com.turt2live.uuid.mojang;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.ServiceProvider;
import com.turt2live.uuid.utils.HTTPUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MojangServiceProvider {
    
    public static final String BASE_URL = "https://api.mojang.com/users/profiles/";
    
    public PlayerRecord doLookup(UUID uuid) {
        Object rawResponse = HTTPUtils.getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + toString(uuid));
        if (rawResponse == null || !(rawResponse instanceof JSONObject)) return null;
        JSONObject response = (JSONObject) rawResponse;
        if (!response.containsKey("name")) return null;
        UUID id = response.containsKey("id") ? toUUID(response.get("id").toString()) : uuid;
        String name = response.get("name").toString();
        return new MojangPlayerRecord(id, name);
    }

    public PlayerRecord doLookup(String playerName) {
        Object rawResponse = HTTPUtils.getJson(BASE_URL + "minecraft/" + playerName);
        if (rawResponse == null || !(rawResponse instanceof JSONObject)) return null;
        JSONObject response = (JSONObject) rawResponse;
        if (!response.containsKey("id")) return null;
        UUID id = toUUID(response.get("id").toString());
        String name = response.containsKey("name") ? response.get("name").toString() : playerName;
        return new MojangPlayerRecord(id, name);
    }

    public String[] getNameHistory(UUID uuid) {
        Object rawResponse = HTTPUtils.getJson(BASE_URL + toString(uuid) + "/names");
        if (rawResponse == null || !(rawResponse instanceof JSONArray)) return null;
        JSONArray response = (JSONArray) rawResponse;
        String[] nameHistory = new String[response.size()];
        for (int i = 0; i < response.size(); i++) {
            Object rawRecord = response.get(i);
            if (rawRecord == null || !(rawRecord instanceof JSONObject)) continue;
            JSONObject record = (JSONObject) rawRecord;
            if (!record.containsKey("name")) return null;
            String name = record.get("name").toString();
            nameHistory[i] = name;
        }
        return nameHistory;
    }

    //Mojang needs to make this better
    public List<PlayerRecord> doBulkLookup(UUID... uuids) {
        List<PlayerRecord> records = new ArrayList<>();
        for (UUID id : uuids) {
            PlayerRecord record = doLookup(id);
            if (record != null) records.add(record);
        }
        return records;
    }

    public List<PlayerRecord> doBulkLookup(String... playerNames) {
        if (playerNames.length > 100) {
            throw new UnsupportedOperationException("Can't have more than 100 uuids in a mojang request");
        }
        JSONArray request = new JSONArray();
        for (String name : playerNames) {
            request.add(name);
        }
        Object rawResponse = HTTPUtils.postJson("https://api.mojang.com/profiles/minecraft", request);
        if (rawResponse == null || !(rawResponse instanceof JSONArray)) return null;
        JSONArray response = (JSONArray) rawResponse;
        List<PlayerRecord> players = new ArrayList<>();
        for (Object rawPlayer : response) {
            if (!(rawPlayer instanceof JSONObject)) continue;
            JSONObject jsonPlayer = (JSONObject) rawPlayer;
            if (!jsonPlayer.containsKey("id") || !(jsonPlayer.containsKey("name"))) continue;
            UUID id = toUUID(jsonPlayer.get("id").toString());
            String name = jsonPlayer.get("name").toString();
            PlayerRecord player = new MojangPlayerRecord(id, name);
            players.add(player);
        }
        return players;
    }

    public PlayerRecord getRandomSample() {
        return null;
    }

    public List<PlayerRecord> getRandomSample(int amount) {
        return null;
    }

    public String getServiceName() {
        return "Mojang Public API";
    }
    
    public static UUID toUUID(String raw) {
        String dashed;
        if (raw.length() == 32) {
            dashed = raw.substring(0, 8) + "-" + raw.substring(8, 12) + "-" + raw.substring(12, 16) + "-" + raw.substring(16, 20) + "-" + raw.substring(20, 32);
        } else {
            dashed = raw;
        }
        return UUID.fromString(raw);
    }
    
    public static String toString(UUID id) {
        String result = id.toString();
        result = result.replace("-", "");
        return result;
    }
    
    /*
    This is for eventual support for more than 100 names in doBulkLookup
    public static String[][] split(String[] original, int maxSize) {
        String[][] split = new String[][ceil(original.size / maxSize)];
        int lastIndex = 0;
        for (int i = 0; i < original.length; i++) {
            int from = lastIndex;
            int to = lastIndex + 100;
            if (to >= original.size) to = original.size - 1;
            split[i] = Arrays.copyOfRange(original, from, to);
        }
        return split;
    }
    
    public static int ceil(double d) {
        return (int) Math.ceil(d);
    }
    */
}