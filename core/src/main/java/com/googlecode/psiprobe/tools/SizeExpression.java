/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.tools;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool for parsing and formatting SI-prefixed numbers in base-2 and base-10.
 *
 * @author Mark Lewis
 */
public class SizeExpression {

    public static final long MULTIPLIER_2 = 1024;
    public static final long MULTIPLIER_10 = 1000;
    public static final String UNIT_BASE = "B";
    public static final char PREFIX_KILO = 'K';
    public static final char PREFIX_MEGA = 'M';
    public static final char PREFIX_GIGA = 'G';
    public static final char PREFIX_TERA = 'T';
    public static final char PREFIX_PETA = 'P';

    /**
     * Parses the given expression into a numerical value.
     * 
     * <p>
     * An expression has three parts:
     * </p>
     * <table>
     *  <thead>
     *   <tr>
     *    <th>Name</th>
     *    <th>Description</th>
     *   </tr>
     *  </thead>
     *  <tbody>
     *   <tr>
     *    <td>Base Number</td>
     *    <td>(Required) The mantissa or significand of the expression.  This can include decimal values.</td>
     *   </tr>
     *   <tr>
     *    <td>Prefix</td>
     *    <td>(Optional) The <a href="http://en.wikipedia.org/wiki/Si_prefix" target="_blank">SI prefix</a>.  These span from K for kilo- to P for peta-.</td>
     *   </tr>
     *   <tr>
     *    <td>Unit</td>
     *    <td>(Optional) If the unit "B" (for bytes) is provided, the prefix is treated as base-2 (1024).  Otherwise, it uses base-10 (1000).</td>
     *   </tr>
     *  </tbody>
     *  <tfoot>
     *   <tr>
     *    <td colspan="2"><em>Note: Whitespace may or may not exist between the Base Number and Prefix.</em></td>
     *   </tr>
     *  </tfoot>
     * </table>
     * 
     * <p>Examples:</p>
     * <ul>
     *  <li>"2k" returns {@code 2000}</li>
     *  <li>"3.5m" returns {@code 3500000}</li>
     *  <li>"2kb" returns {@code 2048}</li>
     *  <li>"3.5mb" returns {@code 3670016}</li>
     * </ul>
     * 
     * @param expression the expression to parse
     * @return the parsed value
     * @throws NumberFormatException if the given expression cannot be parsed
     */
    public static long parse(String expression) {
        String prefixClass = "[" + PREFIX_KILO + PREFIX_MEGA + PREFIX_GIGA + PREFIX_TERA + PREFIX_PETA + "]";
        Pattern p = Pattern.compile("(\\d+|\\d*\\.\\d+)\\s*(" + prefixClass + ")?(" + UNIT_BASE + ")?", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(expression);
        if (m.matches()) {
            String value = m.group(1);
            String unitPrefix = m.group(2);
            String unitBase = m.group(3);
            long multiplier = 1;
            if (unitPrefix != null) {
                multiplier = multiplier(unitPrefix.charAt(0), unitBase != null);
            }
            double rawValue = Double.parseDouble(value);
            return (long) (rawValue * multiplier);
        } else {
            throw new NumberFormatException("Invalid expression format: " + expression);
        }
    }

    /**
     * Formats the value as an expression.
     * 
     * @param value the numerical value to be formatted
     * @param decimalPlaces the number of decimal places in the mantissa
     * @param base2 whether to use the base-2 (1024) multiplier and format with
     *        "B" units.  If false, uses the base-10 (1000) multiplier and no
     *        units.
     * @return a formatted string expression of the value
     */
    public static String format(long value, int decimalPlaces, boolean base2) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(decimalPlaces);

        double doubleResult;
        String unit = (base2 ? UNIT_BASE : "");
        long multiplierKilo = multiplier(PREFIX_KILO, base2);
        long multiplierMega = multiplier(PREFIX_MEGA, base2);
        long multiplierGiga = multiplier(PREFIX_GIGA, base2);
        long multiplierTera = multiplier(PREFIX_TERA, base2);
        long multiplierPeta = multiplier(PREFIX_PETA, base2);
        if (value < multiplierKilo) {
            doubleResult = value;
            nf.setMinimumFractionDigits(0);
        } else if (value >= multiplierKilo && value < multiplierMega) {
            doubleResult = round(value / multiplierKilo, decimalPlaces);
            unit = PREFIX_KILO + unit;
        } else if (value >= multiplierMega && value < multiplierGiga) {
            doubleResult = round(value / multiplierMega, decimalPlaces);
            unit = PREFIX_MEGA + unit;
        } else if (value >= multiplierGiga && value < multiplierTera) {
            doubleResult = round(value / multiplierGiga, decimalPlaces);
            unit = PREFIX_GIGA + unit;
        } else if (value >= multiplierTera && value < multiplierPeta) {
            doubleResult = round(value / multiplierTera, decimalPlaces);
            unit = PREFIX_TERA + unit;
        } else {
            doubleResult = round(value / multiplierPeta, decimalPlaces);
            unit = PREFIX_PETA + unit;
        }
        return nf.format(doubleResult) + (base2 ? " " : "") + unit;
    }

    /**
     * Rounds a decimal value to the given decimal place.
     * 
     * @param value the value to round
     * @param decimalPlaces the number of decimal places to preserve.
     * @return the rounded value
     */
    private static double round(double value, int decimalPlaces) {
        return Math.round(value * Math.pow(10, decimalPlaces)) / Math.pow(10, decimalPlaces);
    }

    /**
     * Returns the base-2 or base-10 multiplier for a given prefix.
     * 
     * @param unitPrefix the character representing the prefix.  Can be K, M, G,
     *        T, or P.
     * @param base2 whether to use the base-2 (1024) multiplier.  If false, uses
     *        the base-10 (1000) multiplier.
     * @return the multiplier for the given prefix
     */
    private static long multiplier(char unitPrefix, boolean base2) {
        long result;
        long multiplier = (base2 ? MULTIPLIER_2 : MULTIPLIER_10);
        switch(Character.toUpperCase(unitPrefix)) {
            case PREFIX_KILO:
                result = multiplier;
                break;
            case PREFIX_MEGA:
                result = multiplier * multiplier;
                break;
            case PREFIX_GIGA:
                result = multiplier * multiplier * multiplier;
                break;
            case PREFIX_TERA:
                result = multiplier * multiplier * multiplier * multiplier;
                break;
            case PREFIX_PETA:
                result = multiplier * multiplier * multiplier * multiplier * multiplier;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit prefix: " + unitPrefix);
        }
        return result;
    }

}
