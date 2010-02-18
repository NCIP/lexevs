package org.lexevs.dao.database.prefix;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.dao.test.StaticPrefixResolver;
import org.springframework.test.annotation.ExpectedException;

public class CyclingCharDbPrefixGeneratorTest {

	@Test
	public void testAdjustLengthChop(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		char[] chars = generator.adjustLength("aasdffdsaer".toCharArray());

		assertEquals(3, chars.length);
		assertArrayEquals("aas".toCharArray(), chars);
	}
	
	@Test
	public void testIsInCycleOneCharTrue(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(1);
		
		assertTrue(generator.isInCycle(new char[]{'Z'}));
	}
	
	@Test
	public void testIsInCycleOneCharFalse(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		assertFalse(generator.isInCycle(new char[]{'E'}));
	}
	
	@Test
	public void testIsInCycleMultipleCharsTrue(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(10);
		
		assertTrue(generator.isInCycle("AEDGRYNGRZ".toCharArray()));
	}
	
	@Test
	public void testIsInCycleMultipleCharsFalse(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(10);
		
		assertFalse(generator.isInCycle("EGHHYTVVHH".toCharArray()));
	}
	
	@Test
	public void testGenerateStartingCyclingPrefix(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(3);
		
		char[] chars = generator.generateStartingCyclingPrefix();

		assertEquals(3, chars.length);
		assertArrayEquals("AAZ".toCharArray(), chars);
	}
	
	@Test
	public void testAdjustLengthAdd(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(5);
		
		char[] chars = generator.adjustLength("ff".toCharArray());

		assertEquals(5, chars.length);
		assertArrayEquals("ffAAA".toCharArray(), chars);
	}
	
	@Test
	public void testGetPrefixWithoutCycle(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(4);
		
		String newPrefix = generator.generateNextDatabasePrefix("aaaa");
		
		assertEquals("baaa", newPrefix);
	}
	
	@Test
	public void testGetPrefixWithoutCycleOneZ(){
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		generator.setPrefixLengthLimit(4);
		
		String newPrefix = generator.generateNextDatabasePrefix("zaaa");
		
		assertEquals("zbaa", newPrefix);
	}
	
	@Test
	public void testGetPrefixWithCycle(){

		
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		
		generator.setPrefixLengthLimit(4);
		generator.setPrefixResolver(new StaticPrefixResolver("$$"));
		
		
		DatabaseUtility dbUtil = createMock(DatabaseUtility.class);
		expect(dbUtil.doesTableExist("$$aaaz" + generator.getTestDatabaseName())).andReturn(true).anyTimes();
		expect(dbUtil.doesTableExist("$$baaz" + generator.getTestDatabaseName())).andReturn(true).anyTimes();
		expect(dbUtil.doesTableExist("$$caaz" + generator.getTestDatabaseName())).andReturn(true).anyTimes();
		expect(dbUtil.doesTableExist("$$daaz" + generator.getTestDatabaseName())).andReturn(false).anyTimes();
		replay(dbUtil);
		
		generator.setDatabaseUtility(dbUtil);
		
		String newPrefix = generator.generateNextDatabasePrefix("zzzz");
		
		assertEquals("daaz", newPrefix);
	}
	
	@Test
	@ExpectedException(RuntimeException.class)
	public void testNoMorePrefixes(){
		
		PrefixResolver resolver = createMock(PrefixResolver.class);
		expect(resolver.resolveDefaultPrefix()).andReturn("$$").anyTimes();
		replay(resolver);
		
		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		
		generator.setPrefixLengthLimit(4);
		generator.setPrefixResolver(new StaticPrefixResolver("$$"));
		
		DatabaseUtility dbUtil = createMock(DatabaseUtility.class);
		expect(dbUtil.doesTableExist((String)EasyMock.anyObject())).andReturn(true).anyTimes();
		replay(dbUtil);
		
		generator.setDatabaseUtility(dbUtil);
		
		String newPrefix = generator.generateNextDatabasePrefix("a");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTooSmallPrefix() throws Exception {

		CyclingCharDbPrefixGenerator generator = new CyclingCharDbPrefixGenerator();
		
		generator.setPrefixLengthLimit(1);
		
		generator.afterPropertiesSet();
	}
	
}
