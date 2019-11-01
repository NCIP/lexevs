package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

public class GraphNodeContentTrackingIterator implements Iterator<ConceptReference>, Iterable<ConceptReference>, Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 2508802737228472504L;
    private List<ConceptReference> cache = new ArrayList<ConceptReference>();
    private int inCachePosition = 0;
    
    public GraphNodeContentTrackingIterator(List<ConceptReference> list){
        this.cache = list;
    }
    
    @Override
    public Iterator<ConceptReference> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if(cache == null){
            throw new RuntimeException("Iterator has not been initialized");
        }
        return inCachePosition < cache.size();
    }

    @Override
    public ConceptReference next() {
        if (!hasNext()) {
            throw new RuntimeException("Iterator is Empty");
        }
        return cache.get(inCachePosition++);
    }
    
    public int getNumberRemaining(){
        return inCachePosition;
    }
    
    public int getTotalCacheSize(){
        return cache.size();
    }

}
