package com.turt2live.uuid.mojang;

import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.utils.HTTPUtils.RateLimitedException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MojangServiceTest {
    private static final String PLAYER_NAME = "Techcable";
    private static final UUID PLAYER_UUID = UUID.fromString("613517d7-31bb-47bd-bc33-7870d4f3adc5");
    private static final String[] PLAYER_HISTORY = new String[] {"Techcable"};

    private static final UUID UUID_1 = UUID.fromString("613517d7-31bb-47bd-bc33-7870d4f3adc5");
    private static final UUID UUID_2 = UUID.fromString("3bbd0abd-1999-4a4d-b357-cb6203f5e956");
    private static final UUID UUID_3 = UUID.fromString("c465b154-3c29-4dbf-a7e3-e0869504b8d8");
    private static final String NAME_1 = "Techcable";
    private static final String NAME_2 = "Dragonslayer293";
    private static final String NAME_3 = "turt2live";
    
    private MojangServiceProvider service;

    @Before
    public void setup() {
        service = new MojangServiceProvider();
    }
    
    @Test
    public void getByNameTest() {
        PlayerRecord record;
        try {
            record = service.doLookup(PLAYER_NAME);
        } catch (RateLimitedException ex) {
            return;
        }
        
        assertNotNull(record);
        assertEquals(PLAYER_NAME, record.getName());
        assertEquals(PLAYER_UUID, record.getUuid());
    }
    
    @Test
    public void getByUUIDTest() {
        PlayerRecord record = service.doLookup(PLAYER_UUID);
        assertNotNull(record);
        assertEquals(PLAYER_NAME, record.getName());
        assertEquals(PLAYER_UUID, record.getUuid());
    }
    
    @Test
    public void getHistoryTest() {
        String[] history = service.getNameHistory(PLAYER_UUID);
        
        assertNotNull(history);
        assertArrayEquals(PLAYER_HISTORY, history);
    }
    
    @Test
    public void getBulkNameTest() {
        List<PlayerRecord> records = service.doBulkLookup(NAME_1, NAME_2, NAME_3);
        
        assertNotNull(records);
        assertEquals(2, records.size());
        assertContains(records, UUID_1, NAME_1);
        assertContains(records, UUID_2, NAME_2);
        assertContains(records, UUID_3, NAME_3);
    }
    
    @Test
    public void getBulkUUIDTest() {
        List<PlayerRecord> records;
        try {
            records = service.doBulkLookup(UUID_1, UUID_2, UUID_3);
        } catch (RateLimitedException ex) {
            return;
        }
        
        assertNotNull(records);
        assertEquals(2, records.size());
        assertContains(records, UUID_1, NAME_1);
        assertContains(records, UUID_2, NAME_2);
        assertContains(records, UUID_3, NAME_3);
    }
    
    private static void assertContains(List<PlayerRecord> records, UUID uuid, String name) {
        assertNotNull(records);
        assertNotNull(uuid);
        assertNotNull(name);

        for (PlayerRecord record : records) {
            assertNotNull(record);
            assertNotNull(record.getName());
            assertNotNull(record.getUuid());

            if (record.getName().equals(name)) {
                assertEquals(uuid, record.getUuid());
                return;
            }
        }

        fail("Record not found: [uuid=" + uuid.toString() + ", name=" + name + "]");
    }
}