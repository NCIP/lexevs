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
import org.lexevs.logging.LoggerFactory;
import org.lexevs.paging.AbstractPageableIterator;

public class AssertedValueSetResolvedConceptReferenceIterator 
extends AbstractPageableIterator<ResolvedConceptReference>
implements ResolvedConceptReferencesIterator{
	private String topNode;
	private int maxValueSets;
	private int remaining;
	private AssertedValueSetEntityResolver assertedValueSetEntityResolver;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4182443658366514327L;
	
	public AssertedValueSetResolvedConceptReferenceIterator() {super();}

	public AssertedValueSetResolvedConceptReferenceIterator(final String code, AssertedValueSetParameters params) {
		topNode = code;
		assertedValueSetEntityResolver = new AssertedValueSetEntityResolver(params, code);
		this.maxValueSets = assertedValueSetEntityResolver.getTotalEntityCount();
		remaining = maxValueSets;
	}

//	@Override
//	public boolean hasNext() throws LBResourceUnavailableException {
//		return numberRemaining() > 0;
//	}

	@Override
	public void release() throws LBResourceUnavailableException {
//		refs = null;
		topNode = null;
		assertedValueSetEntityResolver = null;
	}

	@Override
	public int numberRemaining() throws LBResourceUnavailableException {
		return remaining;
	}

	@Override
	public ResolvedConceptReference next() {
		remaining = remaining - 1 < 0?0:remaining -1;
		return super.next();
	}

	@Override
	public ResolvedConceptReferenceList next(int pageSize)
			throws LBResourceUnavailableException, LBInvocationException {
		if(pageSize < 0) {
			pageSize = maxValueSets;
		}
		ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
		List<ResolvedConceptReference> refs = protoNext(pageSize);
		remaining = remaining -  pageSize < 0?0:remaining - pageSize;
		refs.stream().forEachOrdered(list::addResolvedConceptReference);
		return list;
	}

	@Override
	public ResolvedConceptReferenceList get(int start, int end)
			throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		ResolvedConceptReferenceList refList = new ResolvedConceptReferenceList();
		this.doPage(start, end).stream().forEachOrdered(refList::addResolvedConceptReference);
		return refList;
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
	
//	private int getPageSizeSelectAll(int size) {
//		return size >= 0?size:maxValueSets;
//	}
	
//	private int sizeRemaining(int remain, int size) {
//		return remaining - getPageSizeSelectAll(size) < 0?0: remaining - getPageSizeSelectAll(size); 
//	}
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

	@Override
	protected List<? extends ResolvedConceptReference> doPage(int start, int end) {

		try {
			if (end == 0) {
				return new ArrayList<ResolvedConceptReference>();
			}

			if (start == maxValueSets) {
				return new ArrayList<ResolvedConceptReference>();
			}

			int max = 0;
			if (end < 0) {
				// setting a default size
				max = this.getGlobalPosition() + 100;
			} else {
				max = end;
			}

			if (max > maxValueSets) {
				max = maxValueSets;
			}
			return assertedValueSetEntityResolver.getPagedConceptReferenceByCursorAndCode(topNode, start, max);

		} catch (Exception e) {
			String id = getLogger().error("Implementation problem in the resolved concept reference iterator next(int)",
					e);
			throw new RuntimeException("Unexpected system error: " + e.getMessage() + "Log ID: " + id);
		}
	}
	
}
