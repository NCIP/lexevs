
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class SystemReleaseSurvey {
    private AbsoluteCodingSchemeVersionReference codingScheme;
    private boolean propertiesPresent;
    private boolean relationsPropertiesPresent;
    private boolean isLoaded = false;
    private String revisionId;
    private String editHistoryEntryPoint;
    
    public String getEditHistoryEntryPoint() {
        return editHistoryEntryPoint;
    }
    public void setEditHistoryEntryPoint(String editHistoryEntryPoint) {
        this.editHistoryEntryPoint = editHistoryEntryPoint;
    }
    public boolean isLoaded() {
        return isLoaded;
    }
    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
    public String getRevisionId() {
        return revisionId;
    }
    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }
    public AbsoluteCodingSchemeVersionReference getCodingScheme() {
        return codingScheme;
    }
    public void setCodingScheme(AbsoluteCodingSchemeVersionReference codingScheme) {
        this.codingScheme = codingScheme;
    }
    public boolean isPropertiesPresent() {
        return propertiesPresent;
    }
    public void setPropertiesPresent(boolean propertiesPresent) {
        this.propertiesPresent = propertiesPresent;
    }
    public boolean isRelationsPropertiesPresent() {
        return relationsPropertiesPresent;
    }
    public void setRelationsPropertiesPresent(boolean relationsPropertiesPresent) {
        this.relationsPropertiesPresent = relationsPropertiesPresent;
    }
    public String toString(){
        
        
        return "URI: " + codingScheme.getCodingSchemeURN()
        + "\nversion: " + codingScheme.getCodingSchemeVersion()
        + "\ncoding scheme has properties: " + propertiesPresent
        + "\ncoding scheme relations have properties: " + relationsPropertiesPresent
        + "\nrevision id: " + revisionId    
        + "\nedit history 1st revision id: " + editHistoryEntryPoint;  
        
    }
    
    
}