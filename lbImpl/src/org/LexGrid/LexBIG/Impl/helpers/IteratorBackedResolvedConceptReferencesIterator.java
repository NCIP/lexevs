
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;

@LgClientSideSafe
public class IteratorBackedResolvedConceptReferencesIterator implements ResolvedConceptReferencesIterator {

    private static final long serialVersionUID = -9172975996526240085L;
    
    public static int UNKNOWN_NUMBER = -1;
    private int count;

    protected Iterator<ResolvedConceptReference> iterator;
    
    public IteratorBackedResolvedConceptReferencesIterator(){
    }
    public IteratorBackedResolvedConceptReferencesIterator(
            Iterator<ResolvedConceptReference> iterator) {
        this(iterator, UNKNOWN_NUMBER);
    }
    
    public IteratorBackedResolvedConceptReferencesIterator(
            Iterator<ResolvedConceptReference> iterator,
            int count) {
        this.iterator = iterator;
        this.count = count;
    }
    @Override
    public ResolvedConceptReferenceList get(int arg0, int arg1) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReferenceList getNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
        if(count != UNKNOWN_NUMBER && count > 0) {
            count--;
        }
        return this.iterator.next();
    }

    @Override
    public ResolvedConceptReferenceList next(int page) throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        for(int i=0;i<page && this.iterator.hasNext();i++) {
            returnList.addResolvedConceptReference(this.iterator.next());
        }
        if(count != UNKNOWN_NUMBER) {
            count -= returnList.getResolvedConceptReferenceCount();
            if(count < 0){
                count = 0;
            }
        }
        return returnList;
    }

    @Override
    public ResolvedConceptReferencesIterator scroll(int arg0) throws LBResourceUnavailableException,
            LBInvocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() throws LBResourceUnavailableException {
        boolean hasNext = this.iterator.hasNext();
        if(!hasNext){
            count = 0;
        }
        return hasNext;
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return this.count;
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        //no-op
    }
}