
package org.lexevs.dao.database.service.listener;

import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.junit.Test;

public class DuplicatePropertyIdListenerTest extends LexBIGServiceTestCase {

	@Test
	public void testForNullPropertyId() {
		DuplicatePropertyIdListener listener = new DuplicatePropertyIdListener();
		
		Entity entity = new Entity();
		
		Property prop = new Property();
		
		entity.addProperty(prop);

		listener.doValidate("", "", entity);
	}

	@Override
	protected String getTestID() {
		return DuplicatePropertyIdListenerTest.class.getName();
	}

}