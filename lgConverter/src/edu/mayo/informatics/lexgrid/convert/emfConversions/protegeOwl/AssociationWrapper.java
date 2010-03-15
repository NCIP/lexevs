package edu.mayo.informatics.lexgrid.convert.emfConversions.protegeOwl;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;

public class AssociationWrapper {
    private AssociationPredicate ap;
    private AssociationEntity ae;
    
    public AssociationWrapper(){
        ap = new AssociationPredicate();
        ae = new AssociationEntity();
    }
    
    public AssociationPredicate getAssociationPredicate() {
        return ap;
    }
    
    public AssociationEntity getAssociationEntity() {
        return ae;
    }
    
    public void setAssociationPrediate(AssociationPredicate in) {
        ap = in;
    }
    
    public void setAssociationEntity(AssociationEntity in) {
        ae = in;
    }
    
    public void setAssociationName(String associationName) {
        ap.setAssociationName(associationName);
    }
    
    public void setEntityCode(String entityCode) {
        ae.setEntityCode(entityCode);
    }
    
    public void setEntityCodeNamespace(String entityCodeNamespace) {
        ae.setEntityCodeNamespace(entityCodeNamespace);
    }
    
    public void setForwardName(String forwardName) {
        ae.setForwardName(forwardName);
    }
    
    public void setReverseName(String reverseName) {
        ae.setReverseName(reverseName);
    }
    
    public void setIsTransitive(Boolean b) {
        ae.setIsTransitive(b);
    }
    
    public void setIsNavigable(Boolean b) {
        ae.setIsNavigable(b);
    }
    
    public void addProperty(Property p) {
        ae.addProperty(p);
    }
    

}
