/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.network.shared.web.wicket.util;

import org.apache.wicket.util.convert.converter.DateConverter;
import org.apache.wicket.util.string.StringValue;

import java.util.Date;
import java.util.Locale;

public class WicketHelpers {

    public static String createPlayerLink(String networkUrl, int playerId) {
        String tmp = normalizeBaseUrl(networkUrl);
        tmp += "wicket/bookmarkable/com.cubeia.backoffice.web.user.UserSummary?userId=" + playerId;
        return tmp;
    }

    private static String normalizeBaseUrl(String networkUrl) {
        if (!networkUrl.endsWith("/")) {
            return networkUrl + "/";
        }
        return networkUrl;
    }

    public static Integer toIntOrNull(StringValue value) {
        if (nullValue(value)) return null;
        return value.toInt();
    }

    private static boolean nullValue(StringValue value) {
        return value == null || value.isNull() || value.isEmpty();
    }

    public static boolean isEmpty(StringValue value) {
        return value == null || value.isEmpty() || value.isNull();
    }

    public static String toStringOrNull(StringValue value) {
        if (nullValue(value)) return null;
        return value.toString();
    }

    public static Date toDateOrNull(StringValue value) {
        if (value.isNull()) return null;
        return new DateConverter().convertToObject(value.toString(), Locale.getDefault());
    }
}
