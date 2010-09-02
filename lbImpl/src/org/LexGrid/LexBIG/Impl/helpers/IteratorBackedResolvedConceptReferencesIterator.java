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
    
    private static int UNKNOWN_NUMBER = -1;
    private int count;

    private Iterator<ResolvedConceptReference> iterator;
    
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
        return this.iterator.next();
    }

    @Override
    public ResolvedConceptReferenceList next(int page) throws LBResourceUnavailableException, LBInvocationException {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        for(int i=0;i<page && this.iterator.hasNext();i++) {
            returnList.addResolvedConceptReference(this.iterator.next());
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
        return this.iterator.hasNext();
    }

    @Override
    public int numberRemaining() throws LBResourceUnavailableException {
        return this.count;
    }

    @Override
    public void release() throws LBResourceUnavailableException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

}
