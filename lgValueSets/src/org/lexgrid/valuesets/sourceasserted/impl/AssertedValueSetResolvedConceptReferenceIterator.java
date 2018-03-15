package org.lexgrid.valuesets.sourceasserted.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.paging.AbstractAssertedVSResolvedConceptReferenceIterator;

public class AssertedValueSetResolvedConceptReferenceIterator 
extends AbstractAssertedVSResolvedConceptReferenceIterator<ResolvedConceptReference>
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
		super();
		topNode = code;
		assertedValueSetEntityResolver = new AssertedValueSetEntityResolver(params, code);
		this.maxValueSets = assertedValueSetEntityResolver.getTotalEntityCount();
		remaining = maxValueSets;
		this.refreshRemaining(remaining);
	}

	@Override
	public void release() throws LBResourceUnavailableException {
		topNode = null;
		assertedValueSetEntityResolver = null;
		remaining = 0;
	}

	@Override
	public int numberRemaining() throws LBResourceUnavailableException {
		return this.remaining;
	}

	@Override
	public int refreshNumberRemaining(int remaining) {
		this.remaining = remaining;
		return remaining;
	}


	@Override
	@LgClientSideSafe
	public ResolvedConceptReference next() {
		remaining = remaining - 1 < 0?0:remaining -1;
		return super.next();
	}

	@Override
	@LgClientSideSafe
	public ResolvedConceptReferenceList next(int pageSize)
			throws LBResourceUnavailableException, LBInvocationException {
		if(pageSize < 0) {
			pageSize = maxValueSets;
		}
		ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
		if(this.getRefreshedRemaining() == 0) {return list;}
		List<ResolvedConceptReference> refs = protoNext(pageSize, remaining);
		if(isPagerProxied()) {
		this.refreshRemaining(remaining - pageSize <= 0? 0: remaining - pageSize);
		}
		refs.stream().forEachOrdered(list::addResolvedConceptReference);
		return list;
	}

	@Override
	public ResolvedConceptReferenceList get(int start, int end)
			throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
		ResolvedConceptReferenceList refList = new ResolvedConceptReferenceList();
		this.doPage(start, end, this.numberRemaining()).stream().forEachOrdered(refList::addResolvedConceptReference);
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
	
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

	@Override
	protected List<? extends ResolvedConceptReference> doPage(int skip, int maxToReturn) {
		int max = 0;
		try {
			if (maxToReturn == 0) {
				return new ArrayList<ResolvedConceptReference>();
			}

			if (skip == maxValueSets) {
				return new ArrayList<ResolvedConceptReference>();
			}


			if (maxToReturn < 0) {
				// setting a default size
				max = this.getGlobalPosition() + 100;
			} else {
				max = maxToReturn;
			}

			if (max > maxValueSets) {
				max = maxValueSets;
			}
			return assertedValueSetEntityResolver.getPagedConceptReferenceByCursorAndCode(topNode, skip, max);

		} catch (Exception e) {
			String id = getLogger().error(
					"Implementation problem in the resolved concept reference iterator next(int)",e);
			throw new RuntimeException("Unexpected system error: " + e.getMessage() + "Log ID: " + id);
		}
		finally {
			this.setGlobalPostion(this.getGlobalPosition() > maxValueSets || this.getGlobalPosition() + maxToReturn  > maxValueSets? maxValueSets: maxToReturn);
		}
	}
	
	@Override
	protected List<? extends ResolvedConceptReference> doPage(int skip, int maxToReturn, int remains) {

		try {
			if (maxToReturn == 0) {
				return new ArrayList<ResolvedConceptReference>();
			}

			if (skip == maxValueSets) {
				return new ArrayList<ResolvedConceptReference>();
			}

			int max = 0;
			if (maxToReturn < 0) {
				// setting a default size
				max = this.getGlobalPosition() + 100;
			} else {
				max = maxToReturn;
			}

			if (max > maxValueSets) {
				max = maxValueSets;
			}
			return assertedValueSetEntityResolver.getPagedConceptReferenceByCursorAndCode(topNode, skip, max > remains? remains: max );

		} catch (Exception e) {
			String id = getLogger().error(
					"Implementation problem in the resolved concept reference iterator next(int)",e);
			throw new RuntimeException("Unexpected system error: " + e.getMessage() + "Log ID: " + id);
		}
		finally {
			this.setGlobalPostion(this.getGlobalPosition() > maxValueSets || this.getGlobalPosition() + maxToReturn  > maxValueSets? maxValueSets: maxToReturn);
		}
	}
	
	public boolean isPagerProxied() {
		Field f = null;
		 try {
			 f =	 pager.getClass().getDeclaredField("CGLIB$BOUND");
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("No proxy declared");
		}
		if(f != null) {
		return true;
		}else { return false;}
	}
	
}
