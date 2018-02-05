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
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

public class AssertedValueSetResolvedConceptReferenceIterator implements ResolvedConceptReferencesIterator {
	
	private List<ResolvedConceptReference> refs;
	private AssertedValueSetService vsSvc;
	private String topNode;
	private final int maxValueSets;
	private int remaining;
	private int position = 0;
	private AssertedValueSetEntityResolver assertedValueSetEntityResolver;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4182443658366514327L;

	public AssertedValueSetResolvedConceptReferenceIterator(String code, AssertedValueSetParameters params) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void release() throws LBResourceUnavailableException {
		// TODO Auto-generated method stub

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
                		assertedValueSetEntityResolver.getResolvedConceptReferenceByCursorAndCode(topNode, position, pageSize);
                position = pageSize + position;
                remaining = remaining - pageSize;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferencesIterator scroll(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferenceList getNext() {
		// TODO Auto-generated method stub
		return null;
	}
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
}
