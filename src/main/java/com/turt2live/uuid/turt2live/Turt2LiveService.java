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
import com.turt2live.uuid.ServiceProvider;
import com.turt2live.uuid.utils.HTTPUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

/**
 * Represents a UUID service hosted by turt2live (Travis Ralston).
 *
 * @author turt2live
 */
public abstract class Turt2LiveService implements ServiceProvider {

    /**
     * Converts a UUID to a service-safe UUID
     *
     * @param uuid the uuid to convert, cannot be null
     *
     * @return the service-safe UUID
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public String convertUuid(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");

        return uuid.toString();
    }

    /**
     * Converts a UUID to a service-safe UUID. If the supplied string
     * is not a valid UUID, null is returned.
     *
     * @param uuid the uuid to convert, cannot be null and must be 32 characters long
     *
     * @return the service-safe UUID, or null if invalid
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
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

    /**
     * Gets the URL that this service is using for API lookups
     *
     * @return the service API URL
     */
    public abstract String getConnectionUrl();

    /**
     * Parses a player record from JSON
     *
     * @param json the JSON
     *
     * @return the player record, or null if not parsed
     */
    protected abstract PlayerRecord parsePlayerRecord(String json);

    /**
     * Performs a URL request, returning the content of the URL
     *
     * @param url the URL to connect to, cannot be null
     *
     * @return the URL's returned content, may be null in the event of connection failure
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    protected String doUrlRequest(String url) {
        return HTTPUtils.get(url);
    }

}
