
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 * The Class ResolvedConceptReferencesIteratorAdapter. Used to convert a 
 * ResolvedConceptReferencesIterator to a regular java.util.Iterator,
 * with optional LBException conversions.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvedConceptReferencesIteratorAdapter implements Iterator<ResolvedConceptReference>, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5395961846009479720L;
    
    /** The iterator. */
    private ResolvedConceptReferencesIterator iterator;
    
    /** The exception handler. */
    private IteratorExceptionHandler exceptionHandler;
    
    /** The DEFAULT_EXCEPTION_HANDLER. */
    private static IteratorExceptionHandler DEFAULT_EXCEPTION_HANDLER = new IteratorExceptionHandler(){

        private static final long serialVersionUID = -3276679655963347164L;

        @Override
        public RuntimeException convertLBException(LBException lbException) {
            throw new RuntimeException(lbException);
        }
        
    };
    
    /**
     * Instantiates a new resolved concept references iterator adapter.
     *
     * @param iterator the iterator
     */
    public ResolvedConceptReferencesIteratorAdapter(ResolvedConceptReferencesIterator iterator){
        this(iterator, DEFAULT_EXCEPTION_HANDLER);
    }
    
    /**
     * Instantiates a new resolved concept references iterator adapter.
     *
     * @param iterator the iterator
     * @param exceptionHandler the exception handler
     */
    private ResolvedConceptReferencesIteratorAdapter(
            ResolvedConceptReferencesIterator iterator, 
            IteratorExceptionHandler exceptionHandler){
        this.iterator = iterator;
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        try {
            return this.iterator.hasNext();
        } catch (LBException e) {
            throw this.exceptionHandler.convertLBException(e);
        }
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public ResolvedConceptReference next() {
        try {
            return this.iterator.next();
        } catch (LBException e) {
            throw this.exceptionHandler.convertLBException(e);
        } 
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * The Interface IteratorExceptionHandler.
     *
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static interface IteratorExceptionHandler extends Serializable {
        
        /**
         * Convert lb exception.
         *
         * @param lbException the lb exception
         * @return the runtime exception
         */
        public RuntimeException convertLBException(LBException lbException);
    }
}