
package edu.mayo.informatics.lexgrid.convert.directConversions.owlapi;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.custom.concepts.EntityFactory;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;

/**
 * Association class in lg model is spit to AssociationPredicate 
 * and AssociationEntity. AssociationWrapper holds AssociationPredicate
 * & AssociationEntity, and it's only used in package
 * edu.mayo.informatics.lexgrid.convert
 * 
 * 
 * @author Zonghui Lian
 *
 */
public class AssociationWrapper {
    private AssociationPredicate ap;
    private AssociationEntity ae;
    private String relationsContainerName;
    private boolean inverseTransitive;
    
    public AssociationWrapper(){
        ap = new AssociationPredicate();
        ae = EntityFactory.createAssociation();
    }
    
    public AssociationPredicate getAssociationPredicate() {
        return ap;
    }
    
    public AssociationEntity getAssociationEntity() {
        return ae;
    }
    
    public void setAssociationPredicate(AssociationPredicate in) {
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
    
    public void setEntityDescription(String description) {
        EntityDescription ed = new EntityDescription();
        ed.setContent(description);
        ae.setEntityDescription(ed);
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

    public void setRelationsContainerName(String relationsContainerName) {
        this.relationsContainerName = relationsContainerName;
    }

    public String getRelationsContainerName() {
        return relationsContainerName;
    }

    public boolean isInverseTransitive() {
        return inverseTransitive;
    }

    public void setInverseTransitive(boolean inverseTransitive) {
        this.inverseTransitive = inverseTransitive;
    }
}