
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.AbstractRootNodeResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

public abstract class AbstractRrfRootNodeResolver extends AbstractRootNodeResolver {

	@Override
	protected boolean isHierarchicalRelation(String relation) {
		return RrfLoaderConstants.REL_HIER_RELATIONS.contains(relation) ||
		RrfLoaderConstants.RELA_HIER_RELATIONS.contains(relation);
	}
}