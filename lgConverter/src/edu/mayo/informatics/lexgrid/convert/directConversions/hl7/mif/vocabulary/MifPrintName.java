
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;

/**
 * Object representing the HL7 <printName> XML element inside the <concept> XML element.
 * 
 * @author m046445
 *
 */
@SuppressWarnings("serial")
public class MifPrintName implements Serializable {

    private String language;
    private boolean preferredForLanguage = true;
    private String text;
    
    public MifPrintName() {
        super();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isPreferredForLanguage() {
        return preferredForLanguage;
    }

    public void setPreferredForLanguage(boolean preferredForLanguage) {
        this.preferredForLanguage = preferredForLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
}