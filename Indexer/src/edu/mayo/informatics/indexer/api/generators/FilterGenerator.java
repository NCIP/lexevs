/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.indexer.api.generators;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.search.Filter;

import edu.mayo.informatics.indexer.lucene.ChainableFilter;
import edu.mayo.informatics.indexer.lucene.NumericFilter;
import edu.mayo.informatics.indexer.utility.Utility;

/**
 * This class will create a filter for you that will do post-search filtering.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class FilterGenerator {
    ArrayList filters;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    /**
     * Create a new empty filter generator. Will return null if returnFilter()
     * is called.
     */
    public FilterGenerator() {
        filters = new ArrayList();
    }

    /**
     * Remove all current filters.
     */
    public void clearFilters() {
        filters.clear();
    }

    /**
     * Restrict results to documents where field is >= from and <= after
     * 
     * @param field
     * @param from
     * @param to
     * @param padToLength
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'from' and
     *            'to' will have digits added to it so that its lengths matches
     *            the padToLength field.
     */

    public void addNumFilter(String field, String from, String to, int padToLength) {
        filters.add(new NumericFilter(field, Utility.padString(from, '0', padToLength, true), Utility.padString(to,
                '0', padToLength, true)));
    }

    /**
     * Restrict results to documents where field is >= from and <= after
     * 
     * @param field
     * @param from
     * @param to
     * @param padToDigits
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'from' and
     *            'to' will have digits added to it so that its lengths matches
     *            the padToDigits field.
     */
    public void addNumFilter(String field, int from, int to, int padToDigits) {
        filters.add(new NumericFilter(field, Utility.padInt(from, '0', padToDigits, true), Utility.padInt(to, '0',
                padToDigits, true)));
    }

    /**
     * Restrict results to documents where field is >= from and <= after
     * 
     * @param field
     * @param from
     * @param to
     */
    public void addNumFilter(String field, Date from, Date to) {
        filters.add(new NumericFilter(field, formatter.format(from), formatter.format(to)));
    }

    /**
     * Restrict results to documents where field is <= before
     * 
     * @param field
     * @param before
     * @param padToLength
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'before' will
     *            have digits added to it so that its lengths matches the
     *            padToDigits field.
     */
    public void addAllowNumBelowFilter(String field, String before, int padToLength) {
        filters.add(NumericFilter.Before(field, Utility.padString(before, '0', padToLength, true)));
    }

    /**
     * Restrict results to documents where field is <= before
     * 
     * @param field
     * @param before
     * @param padToDigits
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'before' will
     *            have digits added to it so that its lengths matches the
     *            padToDigits field.
     */
    public void addAllowNumBelowFilter(String field, int before, int padToDigits) {
        filters.add(NumericFilter.Before(field, Utility.padInt(before, '0', padToDigits, true)));
    }

    /**
     * Restrict results to documents where field is <= before
     * 
     * @param field
     * @param before
     */
    public void addAllowNumBelowFilter(String field, Date before) {
        filters.add(NumericFilter.Before(field, formatter.format(before)));
    }

    /**
     * Restrict results to documents where field is >= after
     * 
     * @param field
     * @param after
     * @param padToLength
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'after' will
     *            have digits added to it so that its lengths matches the
     *            padToLength field.
     */
    public void addAllowNumAboveFilter(String field, String after, int padToLength) {
        filters.add(NumericFilter.After(field, Utility.padString(after, '0', padToLength, true)));
    }

    /**
     * Restrict results to documents where field is >= after
     * 
     * @param field
     * @param after
     * @param padToDigits
     *            All numbers in the index must have the same amount of digits
     *            for the filters to work. The value you gave for 'after' will
     *            have digits added to it so that its lengths matches the
     *            padToDigits field.
     */
    public void addAllowNumAboveFilter(String field, int after, int padToDigits) {
        filters.add(NumericFilter.After(field, Utility.padInt(after, '0', padToDigits, true)));
    }

    /**
     * Restrict results to documents where field is >= after
     * 
     * @param field
     * @param after
     */
    public void addAllowNumAboveFilter(String field, Date after) {
        filters.add(NumericFilter.After(field, formatter.format(after)));
    }

    /**
     * Return the filter that has been created.
     * 
     * @return A filter ready to pass to the search interface.
     */
    public Filter returnFilter() {
        if (filters.size() == 0) {
            return null;
        } else if (filters.size() == 1) {
            return (Filter) filters.get(0);
        } else {
            return new ChainableFilter((Filter[]) filters.toArray(new Filter[filters.size()]));
        }
    }
}