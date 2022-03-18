
package org.lexgrid.loader.umls.data.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.LexGrid.concepts.Presentation;
import org.junit.Test;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.rrf.data.property.MrrankUtility;
import org.lexgrid.loader.rrf.model.Mrrank;

/**
 * The Class EntityPropertyPropertyComparatorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyPropertyComparatorTest {

	/**
	 * Test compare by same language.
	 */
	@Test
	public void testCompareBySameLanguage(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();	

		prop1.setLanguage("ENG");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
		prop1.setValue(DaoUtility.createText("test"));
		
		Presentation prop2 = new Presentation();		
		prop2.setLanguage("ENG");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
		prop2.setValue(DaoUtility.createText("test"));
		
		
		
		assertTrue(comparator.compare(prop1, prop2) == 0);
	}
	
	/**
	 * Test compare by different language.
	 */
	@Test
	public void testCompareByDifferentLanguage(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();	
		prop1.setLanguage("ENG");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");

		prop1.setIsPreferred(false);
		
		Presentation prop2 = new Presentation();	
		prop2.setLanguage("FR");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
		prop2.setIsPreferred(false);
		
	
		assertTrue(comparator.compare(prop1, prop2) == 1);
	}
	
	/**
	 * Test compare by rank.
	 */
	@Test
	public void testCompareByRank(){
		
		EntityPropertyPropertyComparator comparator = new EntityPropertyPropertyComparator();
		
		Presentation prop1 = new Presentation();	
		prop1.setLanguage("ENG");
		prop1.setRepresentationalForm("SSN");
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
		prop1.setIsPreferred(false);
		
		Presentation prop2 = new Presentation();
		prop2.setLanguage("ENG");
		prop2.setRepresentationalForm("VSY");
		prop2.setPropertyType("test");
		prop2.setPropertyName("test");
		prop2.setIsPreferred(false);
			
		comparator.setMrrankUtility(new MrrankTestUtility());
		comparator.setSab("SRC");
	
		assertEquals(1, comparator.compare(prop1, prop2));
	}
	
	/**
	 * The Class MrrankTestUtility.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class MrrankTestUtility implements MrrankUtility {

		/* (non-Javadoc)
		 * @see org.lexgrid.loader.rrf.data.property.MrrankUtility#getRank(java.lang.String, java.lang.String)
		 */
		public int getRank(String sab, String tty) {
			if(sab.equals("SRC") && tty.equals("SSN")){
				return 1;
			} else if(sab.equals("SRC") && tty.equals("VSY")){
				return 2;
			}
			throw new RuntimeException("Test MRRANK utility cannot process this sab and tty.");
		}

		public Mrrank getMrrank(String sab, String tty) {
			return null;
		}
	}
	

}