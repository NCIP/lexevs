
package org.lexevs.dao.database.service.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;

/**
 * The listener interface for receiving duplicatePropertyId events.
 * The class that is interested in processing a duplicatePropertyId
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDuplicatePropertyIdListener<code> method. When
 * the duplicatePropertyId event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see DuplicatePropertyIdEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DuplicatePropertyIdListener extends AbstractPreEntityInsertValidatingListener {
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.AbstractPreEntityInsertValidatingListener#doValidate(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	@Override
	protected boolean doValidate(String uri, String version, Entity entity) {
		Property[] props = entity.getProperty();
		List<Property> validList = new ArrayList<Property>();
		List<String> propIdList = new ArrayList<String>();
		
		for (Property prop : props) {
			if(prop.getPropertyId() == null || prop.getPropertyId().length()==0) {return true; }
			if (!propIdList.contains(prop.getPropertyId().toLowerCase())) {
				validList.add(prop);
				propIdList.add(prop.getPropertyId().toLowerCase());
			}
		}
		entity.setProperty(validList);

		return true;
	}
}