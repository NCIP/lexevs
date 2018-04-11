package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.lucene.search.Query;
import org.junit.Test;

public class BuildMatchAlgorithmQueryTest {
	



	@Test
	public void testAddOneTestOnly() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.codeExact().lucene();
		BuildMatchAlgorithmQuery bmq = builder.buildMatchQuery();
		Query query = bmq.getQuery();
		assertNotNull(query);
		assertNull(builder.getLucene());
		assertNotNull(builder.getCodeExact());
	}
	
	@Test
	public void testLuceneQuerySelection() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.lucene();
		assertNotNull(builder.getLucene());
		builder.codeExact();
		builder.presentationExact();
		builder.presentationContains();
		builder.propertyExact();
		builder.propertyContains();
		assertNotNull(builder.getLucene());
		assertNull(builder.getPresentationExact());
		assertNull(builder.getPresentationContains());
		assertNull(builder.getPropertyExact());
		assertNull(builder.getPropertyContains());
		assertNull(builder.getCodeExact());
		Query query = builder.buildMatchQuery().getQuery();
		assertNotNull(query);
	}
	
	@Test
	public void testPresentationExactQuerySelection() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.presentationExact();
		assertNotNull(builder.getPresentationExact());
		builder.codeExact();
		builder.presentationContains();
		builder.propertyExact();
		builder.propertyContains();
		builder.lucene();
		assertNull(builder.getLucene());
		assertNotNull(builder.getPresentationExact());
		assertNull(builder.getPresentationContains());
		assertNull(builder.getPropertyExact());
		assertNull(builder.getPropertyContains());
		assertNull(builder.getCodeExact());
		Query query = builder.buildMatchQuery().getQuery();
		assertNotNull(query);
	}
	
	@Test
	public void testPresentationContainsQuerySelection() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.presentationContains();
		assertNotNull(builder.getPresentationContains());
		builder.codeExact();
		builder.presentationExact();
		builder.propertyExact();
		builder.propertyContains();
		builder.lucene();
		assertNull(builder.getLucene());
		assertNull(builder.getPresentationExact());
		assertNotNull(builder.getPresentationContains());
		assertNull(builder.getPropertyExact());
		assertNull(builder.getPropertyContains());
		assertNull(builder.getCodeExact());
		Query query = builder.buildMatchQuery().getQuery();
		assertNotNull(query);
	}
	
	@Test
	public void testPropertyExactQuerySelection() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.propertyExact();
		assertNotNull(builder.getPropertyExact());
		builder.codeExact();
		builder.presentationContains();
		builder.presentationExact();
		builder.propertyContains();
		builder.lucene();
		assertNull(builder.getLucene());
		assertNull(builder.getPresentationExact());
		assertNull(builder.getPresentationContains());
		assertNotNull(builder.getPropertyExact());
		assertNull(builder.getPropertyContains());
		assertNull(builder.getCodeExact());
		Query query = builder.buildMatchQuery().getQuery();
		assertNotNull(query);
	}
	
	@Test
	public void testPropertyContainsQuerySelection() {
		BuildMatchAlgorithmQuery.Builder builder = new BuildMatchAlgorithmQuery.Builder("C48323", false, false, null);
		builder.propertyContains();
		assertNotNull(builder.getPropertyContains());
		builder.codeExact();
		builder.presentationContains();
		builder.presentationExact();
		builder.propertyContains();
		builder.lucene();
		assertNull(builder.getLucene());
		assertNull(builder.getPresentationExact());
		assertNull(builder.getPresentationContains());
		assertNull(builder.getPropertyExact());
		assertNotNull(builder.getPropertyContains());
		assertNull(builder.getCodeExact());
		Query query = builder.buildMatchQuery().getQuery();
		assertNotNull(query);
	}
	
	
	
	
	
	

}
