
package org.lexevs.dao.database.service.listener;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The listener interface for receiving nullEntityNamespace events.
 * The class that is interested in processing a nullEntityNamespace
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addNullEntityNamespaceListener<code> method. When
 * the nullEntityNamespace event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see NullEntityNamespaceEvent
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullEntityNamespaceListener extends AbstractPreEntityInsertValidatingListener{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.listener.AbstractPreEntityInsertValidatingListener#doValidate(java.lang.String, java.lang.String, org.LexGrid.concepts.Entity)
	 */
	protected boolean doValidate(String uri, String version, Entity entity) {

		if(StringUtils.isBlank(entity.getEntityCodeNamespace())){
			CodingScheme cs = LexEvsServiceLocator.getInstance().
			getDatabaseServiceManager().
			getCodingSchemeService().getCodingSchemeByUriAndVersion(uri, version);

			entity.setEntityCodeNamespace(cs.getCodingSchemeName());
		}

		return true;
	}
}