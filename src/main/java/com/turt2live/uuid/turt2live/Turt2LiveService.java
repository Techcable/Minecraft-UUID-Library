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

import com.turt2live.uuid.ServiceProvider;

import java.util.UUID;

/**
 * Represents a UUID service hosted by turt2live (Travis Ralston).
 *
 * @author turt2live
 */
public interface Turt2LiveService extends ServiceProvider {

    /**
     * Converts a UUID to a service-safe UUID
     *
     * @param uuid the uuid to convert, cannot be null
     *
     * @return the service-safe UUID
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public String convertUuid(UUID uuid);

    /**
     * Converts a UUID to a service-safe UUID. If the supplied string
     * is not a valid UUID, null is returned.
     *
     * @param uuid the uuid to convert, cannot be null
     *
     * @return the service-safe UUID, or null if invalid
     *
     * @throws java.lang.IllegalArgumentException thrown for null arguments
     */
    public UUID convertUuid(String uuid);

    /**
     * Gets the URL that this service is using for API lookups
     *
     * @return the service API URL
     */
    public String getConnectionUrl();

}
