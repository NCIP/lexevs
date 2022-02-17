
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MifCodeSystem implements Serializable{

    // Attributes from containing parent VocabularyModel object
    private String vmCombinedId;
    private String vmSchemaVersion;
    
    // CodeSystem attributes:
    private String name;
    private String title;
    private String codeSystemId;
    
    private String description;  // sub element path is <annotations>/<documentation>/<description>/<text>
    
    // Collection of CodeSystemVersion objects
    private List<MifCodeSystemVersion> codeSystemVersions;

    public MifCodeSystem() {
        super();
        codeSystemVersions = new ArrayList<MifCodeSystemVersion>();
    }

    public String getVmCombinedId() {
        return vmCombinedId;
    }

    public void setVmCombinedId(String vmCombinedId) {
        this.vmCombinedId = vmCombinedId;
    }

    public String getVmSchemaVersion() {
        return vmSchemaVersion;
    }

    public void setVmSchemaVersion(String vmSchemaVersion) {
        this.vmSchemaVersion = vmSchemaVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCodeSystemId() {
        return codeSystemId;
    }

    public void setCodeSystemId(String codeSystemId) {
        this.codeSystemId = codeSystemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MifCodeSystemVersion> getCodeSystemVersions() {
        return codeSystemVersions;
    }

    public void setCodeSystemVersions(List<MifCodeSystemVersion> codeSystemVersions) {
        this.codeSystemVersions = codeSystemVersions;
    }

}