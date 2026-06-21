package org.lexevs.dao.database.ibatis.association.parameter;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

public class BatchAssociationInsertBean {
    
    private String relationsContainer;
    private String associationPredicateId;
    private AssociationSource source;
    private AssociationTarget target;
    
    
    public BatchAssociationInsertBean(String relationsContainer, String associationPredicateId,
            AssociationSource source, AssociationTarget target) {
        super();
        this.relationsContainer = relationsContainer;
        this.associationPredicateId = associationPredicateId;
        this.source = source;
        this.target = target;
    }
    
    public String getRelationsContainer() {
        return relationsContainer;
    }

    public void setRelationsContainer(String relationsContainer) {
        this.relationsContainer = relationsContainer;
    }

    public String getAssociationPredicateId() {
        return associationPredicateId;
    }

    public void setAssociationPredicateId(String associationPredicateId) {
        this.associationPredicateId = associationPredicateId;
    }

    public AssociationSource getSource() {
        return source;
    }
    public void setSource(AssociationSource source) {
        this.source = source;
    }
    public AssociationTarget getTarget() {
        return target;
    }
    public void setTarget(AssociationTarget target) {
        this.target = target;
    }

}
