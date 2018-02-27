package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

public class AssertedValueSetResolvedConceptReferenceIterator implements ResolvedConceptReferencesIterator {
	
	private List<ResolvedConceptReference> refs;
	private AssertedValueSetService vsSvc;
	private String topNode;
	private int maxValueSets;
	private int remaining;
	private int position = 0;
	private AssertedValueSetEntityResolver assertedValueSetEntityResolver;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4182443658366514327L;
	
	public AssertedValueSetResolvedConceptReferenceIterator() {};

	public AssertedValueSetResolvedConceptReferenceIterator(final String code, AssertedValueSetParameters params) {
		topNode = code;
		refs = new ArrayList<ResolvedConceptReference>();
		vsSvc = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getAssertedValueSetService();
		vsSvc.init(params);
		maxValueSets = vsSvc.getVSEntityCountForTopNodeCode(code);
		assertedValueSetEntityResolver = new AssertedValueSetEntityResolver(vsSvc);
		remaining = maxValueSets;
	}

	@Override
	public boolean hasNext() throws LBResourceUnavailableException {
		return numberRemaining() > 0;
	}

	@Override
	public void release() throws LBResourceUnavailableException {
		refs = null;
		vsSvc = null;
		topNode = null;
		remaining = 0;
		position = 0;
		assertedValueSetEntityResolver = null;
	}

	@Override
	public int numberRemaining() throws LBResourceUnavailableException {
		return remaining;
	}

	@Override
	public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
		return next(1).getResolvedConceptReference(0);
	}

	@Override
	public ResolvedConceptReferenceList next(final int pageSize)
			throws LBResourceUnavailableException, LBInvocationException {
        if (refs == null) {
            throw new LBResourceUnavailableException("This iterator is no longer valid.");
        }

        try {

            if (pageSize == 0) {
                return new ResolvedConceptReferenceList();
            }

            if (position == maxValueSets) {
                return new ResolvedConceptReferenceList();
            }

            int max = 0;
            if (pageSize < 0) {
                // setting a default size
                max = position + 100;
            } 
            else {
            	max = pageSize + position;
            }

            if (max > maxValueSets) {
                max = maxValueSets;
            }
            
            ResolvedConceptReferenceList returnedRefs = new ResolvedConceptReferenceList();

            if(position < max){
                returnedRefs = 
                		assertedValueSetEntityResolver.getResolvedConceptReferenceByCursorAndCode(topNode, position, getPageSizeSelectAll(pageSize));
                position = getPageSizeSelectAll(pageSize) + position;
                remaining = sizeRemaining(remaining, pageSize);
                if (refs == null) {
                    throw new LBResourceUnavailableException("This iterator has expired and is no longer valid. "
                            + "You may be attempting to retrieve too large a list or iterator page");
                }
                
            }
            
            return returnedRefs;

        }catch (Exception e) {
            String id = getLogger().error("Implementation problem in the resolved concept reference iterator", e);
            throw new LBInvocationException("Unexpected system error: " + e.getMessage(), id);
        }
	}

	@Override
	public ResolvedConceptReferenceList get(int start, int end)
			throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		return assertedValueSetEntityResolver.getResolvedConceptReferenceByCursorAndCode(topNode, start, end);
	}

	@Override
	public ResolvedConceptReferencesIterator scroll(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException {
		throw new UnsupportedOperationException("Scroll unsupported.");
	}

	@Override
	public ResolvedConceptReferenceList getNext() {
		throw new UnsupportedOperationException("GetNext unsupported.");
	}
	private int getPageSizeSelectAll(int size) {
		return size >= 0?size:maxValueSets;
	}
	
	private int sizeRemaining(int remain, int size) {
		return remaining - getPageSizeSelectAll(size) < 0?0: remaining - getPageSizeSelectAll(size); 
	}
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
}
