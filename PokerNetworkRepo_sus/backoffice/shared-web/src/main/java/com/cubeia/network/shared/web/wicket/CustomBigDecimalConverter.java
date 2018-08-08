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
package com.cubeia.network.shared.web.wicket;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.wicket.util.convert.converter.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBigDecimalConverter extends BigDecimalConverter {

	private static final long serialVersionUID = 1L;
	
	static Logger log = LoggerFactory.getLogger(CustomBigDecimalConverter.class);
	
	@Override
	public BigDecimal convertToObject(String value, Locale locale) {
		value = value.replaceAll(",", ".");
		return new BigDecimal(value);
	}
	
	@Override
	public String convertToString(BigDecimal value, Locale locale) {
		return value.toPlainString();
	}

}
