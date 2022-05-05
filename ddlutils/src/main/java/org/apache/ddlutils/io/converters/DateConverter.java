/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.oro.text.regex.MalformedPatternException
 *  org.apache.oro.text.regex.MatchResult
 *  org.apache.oro.text.regex.Pattern
 *  org.apache.oro.text.regex.Perl5Compiler
 *  org.apache.oro.text.regex.Perl5Matcher
 */
package org.apache.ddlutils.io.converters;

import java.sql.Date;
import java.util.Calendar;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class DateConverter
implements SqlTypeConverter {
    private Pattern _datePattern;
    private Calendar _calendar;

    public DateConverter() {
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._datePattern = compiler.compile("(\\d{2,4})(?:\\-(\\d{2}))?(?:\\-(\\d{2}))?.*");
        }
        catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
        this._calendar = Calendar.getInstance();
        this._calendar.setLenient(false);
    }


    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (sqlTypeCode != 91) {
            return textRep;
        }
        if (textRep != null) {
            Perl5Matcher matcher = new Perl5Matcher();
            int year = 1970;
            int month = 1;
            int day = 1;
            if (matcher.matches(textRep, this._datePattern)) {
                MatchResult match = matcher.getMatch();
                int numGroups = match.groups();
                try {
                    year = Integer.parseInt(match.group(1));
                    if (numGroups > 2 && match.group(2) != null) {
                        month = Integer.parseInt(match.group(2));
                    }
                    if (numGroups > 3 && match.group(3) != null) {
                        day = Integer.parseInt(match.group(3));
                    }
                }
                catch (NumberFormatException ex) {
                    throw new ConversionException("Not a valid date : " + textRep, ex);
                }
                this._calendar.clear();
                try {
                    this._calendar.set(year, month - 1, day);
                    return new Date(this._calendar.getTimeInMillis());
                }
                catch (IllegalArgumentException ex) {
                    throw new ConversionException("Not a valid date : " + textRep, ex);
                }
            }
            throw new ConversionException("Not a valid date : " + textRep);
        }
        return null;
    }


    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        String result = null;
        if (obj != null) {
            if (!(obj instanceof Date)) {
                throw new ConversionException("Expected object of type java.sql.Date, but instead received " + obj.getClass().getName());
            }
            result = obj.toString();
        }
        return result;
    }
}

