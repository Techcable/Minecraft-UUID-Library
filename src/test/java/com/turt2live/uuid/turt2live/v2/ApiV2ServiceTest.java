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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ApiV2ServiceTest {

    private static final String PLAYER_NAME = "turt2live";
    private static final UUID PLAYER_UUID = UUID.fromString("c465b154-3c29-4dbf-a7e3-e0869504b8d8");
    private static final String[] EXPECTED_HISTORY = new String[] {"turt2live"};

    private static final UUID BULK_UUID_1 = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");
    private static final UUID BULK_UUID_2 = UUID.fromString("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6");
    private static final String BULK_NAME_1 = "notch";
    private static final String BULK_NAME_2 = "dinnerbone";

    private ApiV2Service service;

    @Before
    public void setup() {
        service = new ApiV2Service();
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_get_by_name_throw_exception_on_null_input() {
        service.doLookup((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_get_by_uuid_throw_exception_on_null_input() {
        service.doLookup((UUID) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_get_history_throw_exception_on_null_input() {
        service.getNameHistory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_bulk_name_lookup_throw_expcetion_on_null_input() {
        service.doBulkLookup((String[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_bulk_name_lookup_throw_expcetion_on_partial_null_input() {
        service.doBulkLookup("not null", null, "still not null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_bulk_uuid_lookup_throw_expcetion_on_null_input() {
        service.doBulkLookup((UUID[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_bulk_uuid_lookup_throw_expcetion_on_partial_null_input() {
        service.doBulkLookup(UUID.randomUUID(), null, UUID.randomUUID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_random_throw_exception_on_zero() {
        service.getRandomSample(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_random_throw_exception_on_negative() {
        service.getRandomSample(-10);
    }

    @Test
    public void does_get_by_name_work() {
        PlayerRecord record = service.doLookup(PLAYER_NAME);

        assertNotNull(record);
        assertEquals(PLAYER_NAME, record.getName());
        assertEquals(PLAYER_UUID, record.getUuid());
        assertApiV2Compliant(record);
    }

    @Test
    public void does_get_by_uuid_work() {
        PlayerRecord record = service.doLookup(PLAYER_UUID);

        assertNotNull(record);
        assertEquals(PLAYER_NAME, record.getName());
        assertEquals(PLAYER_UUID, record.getUuid());
        assertApiV2Compliant(record);
    }

    @Test
    public void does_get_history_work() {
        String[] history = service.getNameHistory(PLAYER_UUID);

        assertNotNull(history);
        assertArrayEquals(EXPECTED_HISTORY, history);
    }

    @Test
    public void does_get_bulk_names_work() {
        List<PlayerRecord> records = service.doBulkLookup(BULK_NAME_1, BULK_NAME_2);

        assertNotNull(records);
        assertEquals(2, records.size());
        assertContains(records, BULK_UUID_1, BULK_NAME_1);
        assertContains(records, BULK_UUID_2, BULK_NAME_2);
    }

    @Test
    public void does_get_bulk_names_ignore_duplicates() {
        List<PlayerRecord> records = service.doBulkLookup(BULK_NAME_1, BULK_NAME_1);

        assertNotNull(records);
        assertEquals(1, records.size());
        assertContains(records, BULK_UUID_1, BULK_NAME_1);
    }

    @Test
    public void does_get_bulk_uuids_work() {
        List<PlayerRecord> records = service.doBulkLookup(BULK_UUID_1, BULK_UUID_2);

        assertNotNull(records);
        assertEquals(2, records.size());
        assertContains(records, BULK_UUID_1, BULK_NAME_1);
        assertContains(records, BULK_UUID_2, BULK_NAME_2);
    }

    @Test
    public void does_get_bulk_uuids_ignore_duplicates() {
        List<PlayerRecord> records = service.doBulkLookup(BULK_UUID_1, BULK_UUID_1);

        assertNotNull(records);
        assertEquals(1, records.size());
        assertContains(records, BULK_UUID_1, BULK_NAME_1);
    }

    @Test
    public void does_random_return_one_record() {
        PlayerRecord record = service.getRandomSample();

        assertNotNull(record);
        assertApiV2Compliant(record);
    }

    @Test
    public void does_random_return_multiple_records() {
        List<PlayerRecord> records = service.getRandomSample(10);

        assertNotNull(records);
        assertEquals(10, records.size());

        for (PlayerRecord record : records) {
            assertApiV2Compliant(record);
        }
    }

    private static void assertContains(List<PlayerRecord> records, UUID uuid, String name) {
        assertNotNull(records);
        assertNotNull(uuid);
        assertNotNull(name);

        for (PlayerRecord record : records) {
            assertNotNull(record);
            assertNotNull(record.getName());
            assertNotNull(record.getUuid());
            assertApiV1Compliant(record); // Yes, v2 returns a v1 compliant record

            if (record.getName().equals(name)) {
                assertEquals(uuid, record.getUuid());
                return;
            }
        }

        fail("Record not found: [uuid=" + uuid.toString() + ", name=" + name + "]");
    }

    private static void assertApiV1Compliant(PlayerRecord record) {
        assertNotNull(record);
        assertNull(record.getOfflineUuid());
        assertEquals(-1, record.getExpirationTime());
        assertEquals(-1, record.getTimeLeft());
        assertFalse(record.isCached());
    }

    private static void assertApiV2Compliant(PlayerRecord record) {
        assertNotNull(record);
        assertNotNull(record.getOfflineUuid());
        //assertTrue(record.getExpirationTime() > 0); // Can't be asserted: expired records
        //assertTrue(record.getTimeLeft() > 0); // Can't be asserted: expired records
        //assertFalse(record.isCached()); // Can't be asserted: varies
    }

}
