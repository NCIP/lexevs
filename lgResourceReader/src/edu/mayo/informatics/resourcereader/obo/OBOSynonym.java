
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
        Vector<String> vec = StringUtils.makeWordVectorOfSentence(aVal);
        Iterator<String> items = vec.iterator();
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