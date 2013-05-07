
package org.LexGrid.custom.concepts;

/**
 * @author Zonghui Lian
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

/**
 * Common utility class to support concepts and related model objects.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas M Johnson</A>
 * @non-generated
 */
public class EntitiesUtil {

	/**
	 * Returns the first matching property object for a concept that matches the
	 * given propertyName. Returns null if there is no match.
	 * 
	 * @param CodedEntry,
	 *           propertyName
	 * @return property
	 */
	public static Property resolveProperty(Entity entity, String propertyName) {
		for (Property p : entity.getAllProperties()) {
			if (p.getPropertyName().equals(propertyName))
				return p;
		}
		return null;
	}

	/**
	 * Returns the list of property object for a Entity that matches the given
	 * propertyName.
	 * 
	 * @param CodedEntry,
	 *           propertyName
	 * @return List of properties
	 */
	public static List<Property> resolveProperties(Entity entity, String propertyName) {
		List<Property> list = new ArrayList<Property>();
		
		for (Property p : entity.getAllProperties()) {
			if (p.getPropertyName().equals(propertyName)) {
				list.add(p);
			}
		}
		
		return list;
	}

	/**
	 * Returns the synonym presentation object for a entity
	 * 
	 * @param CodedEntry
	 * @return List of presentation
	 */
	public static List<Presentation> getNonPreferredPresentation(Entity entity) {
		List<Presentation> list = new ArrayList<Presentation>();
		for(Presentation presentation : entity.getPresentation()) {
			if (presentation.getIsPreferred() == null || !presentation.getIsPreferred().booleanValue()) {
				list.add(presentation);
			}
		}
		return list;
	}
}