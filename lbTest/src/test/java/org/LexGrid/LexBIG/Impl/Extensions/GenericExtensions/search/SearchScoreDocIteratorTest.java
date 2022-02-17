
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.lucene.search.ScoreDoc;


public class SearchScoreDocIteratorTest extends LexBIGServiceTestCase {

	@Override
	protected String getTestID() {
		return "SearchScoreDocIteratorTest";
	}

	public void testHasNextWithZero() throws Exception {
		SearchScoreDocIterator itr = this.create(0);
		assertFalse(itr.hasNext());
	}
		
	public void testHasNextWithOne() throws Exception {
		SearchScoreDocIterator itr = this.create(1);
		assertTrue(itr.hasNext());
	}
	
	public void testHasNextWithOneAnd() throws Exception {
		SearchScoreDocIterator itr = this.create(1);
		itr.next();
		assertFalse(itr.hasNext());
	}
	
	public void testGetNextList() throws Exception {
		SearchScoreDocIterator itr = this.create(6);
		ResolvedConceptReferenceList list = itr.get(0, 3);
		assertEquals(3, list.getResolvedConceptReferenceCount());
	}

	public void testGetNextSmallerList() throws Exception {
		SearchScoreDocIterator itr = this.create(8);
		ResolvedConceptReferenceList list = itr.get(0, 10);
		assertEquals(8, list.getResolvedConceptReferenceCount());
	}
	
	public void testNext() throws Exception {
		SearchScoreDocIterator itr = this.create(20);
		ResolvedConceptReferenceList list = itr.next(10);
		
		assertEquals(10, list.getResolvedConceptReferenceCount());
	}
	
	public void testNextMore() throws Exception {
		SearchScoreDocIterator itr = this.create(20);
		ResolvedConceptReferenceList list = itr.next(30);
		assertEquals(20, list.getResolvedConceptReferenceCount());
	}
	
	public void testIterate() throws Exception {
		SearchScoreDocIterator itr = this.create(21);
		
		for(int i=0;i<20;i++){
			itr.next();
		}
		
		assertTrue(itr.hasNext());
		itr.next();
		assertFalse(itr.hasNext());
	}
	
	private SearchScoreDocIterator create(
			int size) {

		List<ScoreDoc> list = new ArrayList<ScoreDoc>();
		Set<AbsoluteCodingSchemeVersionReference> refs = new HashSet<AbsoluteCodingSchemeVersionReference>();
		refs.add(Constructors.createAbsoluteCodingSchemeVersionReference(AUTO_URN, AUTO_VERSION));
		
		for (int i = 0; i < size; i++) {
			list.add(new ScoreDoc(1, 2));
		}

		SearchScoreDocIterator ssdi = new SearchScoreDocIterator(refs, list,
				new ScoreDocTransformer() {
					private static final long serialVersionUID = 1L;

					@Override
					public ResolvedConceptReference doTransform(
							Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
							ProxyProtectedScoreDocWrapper item) {
						return new ResolvedConceptReference();
					}

				}) {

			private static final long serialVersionUID = 1L;

		};

		return ssdi;

	}
}