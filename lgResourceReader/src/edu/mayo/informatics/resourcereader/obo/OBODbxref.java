
package edu.mayo.informatics.resourcereader.obo;

import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.mayo.informatics.resourcereader.core.StringUtils;

public class OBODbxref {

    String source = "";
    String subref = "";
    String description = "";
    String modifier = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubref() {
        return subref;
    }

    public void setSubref(String subref) {
        this.subref = subref;
    }
    
    public String getSubrefAndDescription() {
        String subrefAnddesc= subref+" "+ description;
        subrefAnddesc= subrefAnddesc.trim();
        if (subrefAnddesc.length() > 249) {
            subrefAnddesc= subrefAnddesc.substring(0, 249);
        }
        return subrefAnddesc;
    }

    public String toString() {
        return "source= " + source + " subref=" + subref + " description=" + description;
    }

    public static Vector<OBODbxref> parse(String str) {
        // final String REGEX = "(\\w*):([^\\s]*)(\\s*\".*\")?(,?)";
        // final String REGEX = "(.*):(.*)(\\s*\".*\")?(,?)";
        // final String REGEX = "(.*):(.*)\\s*\"(.*)?\"(,?)";
        // In the regex expression below, the
        // "\\[*" removes leading [
        // the ([^\"]*) becomes the source, ie everything before the colon and not a quote
        // the ([^\"\\]]*) becomes the subref, and is basically says everything but a "
        // or closing ]
        // the (\\s*\".*\")? becomes the description.
        final String REGEX = "\\[*([^\"]*):([^\"\\]]*)(\\s*\".*\")?";

        final String REGEX_HTTP = "(http[^\"]*)(\\s*\".*\")?";
        // We do not want to make dbxrefs from [ ]
        final String REGEX_EMPTY = "\\[\\s*\\]";
        Vector<OBODbxref> vector = new Vector<OBODbxref>();

        String str_array[] = StringUtils.splitEscapedString(str, ",");

        for (String temp : str_array) {
            // System.out.println("temp= " + temp);
            temp = temp.trim();
            Matcher m_http = Pattern.compile(REGEX_HTTP, Pattern.CASE_INSENSITIVE).matcher(temp);
            Matcher m = Pattern.compile(REGEX).matcher(temp);
            Matcher m_empty = Pattern.compile(REGEX_EMPTY).matcher(temp);
            if (m_http.find()) {
                OBODbxref dbxref = new OBODbxref();
                dbxref.source = (m_http.group(1) != null) ? m_http.group(1).trim() : "";
                dbxref.description = (m_http.group(2) != null) ? m_http.group(2).trim() : "";
                vector.add(dbxref);
            } else if (m.find()) {
                OBODbxref dbxref = new OBODbxref();
                dbxref.source = (m.group(1) != null) ? m.group(1).trim() : "";
                dbxref.subref = (m.group(2) != null) ? m.group(2).trim() : "";
                dbxref.description = (m.group(3) != null) ? m.group(3).trim() : "";
                if (!vector.contains(dbxref))
                    vector.add(dbxref);
            } else if (temp.length() != 0 && !m_empty.find()) {

                OBODbxref dbxref = new OBODbxref();
                dbxref.source = temp.trim();
                if (!vector.contains(dbxref))
                    vector.add(dbxref);
            }
        }

        return vector;

    }

    public static Vector<String> getSourceAndSubRefAsVector(Vector<OBODbxref> vec) {
        Vector<String> src_vec = new Vector<String>();
        for (OBODbxref dbxref : vec) {
            String sourceAndSubref= dbxref.getSource();
            if (!StringUtils.isNull(dbxref.getSubref())) {
                sourceAndSubref += ":" +dbxref.getSubref();
            }
            src_vec.add(sourceAndSubref);
        }
        return src_vec;

    }

    public boolean equals(Object o) {
        if (!(o instanceof OBODbxref))
            return false;
        OBODbxref dbxref = (OBODbxref) o;
        return (this.source.equals(dbxref.source) && this.subref.equals(dbxref.subref)
                && this.description.equals(dbxref.description) && this.modifier.equals(dbxref.modifier));
    }

    
    private static void printTest(String text) {
        List<OBODbxref> list = OBODbxref.parse(text);
        int count = 0;
        System.out.println("\nText= " + text);
        for (OBODbxref item : list) {
            count++;
            System.out.println("Item" + count + "= " + item);

        }
    }
    
    
    public static void main(String[] args) {
         String text;
         text=" [ABBA:BABA, DD:EEE, DD:EEE, test:subref \"description \\, page \\, book\", MESH:A.11.118.637.555.567.532, MESH:A.11.118.637.555.567.537 ,http://amazon.com/pradip.html \"pradip's webpage\", http://amazon.com/ashoka.html, dog:doggie dog doggie dogg, GO:ma,GO:blah \"abba abbcd\", new:entry   ]";
         printTest(text);
         text="[Stedman's Medical Dictionary \\, 27th edition:ISBN 0-683-40008-8, J:1776, Principles of Neural Science \\SE, 3rd edition:ISBN 0-8385-8034-3]";
         printTest(text);
         text= "[DOI:10.1007/BF02814484 \"Theissen G (2005) Birth, life and death of developmental control genes: New challenges for the homology concept. Theory in Biosciences 124:199-212\", DOI:10.1007/s00427-003-0301-4 \"Nielsen C and Martinez P (2003) Patterns of gene expression: homology or homocracy? Development Genes and Evolution 213: 149-154\", DOI:10.1186/1742-9994-2-15 \"Sanetra M et al. (2005) Conservation and co-option in developmental programmes: the importance of homology relationships. Frontiers in Zoology 2:15\"]";
         printTest(text);
         text= "[]";
         printTest(text);
         text="[ISBN:0123195837 \"Hillis DM (1994) in Hall BK (Ed.) Homology: The Hierarchical Basis of Comparative Biology. Academic Press, p. 348\", ISBN:0123195837 \"Sattler R (1994) in Hall BK (Ed.) Homology: The Hierarchical Basis of Comparative Biology. Academic Press, p. 425\", ISBN:978-0471984931 \"Hall BK (Ed.) (2000) HOMOLOGY : NOVARTIS FOUNDATION SYMPOSIUM 222. Wiley & Sons\"]";
         printTest(text);
    }

}