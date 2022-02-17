
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MifSupportedConceptRelationship implements Serializable {

    private String relationshipKind;
    private String name;
    private String inverseName;
    private String reflexivity;
    private String symmetry;
    private String transitivity;
    
    public MifSupportedConceptRelationship() {
        super();
    }
    
    public String getRelationshipKind() {
        return relationshipKind;
    }
    
    public void setRelationshipKind(String relationshipKind) {
        this.relationshipKind = relationshipKind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInverseName() {
        return inverseName;
    }

    public void setInverseName(String inverseName) {
        this.inverseName = inverseName;
    }

    public String getReflexivity() {
        return reflexivity;
    }

    public void setReflexivity(String reflexivity) {
        this.reflexivity = reflexivity;
    }

    public String getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }

    public String getTransitivity() {
        return transitivity;
    }

    public void setTransitivity(String transitivity) {
        this.transitivity = transitivity;
    }
    
    
}