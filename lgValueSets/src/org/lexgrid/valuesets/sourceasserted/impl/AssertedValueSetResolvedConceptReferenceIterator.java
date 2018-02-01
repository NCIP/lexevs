package org.lexgrid.valuesets.sourceasserted.impl;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.lexevs.dao.database.service.valuesets.AssertedValueSetServiceImpl;

public class AssertedValueSetResolvedConceptReferenceIterator implements ResolvedConceptReferencesIterator {
	
	private List<ResolvedConceptReference> refs;
	private AssertedValueSetServiceImpl vsSvc;
	private String topNode;
	int position = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4182443658366514327L;

	public AssertedValueSetResolvedConceptReferenceIterator(String code, AssertedValueSetParameters params) {
		topNode = code;
		refs = new ArrayList<ResolvedConceptReference>();
		vsSvc = new AssertedValueSetServiceImpl();
		vsSvc.init(params);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResolvedConceptReference next() throws LBResourceUnavailableException, LBInvocationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResolvedConceptReferenceList next(int maxToReturn)
			throws LBResourceUnavailableException, LBInvocationException {
		// TODO Auto-generated method stub
		return null;
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

}
