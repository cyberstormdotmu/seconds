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
package com.cubeia.events.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RuleCalculatorTest {

	Map<String, String> attr;
	
	RuleCalculator calc = new RuleCalculator();
	
	@Before
	public void setup() {
		attr = new HashMap<String, String>();
	}
	
	@Test
	public void testTypeChecks() {
		assertThat(calc.isNumeric("0"), is(true));
		assertThat(calc.isNumeric("1"), is(true));
		assertThat(calc.isNumeric("2"), is(true));
		assertThat(calc.isNumeric("134564"), is(true));
		assertThat(calc.isNumeric("2L"), is(false));
		assertThat(calc.isNumeric("-2"), is(false));
		assertThat(calc.isNumeric("{2}"), is(false));
		
		assertThat(calc.isString("'val'"), is(true));
		assertThat(calc.isString("'2'"), is(true));
		assertThat(calc.isString("val"), is(false));
		assertThat(calc.isString("2"), is(false));
		assertThat(calc.isString("'some thing with > 3 spaces'"), is(true));
		
		assertThat(calc.isVariable("{val}"), is(true));
		assertThat(calc.isVariable("{2}"), is(true));
		assertThat(calc.isVariable("val"), is(false));
		assertThat(calc.isVariable("'val'"), is(false));
		
	}

    @Test
    public void testDecimalTypeCheck() {
        assertThat(calc.isNumeric("2.23"),is(true));
        assertThat(calc.isNumeric("-23.3"),is(false));
        assertThat(calc.isNumeric("0.000003"),is(true));
    }

    @Test
    public void testSplitRule() {
        assertThat(calc.splitRule("{key} != 1 & {key2}=4"), is(new String[]{"{key}","!=","1","&","{key2}","=","4"}));
        assertThat(calc.splitRule("{key } > 1"), is(new String[]{"{key}",">","1"}));
        assertThat(calc.splitRule("{key} < 1 | {key } = 'test key'"), is(new String[]{"{key}","<","1","|","{key}","=","'test key'"}));
        assertThat(calc.splitRule("{key } = 'test name'"), is(new String[]{"{key}","=","'test name'"}));
    }

	@Test
	public void testSimpleSingleNumericChecks() {
		attr.put("key", "5");
		assertThat(calc.matches("{key} > 1", attr), is(true));
		assertThat(calc.matches("{key} > 5", attr), is(false));
		assertThat(calc.matches("{key} > 6", attr), is(false));
		
		assertThat(calc.matches("{key} < 6", attr), is(true));
		assertThat(calc.matches("{key} < 5", attr), is(false));
		assertThat(calc.matches("{key} < 4", attr), is(false));
		
		assertThat(calc.matches("{key} >= 1", attr), is(true));
		assertThat(calc.matches("{key} >= 5", attr), is(true));
		assertThat(calc.matches("{key} >= 6", attr), is(false));
		
		assertThat(calc.matches("{key} <= 6", attr), is(true));
		assertThat(calc.matches("{key} <= 5", attr), is(true));
		assertThat(calc.matches("{key} <= 4", attr), is(false));
		
		assertThat(calc.matches("{key} = 6", attr), is(false));
		assertThat(calc.matches("{key} = 5", attr), is(true));
		assertThat(calc.matches("{key} = 4", attr), is(false));

	}

    @Test
    public void testSimpleSingleNumericDecimalChecks() {
        attr.put("key", "5.5");
        assertThat(calc.matches("{key} > 1", attr), is(true));
        assertThat(calc.matches("{key} > 5", attr), is(true));
        assertThat(calc.matches("{key} > 6", attr), is(false));

        assertThat(calc.matches("{key} < 6", attr), is(true));
        assertThat(calc.matches("{key} < 5", attr), is(false));
        assertThat(calc.matches("{key} < 4", attr), is(false));

        assertThat(calc.matches("{key} >= 1", attr), is(true));
        assertThat(calc.matches("{key} >= 5.5", attr), is(true));
        assertThat(calc.matches("{key} >= 5.6", attr), is(false));

        assertThat(calc.matches("{key} <= 5.6", attr), is(true));
        assertThat(calc.matches("{key} <= 5.5", attr), is(true));
        assertThat(calc.matches("{key} <= 4", attr), is(false));

        assertThat(calc.matches("{key} = 5", attr), is(false));
        assertThat(calc.matches("{key} = 5.5", attr), is(true));
        assertThat(calc.matches("{key} = 4", attr), is(false));

    }
	
	@Test
	public void testVariableChecks() {
		attr.put("key", "5");
		assertThat(calc.matches("{key} > 1", attr), is(true));
		assertThat(calc.matches("5 > 2", attr), is(true));
		assertThat(calc.matches("{key} > {value}", value("3")), is(true));
	}
	
	@Test(expected=RuleException.class)
	public void testErrorOnInconsistencies() {
		attr.put("key", "5");
		calc.matches("'key' = 1", attr); // String and integer
	}
	
	@Test
	public void testVariableSingleNumericChecks() {
		attr.put("key", "5");
		assertThat(calc.matches("{key} > {value}", value("1")), is(true));
		assertThat(calc.matches("{key} > {value}", value("5")), is(false));
		assertThat(calc.matches("{key} > {value}", value("6")), is(false));
		
		assertThat(calc.matches("{key} < {value}", value("6")), is(true));
		assertThat(calc.matches("{key} < {value}", value("5")), is(false));
		assertThat(calc.matches("{key} < {value}", value("4")), is(false));
		
		assertThat(calc.matches("{key} >= {value}", value("1")), is(true));
		assertThat(calc.matches("{key} >= {value}", value("5")), is(true));
		assertThat(calc.matches("{key} >= {value}", value("6")), is(false));
		
		assertThat(calc.matches("{key} <= {value}", value("6")), is(true));
		assertThat(calc.matches("{key} <= {value}", value("5")), is(true));
		assertThat(calc.matches("{key} <= {value}", value("4")), is(false));
		
		assertThat(calc.matches("{key} = {value}", value("6")), is(false));
		assertThat(calc.matches("{key} = {value}", value("5")), is(true));
		assertThat(calc.matches("{key} = {value}", value("4")), is(false));
	}

    @Test
    public void testVariableSingleNumericDecimalChecks() {
        attr.put("key", "5.5");
        assertThat(calc.matches("{key} > {value}", value("1.5")), is(true));
        assertThat(calc.matches("{key} > {value}", value("5.5")), is(false));
        assertThat(calc.matches("{key} > {value}", value("6.5")), is(false));

        assertThat(calc.matches("{key} < {value}", value("6.5")), is(true));
        assertThat(calc.matches("{key} < {value}", value("5.5")), is(false));
        assertThat(calc.matches("{key} < {value}", value("4.5")), is(false));

        assertThat(calc.matches("{key} >= {value}", value("1.5")), is(true));
        assertThat(calc.matches("{key} >= {value}", value("5.5")), is(true));
        assertThat(calc.matches("{key} >= {value}", value("6.5")), is(false));

        assertThat(calc.matches("{key} <= {value}", value("6.5")), is(true));
        assertThat(calc.matches("{key} <= {value}", value("5.5")), is(true));
        assertThat(calc.matches("{key} <= {value}", value("4.5")), is(false));

        assertThat(calc.matches("{key} = {value}", value("6.5")), is(false));
        assertThat(calc.matches("{key} = {value}", value("5.5")), is(true));
        assertThat(calc.matches("{key} = {value}", value("4.5")), is(false));


        assertThat(calc.matches("{key} > {value}", value("1")), is(true));
        assertThat(calc.matches("{key} > {value}", value("5")), is(true));
        assertThat(calc.matches("{key} > {value}", value("6")), is(false));

        assertThat(calc.matches("{key} < {value}", value("6")), is(true));
        assertThat(calc.matches("{key} < {value}", value("5")), is(false));
        assertThat(calc.matches("{key} < {value}", value("4")), is(false));

        assertThat(calc.matches("{key} >= {value}", value("1")), is(true));
        assertThat(calc.matches("{key} >= {value}", value("5")), is(true));
        assertThat(calc.matches("{key} >= {value}", value("6")), is(false));

        assertThat(calc.matches("{key} <= {value}", value("6")), is(true));
        assertThat(calc.matches("{key} <= {value}", value("5")), is(false));
        assertThat(calc.matches("{key} <= {value}", value("4")), is(false));

        assertThat(calc.matches("{key} = {value}", value("6")), is(false));
        assertThat(calc.matches("{key} = {value}", value("4")), is(false));
    }

	private Map<String, String> value(String value) {
		attr.put("value", value);
		return attr;
	}
	
	@Test
	public void testSimpleStringMatching() {
		attr.put("key", "apa");
		attr.put("tournamentName", "Cubeia-weekly");
		assertThat(calc.matches("{key} = 'apa'", attr), is(true));
		assertThat(calc.matches("{key} = 'apan'", attr), is(false));
		assertThat(calc.matches("{tournamentName} = 'Cubeia-weekly'", attr), is(true));
	}
	
	@Test
	public void testBooleanMatch() {
		attr.put("key", "false");
		assertThat(calc.matches("{key} = 'false'", attr), is(true));
		assertThat(calc.matches("{key} = 'true'", attr), is(false));
	}
	
	
	@Test(expected=RuleException.class)
	public void testErrorOnOperands() {
		attr.put("key", "apa");
		calc.matches("'key' > 'apa", attr); // String and integer
	}
	
	@Test(expected=RuleException.class)
	public void testErrorOnInvalidElement() {
		attr.put("key", "apa");
		calc.matches("{key} = apa", attr);
	}
	
	@Test
	public void testMultipleExpressions() {
		attr.put("fruit", "banana");
		attr.put("count", "5");
		assertThat(calc.matches("{fruit} = 'banana' & {count} > 2", attr), is(true));
		assertThat(calc.matches("{fruit} = 'banana' & {count} > 6", attr), is(false));
		assertThat(calc.matches("{fruit} = 'banana' | {count} > 6", attr), is(true));
		assertThat(calc.matches("{fruit} = 'apple' | {count} > 6", attr), is(false));
		assertThat(calc.matches("{fruit} = 'banana' | {count} > 2", attr), is(true));
	}
	
	@Test
	public void testCheckOperand() {
		assertThat(calc.checkOperand("&", true, true), is(true));
		assertThat(calc.checkOperand("&", false, false), is(false));
		assertThat(calc.checkOperand("&", true, false), is(false));
		assertThat(calc.checkOperand("|", true, false), is(true));
		assertThat(calc.checkOperand("|", true, true), is(true));
		assertThat(calc.checkOperand("|", false, true), is(true));
		assertThat(calc.checkOperand("|", false, false), is(false));
	}
	
	@Test
	public void testNullValues() {
		attr.put("fruit", null);
		attr.put("count", "6");
		assertThat(calc.matches("{fruit} = 'banana' & {count} > 1", attr), is(false));
		
		attr.put("fruit", "banana");
		attr.put("count", null);
		assertThat(calc.matches("{fruit} = 'banana' & {count} > 1", attr), is(false));
		
		assertThat(calc.matches("{missing} = 'banana'", attr), is(false));
	}
}
