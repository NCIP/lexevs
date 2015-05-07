/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.tree.dao.pathtoroot;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.lexevs.tree.model.LexEvsTreeNode;

/**
 * The Interface PathToRootResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public interface PathToRootResolver extends Serializable {

	/**
	 * Gets the path to root.
	 * 
	 * @param codingScheme the coding scheme
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * 
	 * @return the path to root
	 */
	public List<LexEvsTreeNode> getPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, 
			String code, String namespace);
}
