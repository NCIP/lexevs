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

import java.sql.Time;
import java.util.Calendar;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public class TimeConverter
implements SqlTypeConverter {
    private Pattern _timePattern;
    private Calendar _calendar;

    public TimeConverter() {
        Perl5Compiler compiler = new Perl5Compiler();
        try {
            this._timePattern = compiler.compile("(?:\\d{4}\\-\\d{2}\\-\\d{2}\\s)?(\\d{2})(?::(\\d{2}))?(?::(\\d{2}))?(?:\\..*)?");
        }
        catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
        this._calendar = Calendar.getInstance();
        this._calendar.setLenient(false);
    }


    public Object convertFromString(String textRep, int sqlTypeCode) throws ConversionException {
        if (sqlTypeCode != 92) {
            return textRep;
        }
        if (textRep != null) {
            Perl5Matcher matcher = new Perl5Matcher();
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            if (matcher.matches(textRep, this._timePattern)) {
                MatchResult match = matcher.getMatch();
                int numGroups = match.groups();
                try {
                    hours = Integer.parseInt(match.group(1));
                    if (numGroups > 2 && match.group(2) != null) {
                        minutes = Integer.parseInt(match.group(2));
                    }
                    if (numGroups > 3 && match.group(3) != null) {
                        seconds = Integer.parseInt(match.group(3));
                    }
                }
                catch (NumberFormatException ex) {
                    throw new ConversionException("Not a valid time : " + textRep, ex);
                }
                this._calendar.clear();
                try {
                    this._calendar.set(11, hours);
                    this._calendar.set(12, minutes);
                    this._calendar.set(13, seconds);
                    return new Time(this._calendar.getTimeInMillis());
                }
                catch (IllegalArgumentException ex) {
                    throw new ConversionException("Not a valid time : " + textRep, ex);
                }
            }
            throw new ConversionException("Not a valid time : " + textRep);
        }
        return null;
    }


    public String convertToString(Object obj, int sqlTypeCode) throws ConversionException {
        String result = null;
        if (obj != null) {
            if (!(obj instanceof Time)) {
                throw new ConversionException("Expected object of type java.sql.Time, but instead received " + obj.getClass().getName());
            }
            result = obj.toString();
        }
        return result;
    }
}

