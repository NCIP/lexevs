package org.cts2.internal.match;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RegexMatcherTest {
	
	private RegexMatcher matcher;

	@Before
	public void setUpMatcher(){
		this.matcher = new RegexMatcher();
	}
	
	@Test
	public void noMatch(){
		float score = this.matcher.matchScore("some*.text", "other stuff");
		
 		assertEquals(0f,score, 0f);
	}
	
	@Test
	public void exactMatch(){
		float score = this.matcher.matchScore("some", "some");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void exactMatchMultiWord(){
		float score = this.matcher.matchScore("some.*text", "some text");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void noWildcard(){
		float score = this.matcher.matchScore("string", "some text in a string");
		
 		assertEquals(0f,score, 0f);
	}
	
	@Test
	public void numbers(){
		float score = this.matcher.matchScore("hello\\d\\d", "hello92");
		
 		assertEquals(1f,score, 0f);
	}
	
	@Test
	public void numbersWrong(){
		float score = this.matcher.matchScore("hello\\d", "hello92");
		
 		assertEquals(0f,score, 0f);
	}
}
