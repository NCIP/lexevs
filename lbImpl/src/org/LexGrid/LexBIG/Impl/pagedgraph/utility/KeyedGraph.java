
package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.utility.DaoUtility;
import org.springframework.beans.BeanUtils;

public class KeyedGraph {

    private Map<CodeNamespacePair,AssociatedConcept> nodes = new HashMap<CodeNamespacePair,AssociatedConcept>();
    private Set<CodeNamespacePair> roots = new HashSet<CodeNamespacePair>();
    
    public KeyedGraph(ResolvedConceptReferenceList list) {
        prepareRoots(list);
    }
    
    public KeyedGraph(Map<CodeNamespacePair,AssociatedConcept> nodes) {
        this.nodes = nodes;
    }
    
    protected void prepareRoots(ResolvedConceptReferenceList list) {
        for(ResolvedConceptReference ref : list.getResolvedConceptReference()) {
            prepareRoot(ref);
        }
    }
    
    protected void prepareRoot(ResolvedConceptReference ref) {
       this.roots.add(DaoUtility.toCodeNamespacePair(ref));
       AssociatedConcept ac = new AssociatedConcept();
       BeanUtils.copyProperties(ref, ac);
       walkTree(ac);
    }
    
    protected void walkTree(AssociationList list) {
        for(Association assoc : list.getAssociation()) {
            walkTree(assoc);
        }
    }
    
    private void walkTree(Association assoc) {
       walkTree(assoc.getAssociatedConcepts());
    }

    private void walkTree(AssociatedConceptList associatedConcepts) {
        for(AssociatedConcept ac : associatedConcepts.getAssociatedConcept()) {
            walkTree(ac);
        }
    }
    
    public ResolvedConceptReferenceList toResolvedConceptReferenceList() {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for(CodeNamespacePair pair : this.roots) {
            list.addResolvedConceptReference(this.nodes.get(pair));
        }
        return list;
    }

    protected void walkTree(AssociatedConcept ref) {
        if(ref != null) {
            nodes.put(DaoUtility.toCodeNamespacePair(ref), ref);
        }
        
        if(ref.getSourceOf() != null) {
            for(Association assoc : ref.getSourceOf().getAssociation()) {
                walkTree(assoc.getAssociatedConcepts());
            }
        }
        
        if(ref.getTargetOf() != null) {
            for(Association assoc : ref.getTargetOf().getAssociation()) {
                walkTree(assoc.getAssociatedConcepts());
            }
        }
    }

    public KeyedGraph union(KeyedGraph tree) {  
        Set<CodeNamespacePair> unionSet = new HashSet<CodeNamespacePair>(nodes.keySet());
        unionSet.addAll(tree.nodes.keySet());
        
        int tree1Size = nodes.size();
        int tree2Size = tree.nodes.size();
        
        KeyedGraph masterTree = tree1Size >= tree2Size ? this : tree;
        KeyedGraph slaveTree = tree1Size >= tree2Size ? tree : this;
   
        for(CodeNamespacePair pair : unionSet) {
            AssociatedConcept ac1 = masterTree.nodes.get(pair);
            AssociatedConcept ac2 = slaveTree.nodes.get(pair);
            
            if(ac1 != null && ac2 != null) {
                AssociatedConcept unionedConcept = union(ac1, ac2);
                ac1.setSourceOf(unionedConcept.getSourceOf());
                ac1.setTargetOf(unionedConcept.getTargetOf());   
                
                ac2.setSourceOf(unionedConcept.getSourceOf());
                ac2.setTargetOf(unionedConcept.getTargetOf());  
                
                if(masterTree.roots.contains(pair)){
                    if(!slaveTree.roots.contains(pair)) {
                        masterTree.roots.remove(pair);
                    }   
                }
                if(slaveTree.roots.contains(pair)){
                    if(!masterTree.roots.contains(pair)) {
                        slaveTree.roots.remove(pair);
                    }   
                }
            }
            
            if(ac1 == null && ac2 != null) {
                masterTree.nodes.put(pair, ac2);    
            }
        }  
        
        masterTree.roots.addAll(slaveTree.roots);
        
        KeyedGraph unionTree = new KeyedGraph(masterTree.nodes);
        unionTree.setRoots(masterTree.roots);
        
        return unionTree;
    }
    
    public KeyedGraph intersect(KeyedGraph tree) {  
        Set<CodeNamespacePair> unionSet = new HashSet<CodeNamespacePair>(nodes.keySet());
        unionSet.addAll(tree.nodes.keySet());
        
        int tree1Size = nodes.size();
        int tree2Size = tree.nodes.size();
        
        KeyedGraph slaveTree = tree1Size >= tree2Size ? this : tree;
        KeyedGraph masterTree = tree1Size >= tree2Size ? tree : this;
   
        for(CodeNamespacePair pair : unionSet) {
            AssociatedConcept ac1 = masterTree.nodes.get(pair);
            AssociatedConcept ac2 = slaveTree.nodes.get(pair);
            
            if(ac1 != null && ac2 != null) {
                AssociatedConcept intersectConcept = intersect(ac1, ac2);
                ac1.setSourceOf(intersectConcept.getSourceOf());
                ac1.setTargetOf(intersectConcept.getTargetOf());   
            } else {
                masterTree.roots.remove(pair);
            }
        }  
        
        KeyedGraph unionTree = new KeyedGraph(masterTree.nodes);
        
        unionTree.setRoots(masterTree.roots);
        
        return unionTree;
    }
    
    private AssociatedConcept union(AssociatedConcept master, AssociatedConcept addition) {
        return MultiGraphUtility.unionReference(master, addition);
    }
    
    private AssociatedConcept intersect(AssociatedConcept master, AssociatedConcept addition) {
        return MultiGraphUtility.intersectReference(master, addition);
    }

    public Map<CodeNamespacePair, AssociatedConcept> getNodes() {
        return nodes;
    }

    public void setNodes(Map<CodeNamespacePair, AssociatedConcept> nodes) {
        this.nodes = nodes;
    }

    public Set<CodeNamespacePair> getRoots() {
        return roots;
    }

    public void setRoots(Set<CodeNamespacePair> roots) {
        this.roots = roots;
    } 
}