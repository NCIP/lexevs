
package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;

public class MultiGraphUtility {
    
    public static ResolvedConceptReferenceList intersectReferenceList(
            ResolvedConceptReferenceList list1, 
            ResolvedConceptReferenceList list2) {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        if(list1 == null) { list1 = new ResolvedConceptReferenceList(); }
        if(list2 == null) { list2 = new ResolvedConceptReferenceList(); }
        
        for(ResolvedConceptReference ref : list1.getResolvedConceptReference()) {
            ResolvedConceptReference foundRef =
                getAssociatedConcept(ref, list2.getResolvedConceptReference());
            if(foundRef != null) {
                returnList.addResolvedConceptReference(intersectReference(ref, foundRef));
            }
        }

        return returnList;
    }

    public static ResolvedConceptReference intersectReference(ResolvedConceptReference ref1, ResolvedConceptReference ref2) {
        ref1.setSourceOf(intersectAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(intersectAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    public static AssociatedConcept intersectReference(AssociatedConcept ref1, AssociatedConcept ref2) {
        ref1.setSourceOf(intersectAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref2.setTargetOf(intersectAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    public static AssociationList intersectAssociationList(AssociationList list1, AssociationList list2) {
        AssociationList returnList = new AssociationList();
        
        if(list1 != null) {
            for(Association association : list1.getAssociation()) {
                
                Association intersectedAssociation = getAssociationForName(association.getAssociationName(), list2);
                
                if(intersectedAssociation != null) {
                    returnList.addAssociation(intersectAssociation(association, intersectedAssociation));
                } 
            }
        }
        
        if(returnList.getAssociationCount() == 0) {
            return null;
        } else {
            return returnList;
        }
    }
    
    public static Association intersectAssociation(Association assoc1, Association assoc2) {
        AssociatedConceptList list = new AssociatedConceptList();
        Association assoc = new Association();
        
        AssociatedConcept[] associatedConcepts1 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] associatedConcepts2 = assoc2.getAssociatedConcepts().getAssociatedConcept();
        
        for(AssociatedConcept concept : associatedConcepts1) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts2);
            if(foundConcept != null) {
                
                list.addAssociatedConcept(
                        intersectReference(concept, foundConcept));
            }
        }

        assoc.setAssociationName(errorThrowingCompare(assoc1.getAssociationName(), assoc2.getAssociationName()));
        assoc.setDirectionalName(nullReturningCompare(assoc1.getDirectionalName(), assoc2.getDirectionalName()));
        assoc.setRelationsContainerName(nullReturningCompare(assoc1.getRelationsContainerName(), assoc2.getRelationsContainerName()));
        assoc.setAssociatedConcepts(list);
        
        return assoc;
    }
    
    public static ResolvedConceptReferenceList unionReferenceList(
            ResolvedConceptReferenceList list1, 
            ResolvedConceptReferenceList list2) {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        if(list1 == null) { list1 = new ResolvedConceptReferenceList(); }
        if(list2 == null) { list2 = new ResolvedConceptReferenceList(); }
        
        for(ResolvedConceptReference ref : list1.getResolvedConceptReference()) {
            ResolvedConceptReference foundRef =
                getAssociatedConcept(ref, list2.getResolvedConceptReference());
            if(foundRef != null) {
                returnList.addResolvedConceptReference(unionReference(ref, foundRef));
            } else {
                returnList.addResolvedConceptReference(ref);
            }
        }
        
        for(ResolvedConceptReference ref : list2.getResolvedConceptReference()) {
            ResolvedConceptReference foundRef =
                getAssociatedConcept(ref, list1.getResolvedConceptReference());
            if(foundRef == null) {
                returnList.addResolvedConceptReference(ref);
            }
        }
        
        return returnList;
    }
    
    public static ResolvedConceptReference unionReference(ResolvedConceptReference ref1, ResolvedConceptReference ref2) {
        ref1.setSourceOf(unionAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref1.setTargetOf(unionAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        
        return ref1;
    }
    
    public static AssociatedConcept unionReference(AssociatedConcept ref1, AssociatedConcept ref2) {
        ref1.setSourceOf(unionAssociationList(ref1.getSourceOf(), ref2.getSourceOf()));
        ref1.setTargetOf(unionAssociationList(ref1.getTargetOf(), ref2.getTargetOf()));
       
        return ref1;
    }
    
    public static AssociationList unionAssociationList(AssociationList list1, AssociationList list2) {
        AssociationList returnList = new AssociationList();
        
        if(list1 != null) {
            for(Association association : list1.getAssociation()) {
                
                Association unionedAssociation = getAssociationForName(association.getAssociationName(), list2);
                
                if(unionedAssociation != null) {
                    returnList.addAssociation(unionAssociation(association, unionedAssociation));
                }  else {
                    returnList.addAssociation(association);
                }
            }
        }
        if(list2 != null) {
            for(Association association : list2.getAssociation()) {
                Association unionedAssociation = getAssociationForName(association.getAssociationName(), list1);
                
                if(unionedAssociation == null) {
                    returnList.addAssociation(association);
                }
            }
        }
        
        if(returnList.getAssociationCount() == 0) {
            return null;
        } else {
            return returnList;
        }
    }
    
    public static Association unionAssociation(Association assoc1, Association assoc2) {
        AssociatedConceptList list = new AssociatedConceptList();
        Association assoc = new Association();
        
        AssociatedConcept[] associatedConcepts1 = assoc1.getAssociatedConcepts().getAssociatedConcept();
        AssociatedConcept[] associatedConcepts2 = assoc2.getAssociatedConcepts().getAssociatedConcept();
        
        for(AssociatedConcept concept : associatedConcepts1) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts2);
            if(foundConcept != null) {
                list.addAssociatedConcept(
                        unionReference(concept, foundConcept));
            } else {
                list.addAssociatedConcept(concept);
            }
        }
        
        for(AssociatedConcept concept : associatedConcepts2) {
            AssociatedConcept foundConcept = getAssociatedConcept(concept, associatedConcepts1);
            if(foundConcept == null) {
                list.addAssociatedConcept(concept);
            } 
        }  
        
        assoc.setAssociationName(errorThrowingCompare(assoc1.getAssociationName(), assoc2.getAssociationName()));
        assoc.setDirectionalName(nullReturningCompare(assoc1.getDirectionalName(), assoc2.getDirectionalName()));
        assoc.setRelationsContainerName(nullReturningCompare(assoc1.getRelationsContainerName(), assoc2.getRelationsContainerName()));

        assoc.setAssociatedConcepts(list);
        
        return assoc;
    }
    
    private static <T extends ConceptReference> T getAssociatedConcept(T searchConcept, T[] list) {
        for(T concept : list) {
            if(concept.getCode().equals(searchConcept.getCode()) &&
                    concept.getCodeNamespace().equals(searchConcept.getCodeNamespace())){
                return concept;
            }
        }
        return null;
    }
    
    private static Association getAssociationForName(String associationName, AssociationList list) {
        if(list == null) {return null;}
        
        for(Association association : list.getAssociation()) {
            if(association.getAssociationName().equals(associationName)) {
                return association;
            }
        }
        return null;
    }
    
    private static <T> T nullReturningCompare(T one, T two) {
        if(one == null || two == null) {return null;}
        
        if(one.equals(two)) {
            return one;
        } else {
            return null;
        }
    }
    
    private static <T> T errorThrowingCompare(T one, T two) {
       T t = nullReturningCompare(one, two);
       if(t == null) { 
           throw new RuntimeException();
       } else {
           return t;
       }
    }
}