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
package edu.mayo.informatics.resourcereader.obo;

import java.util.Iterator;
import java.util.Vector;

import edu.mayo.informatics.resourcereader.core.StringUtils;

public class OBOSynonym {
    private String text = "";
    private String scope = "";
    private String typeName = "";
    private String dbxref = "";

    public static final String SCOPE_EXACT = "EXACT";
    public static final String SCOPE_BROAD = "BROAD";
    public static final String SCOPE_NARROW = "NARROW";
    public static final String SCOPE_RELATED = "RELATED";
    public static final String SCOPE_NONE = "";

    public String getDbxref() {
        return dbxref;
    }

    public void setDbxref(String dbxref) {
        this.dbxref = dbxref;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static OBOSynonym createSynonym(String obo_synonym_text, Vector<String> synonymTypeDefs) {

        OBOSynonym syn = new OBOSynonym();
        String currentTag = "";

        if (obo_synonym_text.startsWith(OBOConstants.TAG_SYNONYM)) {
            syn.setScope(OBOSynonym.SCOPE_NONE);
            currentTag = OBOConstants.TAG_SYNONYM;
        }

        if (obo_synonym_text.startsWith(OBOConstants.TAG_SYNONYM_EXACT)) {
            syn.setScope(OBOSynonym.SCOPE_EXACT);
            currentTag = OBOConstants.TAG_SYNONYM_EXACT;
        }

        if (obo_synonym_text.startsWith(OBOConstants.TAG_SYNONYM_BROAD)) {
            syn.setScope(OBOSynonym.SCOPE_BROAD);
            currentTag = OBOConstants.TAG_SYNONYM_BROAD;
        }

        if (obo_synonym_text.startsWith(OBOConstants.TAG_SYNONYM_NARROW)) {
            syn.setScope(OBOSynonym.SCOPE_NARROW);
            currentTag = OBOConstants.TAG_SYNONYM_NARROW;
        }

        if (obo_synonym_text.startsWith(OBOConstants.TAG_SYNONYM_RELATED)) {
            syn.setScope(OBOSynonym.SCOPE_RELATED);
            currentTag = OBOConstants.TAG_SYNONYM_RELATED;
        }
        String aVal = StringUtils.parseAsSimpleKeyValue(obo_synonym_text, currentTag);
        Vector vec = StringUtils.makeWordVectorOfSentence(aVal);
        Iterator items = vec.iterator();
        if (items.hasNext()) {
            // First item in the vector is the synonym text
            String synonym= items.next().toString();
            synonym= StringUtils.removeOuterMostQuotes(synonym);
            syn.setText(synonym);
        }

        for (; items.hasNext();) {
            String str = items.next().toString();
            if (isStringInScope(str)) {
                syn.setScope(str);
            } else if (synonymTypeDefs.contains(str)) {
                syn.setTypeName(str);
            } else {
                syn.setDbxref(syn.getDbxref() + " " + str);
            }
        }
        return syn;

    }

    private static boolean isStringInScope(String str) {
        if (str.equalsIgnoreCase(SCOPE_EXACT) || str.equalsIgnoreCase(SCOPE_BROAD)
                || str.equalsIgnoreCase(SCOPE_NARROW) || str.equalsIgnoreCase(SCOPE_RELATED)) {
            return true;

        }
        return false;
    }

}