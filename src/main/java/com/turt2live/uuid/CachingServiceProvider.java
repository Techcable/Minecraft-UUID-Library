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

import java.util.*;

/**
 * Represents a service provider that caches it's results in memory for the duration
 * specified (default is 1 hour or until the service's expiration time is reached, if
 * any).
 *
 * @author turt2live
 */
public class CachingServiceProvider implements ServiceProvider {

    private class CachedRecord {

        private long expires;
        private PlayerRecord record;

        CachedRecord(PlayerRecord record, long expires) {
            this.record = record;
            this.expires = expires;
        }

        public boolean isExpired() {
            return expires > System.currentTimeMillis();
        }

        public PlayerRecord getRecord() {
            return record;
        }
    }

    private Map<String, CachedRecord> byName = new HashMap<>();
    private Map<UUID, CachedRecord> byUuid = new HashMap<>();
    private ServiceProvider serviceProvider;
    private long cacheTimeMax = 1 * 60 * 60 * 1000; // 60 minutes

    /**
     * Creates a new caching service provider using the supplied service
     * provider. This will default to use a 1 hour cache time, or if the
     * service provides an expiration time, it will use that.
     *
     * @param other the other service provider to use, cannot be null
     */
    public CachingServiceProvider(ServiceProvider other) {
        this(other, 1 * 60 * 60); // 1 hour
    }

    /**
     * Creates a new caching service provider using the supplied service
     * provider. This will use the supplied maximum cache time to determine
     * when records expire from memory. If the serivce provider provides an
     * expiration time, this will use that instead of the supplied time.
     *
     * @param other            the other service provider to use, cannot be null
     * @param maximumCacheTime the maximum cache time, in seconds. Cannot be negative or zero.
     */
    public CachingServiceProvider(ServiceProvider other, long maximumCacheTime) {
        if (other == null || maximumCacheTime <= 0) throw new IllegalArgumentException();

        this.serviceProvider = other;
        this.cacheTimeMax = maximumCacheTime * 1000;
    }

    /**
     * Pre-sets the cache to the specified values. This uses the default
     * cache time defined in this provider for the duration.
     *
     * @param playerNames the player names to cache, cannot be null
     */
    public void seedLoad(Map<UUID, String> playerNames) {
        seedLoad(playerNames, cacheTimeMax);
    }

    /**
     * Pre-sets the cache to the specified values. This uses the supplied
     * cache time (in seconds) for the expiration.
     *
     * @param playerNames the player names to cache, cannot be null
     * @param cacheTime   the cache time, in seconds. Must be positive and non-zero
     */
    public void seedLoad(Map<UUID, String> playerNames, long cacheTime) {
        if (playerNames == null || cacheTime <= 0) throw new IllegalArgumentException();

        for (Map.Entry<UUID, String> record : playerNames.entrySet()) {
            if (record.getKey() == null || record.getValue() == null) continue;
            PlayerRecord entry = new MemoryPlayerRecord(record.getKey(), record.getValue());
            cache(entry, cacheTime);
        }
    }

    private PlayerRecord checkRecord(UUID uuid) {
        CachedRecord existing = byUuid.get(uuid);
        if (existing == null || existing.isExpired()) {
            if (existing != null) {
                byUuid.remove(uuid);
                byName.remove(existing.getRecord().getName().toLowerCase());
            }
            return null;
        }
        return existing.getRecord();
    }

    private PlayerRecord checkRecord(String name) {
        CachedRecord existing = byName.get(name.toLowerCase());
        if (existing == null || existing.isExpired()) {
            if (existing != null) {
                byUuid.remove(existing.getRecord().getUuid());
                byName.remove(name.toLowerCase());
            }
            return null;
        }
        return existing.getRecord();
    }

    private void cache(PlayerRecord record) {
        cache(record, record.getExpirationTime() <= 0 ? cacheTimeMax : record.getExpirationTime());
    }

    private void cache(PlayerRecord record, long time) {
        CachedRecord cachedRecord = new CachedRecord(record, time);
        byName.put(record.getName(), cachedRecord);
        byUuid.put(record.getUuid(), cachedRecord);
    }

    @Override
    public PlayerRecord doLookup(UUID uuid) {
        return doBulkLookup(uuid).get(0);
    }

    @Override
    public PlayerRecord doLookup(String playerName) {
        return doBulkLookup(playerName).get(0);
    }

    @Override
    public String[] getNameHistory(UUID uuid) {
        return serviceProvider.getNameHistory(uuid);
    }

    @Override
    public List<PlayerRecord> doBulkLookup(UUID... uuids) {
        if (uuids == null) throw new IllegalArgumentException();
        List<PlayerRecord> records = new ArrayList<>();

        for (UUID uuid : uuids) {
            if (uuid == null) throw new IllegalArgumentException();
            PlayerRecord existing = checkRecord(uuid);
            if (existing != null) records.add(existing);
            else {
                PlayerRecord record = serviceProvider.doLookup(uuid);
                if (record != null) {
                    records.add(record);
                    cache(record);
                }
            }
        }

        return records;
    }

    @Override
    public List<PlayerRecord> doBulkLookup(String... playerNames) {
        if (playerNames == null) throw new IllegalArgumentException();
        List<PlayerRecord> records = new ArrayList<>();

        for (String playerName : playerNames) {
            if (playerName == null) throw new IllegalArgumentException();
            PlayerRecord existing = checkRecord(playerName);
            if (existing != null) records.add(existing);
            else {
                PlayerRecord record = serviceProvider.doLookup(playerName);
                if (record != null) {
                    records.add(record);
                    cache(record);
                }
            }
        }

        return records;
    }

    @Override
    public PlayerRecord getRandomSample() {
        return getRandomSample(1).get(0);
    }

    @Override
    public List<PlayerRecord> getRandomSample(int amount) {
        if (amount <= 0) throw new IllegalArgumentException();
        List<PlayerRecord> records = new ArrayList<>();

        Random random = new Random();
        while (records.size() < amount && byUuid.size() > 0 && records.size() < byUuid.size()) {
            Set<UUID> keys = byUuid.keySet();
            UUID randKey = keys.toArray(new UUID[keys.size()])[random.nextInt(keys.size())];
            PlayerRecord randRecord = checkRecord(randKey);
            if (randRecord != null && !contains(records, randRecord)) records.add(randRecord);
        }
        if (records.size() != amount) {
            List<PlayerRecord> pulled = serviceProvider.getRandomSample(amount - records.size());
            records.addAll(pulled);

            for (PlayerRecord record : pulled) cache(record);
        }

        return records;
    }

    private boolean contains(List<PlayerRecord> records, PlayerRecord record) {
        for (PlayerRecord record1 : records) {
            if (record1.getUuid().equals(record.getUuid())) return true;
        }
        return false;
    }

    @Override
    public String getServiceName() {
        return serviceProvider.getServiceName();
    }
}
