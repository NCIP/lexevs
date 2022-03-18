
package org.lexevs.dao.database.service.listener;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;

/**
 * The listener interface for receiving invalidPropertyLink events.
 * The class that is interested in processing a invalidPropertyLink
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addInvalidPropertyLinkListener<code> method. When
 * the invalidPropertyLink event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see InvalidPropertyLinkEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InvalidPropertyLinkListener extends AbstractPreEntityInsertValidatingListener{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.AbstractPreEntityInsertValidatingListener#doValidate(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	protected boolean doValidate(String uri, String version, Entity entity) {
		
		Property[] properties = entity.getProperty();
		List<PropertyLink> propLinkList = entity.getPropertyLinkAsReference();
		List<PropertyLink> validList = new ArrayList<PropertyLink>();

		for(PropertyLink propLink : propLinkList) {
			boolean srcFlag = false, tgtFlag = false;
			for (Property property : properties) {
				if (srcFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getSourceProperty()))
					srcFlag = true;
				if (tgtFlag == false && property.getPropertyId().equalsIgnoreCase(propLink.getTargetProperty()))
					tgtFlag = true;
			}
			if (srcFlag == true && tgtFlag == true)
				validList.add(propLink);
		}

		entity.setPropertyLink(validList);

		return true;
	}
}