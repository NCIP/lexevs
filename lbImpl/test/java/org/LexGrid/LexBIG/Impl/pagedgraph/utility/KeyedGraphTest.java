
package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import static org.junit.Assert.assertEquals;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.junit.Test;

public class KeyedGraphTest {

    @Test
    public void getDuplicates() {
        ResolvedConceptReferenceList list1 = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList list2 = new ResolvedConceptReferenceList();
        
        String[] codes1 = new String[] {"a","b","c","d","e","f"};
        String[] codes2 = new String[] {"d","e","f"};
        
        for(String code : codes1) {
            list1.addResolvedConceptReference(createResolvedConceptReference(code));
        }
        for(String code : codes2) {
            list2.addResolvedConceptReference(createResolvedConceptReference(code));
        }
        
        KeyedGraph g1 = new KeyedGraph(list1);
        KeyedGraph g2 = new KeyedGraph(list2);
        
        KeyedGraph unionedGraph = g1.union(g2);
        
        assertEquals(6, unionedGraph.getNodes().keySet().size()); 
    }
    
    @Test
    public void getTree() {
        ResolvedConceptReferenceList list1 = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList list2 = new ResolvedConceptReferenceList();
        
        AssociatedConcept as1 = this.createResolvedConceptReference("a");
        AssociatedConcept at1 = this.createResolvedConceptReference("b");
        this.addAssociatedConcept(as1, at1);
       
        AssociatedConcept as2 = this.createResolvedConceptReference("b");
        AssociatedConcept at2 = this.createResolvedConceptReference("c");
        this.addAssociatedConcept(as2, at2);
   
        list1.addResolvedConceptReference(as1);
        list2.addResolvedConceptReference(as2);
        KeyedGraph g1 = new KeyedGraph(list1);
        KeyedGraph g2 = new KeyedGraph(list2);
        
        KeyedGraph unionedGraph = g1.union(g2);
        
        ResolvedConceptReferenceList returnList = unionedGraph.toResolvedConceptReferenceList();
        assertEquals(1,returnList.getResolvedConceptReferenceCount());
        
        assertEquals("b",returnList.getResolvedConceptReference(0).getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getCode());
    
        assertEquals("c",returnList.getResolvedConceptReference(0).
                    getSourceOf().getAssociation(0).
                        getAssociatedConcepts().getAssociatedConcept(0).getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getCode());
        
    }
    
    @Test
    public void getTreeSharedRoot() {
        ResolvedConceptReferenceList list1 = new ResolvedConceptReferenceList();
        ResolvedConceptReferenceList list2 = new ResolvedConceptReferenceList();
        
        AssociatedConcept as1 = this.createResolvedConceptReference("a");
        AssociatedConcept at1 = this.createResolvedConceptReference("b");
        this.addAssociatedConcept(as1, at1);
       
        AssociatedConcept as2 = this.createResolvedConceptReference("a");
        AssociatedConcept at2 = this.createResolvedConceptReference("c");
        this.addAssociatedConcept(as2, at2);
   
        list1.addResolvedConceptReference(as1);
        list2.addResolvedConceptReference(as2);
        KeyedGraph g1 = new KeyedGraph(list1);
        KeyedGraph g2 = new KeyedGraph(list2);
        
        KeyedGraph unionedGraph = g1.union(g2);
        
        ResolvedConceptReferenceList returnList = unionedGraph.toResolvedConceptReferenceList();
        assertEquals(1,returnList.getResolvedConceptReferenceCount());
        
        assertEquals("a",returnList.getResolvedConceptReference(0).getCode());
    
        assertEquals(2,returnList.getResolvedConceptReference(0).
                    getSourceOf().getAssociation(0).
                        getAssociatedConcepts().getAssociatedConceptCount()); 
    }
    
    private void addAssociatedConcept(AssociatedConcept source, AssociatedConcept target) {
        AssociationList al = new AssociationList();
        Association assoc = new Association();
        AssociatedConceptList acl = new AssociatedConceptList();
        assoc.setAssociatedConcepts(acl);
        assoc.setAssociationName("testName");
        acl.addAssociatedConcept(target);
        al.addAssociation(assoc);
        source.setSourceOf(al);
    }
    
    private AssociatedConcept createResolvedConceptReference(String code) {
        AssociatedConcept ref = new AssociatedConcept();
        ref.setCode(code);
        ref.setCodeNamespace(code);
        return ref;
    }
 
}