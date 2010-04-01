package org.lexevs.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.URIMap;
import org.junit.Test;
import org.lexevs.dao.database.utility.DaoUtility;

public class DaoUtilityTest {
	
	@Test
	public void testGetAllURIMaps() {
		Mappings mappings = new Mappings();
		mappings.addSupportedAssociation(new SupportedAssociation());
		mappings.addSupportedCodingScheme(new SupportedCodingScheme());

		List<URIMap> list = DaoUtility.getAllURIMappings(mappings);

		assertEquals(2, list.size());
	}
}
