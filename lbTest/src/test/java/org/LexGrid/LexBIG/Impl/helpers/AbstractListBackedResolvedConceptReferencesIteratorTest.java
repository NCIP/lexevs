package org.LexGrid.LexBIG.Impl.helpers;

import java.util.Arrays;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;

public class AbstractListBackedResolvedConceptReferencesIteratorTest extends
		LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return "AbstractListBackedResolvedConceptReferencesIteratorTest";
	}

	public void testHasNextWithZero() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(0);
		
		assertFalse(itr.hasNext());
	}
	
	public void testHasNextWithOne() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(1);
		
		assertTrue(itr.hasNext());
	}
	
	public void testHasNextWithOneAnd() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(1);
		
		itr.next();
		
		assertFalse(itr.hasNext());
	}
	
	public void testGetNextList() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(20);
		
		ResolvedConceptReferenceList list = itr.get(0, 10);
		
		assertEquals(10, list.getResolvedConceptReferenceCount());
	}

	public void testGetNextSmallerList() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(8);
		
		ResolvedConceptReferenceList list = itr.get(0, 10);
		
		assertEquals(8, list.getResolvedConceptReferenceCount());
	}
	
	public void testNext() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(20);
		
		ResolvedConceptReferenceList list = itr.next(10);
		
		assertEquals(10, list.getResolvedConceptReferenceCount());
	}
	
	public void testNextMore() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(20);
		
		ResolvedConceptReferenceList list = itr.next(30);
		
		assertEquals(20, list.getResolvedConceptReferenceCount());
	}
	
	public void testIterate() throws Exception {
		AbstractListBackedResolvedConceptReferencesIterator<Void> itr = this.create(21);
		
		for(int i=0;i<20;i++){
			itr.next();
		}
		
		assertTrue(itr.hasNext());
		itr.next();
		assertFalse(itr.hasNext());
	}
	
	private AbstractListBackedResolvedConceptReferencesIterator<Void> create(
			int size) {
		return new AbstractListBackedResolvedConceptReferencesIterator<Void>(
				Arrays.asList(new Void[size])) {

			private static final long serialVersionUID = 1L;

			@Override
			protected ResolvedConceptReference doTransform(Void item) {
				return new ResolvedConceptReference();
			}

		};
	}

}
