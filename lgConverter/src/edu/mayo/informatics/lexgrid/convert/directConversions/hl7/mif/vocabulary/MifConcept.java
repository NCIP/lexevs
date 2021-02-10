
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MifConcept implements Serializable{

    // Attributes from great grandparent VocabularyModel object
    private String vmCombinedId;
    
    // Attributes from grandparent CodeSystem object
    private String csCodeSystemId;
    
    // Attributes from parent CodeSystemVersion object
    private String csvReleaseDate;
    
    // Concept attributes:
    private boolean isIsSelectable = true; // XML attribute name = isSelectable

    private String definition;  // sub element path is <annotations>/<documentation>/<definition>/<text>
    
    private MifPrintName printName;
    
    private List<MifConceptRelationship> conceptRelationships;
    private List<MifConceptProperty> conceptProperties;
    private List<MifConceptCode> conceptCodes;

    public MifConcept() {
        super();
        conceptCodes = new ArrayList<MifConceptCode>();
        setPrintName(new MifPrintName());
    }

    public String getVmCombinedId() {
        return vmCombinedId;
    }

    public void setVmCombinedId(String vmCombinedId) {
        this.vmCombinedId = vmCombinedId;
    }

    public String getCsCodeSystemId() {
        return csCodeSystemId;
    }

    public void setCsCodeSystemId(String csCodeSystemId) {
        this.csCodeSystemId = csCodeSystemId;
    }

    public String getCsvReleaseDate() {
        return csvReleaseDate;
    }

    public void setCsvReleaseDate(String csvReleaseDate) {
        this.csvReleaseDate = csvReleaseDate;
    }

    public boolean isSelectable() {
        return isIsSelectable;
    }

    public void setIsSelectable(boolean isIsSelectable) {
        this.isIsSelectable = isIsSelectable;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public MifPrintName getPrintName() {
        return printName;
    }

    public void setPrintName(MifPrintName printName) {
        this.printName = printName;
    }

    public List<MifConceptRelationship> getConceptRelationships() {
        return conceptRelationships;
    }

    public void setConceptRelationships(List<MifConceptRelationship> conceptRelationships) {
        this.conceptRelationships = conceptRelationships;
    }

    public List<MifConceptProperty> getConceptProperties() {
        return conceptProperties;
    }

    public void setConceptProperties(List<MifConceptProperty> conceptProperties) {
        this.conceptProperties = conceptProperties;
    }

    public List<MifConceptCode> getConceptCodes() {
        return conceptCodes;
    }

    public void setConceptCodes(List<MifConceptCode> conceptCodes) {
        this.conceptCodes = conceptCodes;
    }    
    
}