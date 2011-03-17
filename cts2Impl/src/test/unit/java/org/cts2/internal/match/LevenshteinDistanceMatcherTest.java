package org.cts2.internal.match;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LevenshteinDistanceMatcherTest {
	
	private LevenshteinDistanceMatcher levenshteinDistanceMatcher;

	@Before
	public void setUpMatcher(){
		this.levenshteinDistanceMatcher = new LevenshteinDistanceMatcher();
	}
	
	@Test
	public void testMatchNoSimilarLetters(){
		float score = this.levenshteinDistanceMatcher.matchScore("qqqqq", "zzzzzzzz");
		
 		assertEquals(0,score, 0f);
	}
	
	@Test
	public void testMatchHalfSame(){
		float score = this.levenshteinDistanceMatcher.matchScore("aabb", "qqbb");
		
 		assertEquals(0.5f,score, 0f);
	}
	
	@Test
	public void testMatchOneForth(){
		float score = this.levenshteinDistanceMatcher.matchScore("abaa", "bbbb");
		
 		assertEquals(0.25f,score, 0f);
	}
	
	@Test
	public void testMatchOneForthDifferentLengths(){
		float score = this.levenshteinDistanceMatcher.matchScore("b", "bbbb");
		
 		assertEquals(0.25f,score, 0f);
	}
	
	@Test
	public void testNormalize(){
		float score = this.levenshteinDistanceMatcher.normalize(5, 20);
		
 		assertEquals(0.75f,score, 0f);
	}

}
