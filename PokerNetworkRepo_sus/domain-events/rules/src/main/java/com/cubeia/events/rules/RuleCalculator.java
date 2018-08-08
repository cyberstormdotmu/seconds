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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 *
 *
 * @author fredrik
 *
 */
public class RuleCalculator {
    private final static String SPLIT_SYMBOL_REGEXP = "((?<=%1$s)|(?=%1$s))";

    private final static String[] SPLIT_SYMBOLS = new String[]{"<=",">=","!=","<",">","=","&","\\|"};

    private String split = "";

    public RuleCalculator() {
        StringBuilder sb = new StringBuilder();
        for(String s : SPLIT_SYMBOLS) {
            if(sb.length()>0) {
                sb.append("|");
            }
            sb.append(symbol(s));
        }
        this.split = sb.toString();
    }

    public boolean matches(String rule, Map<String, String> attributes) {
        boolean match = true;
        String[] elements = splitRule(rule);

        int expressions = getNumberOfExpressions(elements.length);

        for (int i = 0; i < expressions; i++) {
            String[] expression = Arrays.copyOfRange(elements, i*4, i*4+3);
            boolean result = getExpressionResult(attributes, expression);
            if (i > 0) {
                String op = elements[i*4-1];
                match = checkOperand(op, match, result);
            } else {
                match = result;
            }

        }
        return match;
    }
    private String symbol(String symbol) {
        return  String.format(SPLIT_SYMBOL_REGEXP,symbol);
    }
    protected String[] splitRule(String rule) {
        rule = removeWhitespace(rule);
        String[] splits =  rule.split(split);
        return mergeOperators(splits);
    }

    private String[] mergeOperators(String[] splits) {
        List<String> target = new ArrayList<>();
        for(int i = 0; i<splits.length; i++) {
            if(splits[i].matches("!|<|>")) {
                if(i+1<splits.length && splits[i+1].equals("=")) {
                    target.add(splits[i]+splits[i+1]);
                    i++;
                    continue;
                }
            }
            target.add(splits[i]);
        }
        return target.toArray(new String[0]);
    }

    /**
     * Removes whitespace from a string except for whitespaces within
     * ' character.  eg
     * test white space 'to remove' => testwhitespace'to remove'
     * @param rule
     * @return
     */
    protected String removeWhitespace(String rule) {
        return rule.replaceAll("\\s+(?=([^']*'[^']*')*[^']*$)", "");
    }

    protected boolean checkOperand(String op, boolean var1, boolean var2) {
        if (op.equals("&")) {
            return var1 && var2;
        } else if (op.equals("|")) {
            return var1 || var2;
        } else {
            throw new RuleException("Invalid expression operator "+op+", should be & or |");
        }
    }

    private boolean getExpressionResult(Map<String, String> attributes, String[] expression) {
        return checkOperation(expression, attributes);
    }

    private int getNumberOfExpressions(int length) {
        int rem = (length - 3) % 4;
        if (rem != 0) {
            throw new RuleException("Expression does not have a valid number of elements");
        }
        return 1 + ((length - 3) / 4);
    }

    private boolean checkOperation(String[] expression, Map<String, String> attributes) {
        boolean match = true;

        String operand = expression[1];

        String val1 = expression[0];
        String val2 = expression[2];

        val1 = doSubstitutionIfApplicable(val1, attributes);
        val2 = doSubstitutionIfApplicable(val2, attributes);

        if (val1 == null || val2 == null) {
            return false;
        }

        if (isBothNumeric(val1, val2)) {
            match &= evaluateNumericExpression(val1, operand, val2);
        } else {
            match &= evaluateStringExpression(val1, operand, val2);
        }

        return match;
    }

    private boolean isBothNumeric(String val1, String val2) {
        boolean n1 = isNumeric(val1);
        boolean n2 = isNumeric(val2);

        if (n1 ^ n2) {
            throw new RuleException("Two variables in an expression have different type. "+val1+" and "+val2+" must be both either String values wrapped in '' or numeric");
        } else if (n1 && n2) {
            return true;
        } else {
            return false;
        }
    }

    private String doSubstitutionIfApplicable(String value, Map<String, String> attributes) {
        if (isVariable(value)) {
            return attributes.get(value.substring(1, value.length()-1));
        } if (isString(value)) {
            return value.substring(1, value.length()-1);
        } else if (isNumeric(value)) {
            return value;
        } else {
            throw new RuleException("Invalid element declaration "+value+". Should conform to Variable{"+value+"}, String '"+value+"' or be numeric");
        }

    }

    private boolean evaluateNumericExpression(String keyValueStr, String operand, String valueStr) {
        double key = Double.parseDouble(keyValueStr);
        double value = Double.parseDouble(valueStr);

        if (operand.equals(">")) {
            return key > value;
        } else if (operand.equals(">=")) {
            return key >= value;
        } else if (operand.equals("<")) {
            return key < value;
        } else if (operand.equals("<=")) {
            return key <= value;
        } else if (operand.equals("=")) {
            return key == value;
        } else if (operand.equals("!=")) {
            return key != value;
        } else {
            throw new RuleException("Unknown operand: "+operand);
        }
    }

    private boolean evaluateStringExpression(String value1, String operand, String value2) {
        if (operand.equals("=")) {
            return value1.equals(value2);
        } else {
            throw new RuleException("Invalid operand for String matching (only = allowed): "+operand);
        }
    }


    protected boolean isVariable(String element) {
        return element.startsWith("{") && element.endsWith("}");
    }

    protected boolean isString(String element) {
        return element.startsWith("'") && element.endsWith("'");
    }

    protected boolean isNumeric(String element) {
        return element.matches("\\d+(\\.\\d+)?");
    }

}
