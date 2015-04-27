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
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.pathtoroot;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.transform.AssociationListToTreeNodeList;
import org.LexGrid.LexBIG.Impl.Extensions.tree.transform.Transformer;
import org.LexGrid.LexBIG.Utility.ServiceUtility;

/**
 * The Class LexEvsPathToRootResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsPathToRootResolver implements PathToRootResolver {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5799632517270574621L;
	
	/** The lex big service convenience methods. */
	private LexBIGServiceConvenienceMethods lexBIGServiceConvenienceMethods;
	{
		try {
			lexBIGServiceConvenienceMethods = 
				(LexBIGServiceConvenienceMethods)
				LexBIGServiceImpl.defaultInstance().
				getGenericExtension("LexBIGServiceConvenienceMethods");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** The transformer. */
	private Transformer<AssociationList, List<LexEvsTreeNode>> transformer = new AssociationListToTreeNodeList();

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.pathtoroot.PathToRootResolver#getPathToRoot(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, java.lang.String)
	 */
	public List<LexEvsTreeNode> getPathToRoot(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String code, String codeNamespace) {
		try {
			
			//Path-To-Root processing seems to work best when passing in a CodingSchemeName.
			//TODO: Make the underlying API handle all coding scheme handles for this.
			String version = 
				ServiceUtility.getVersion(codingScheme, versionOrTag);
			
			String codingSchemeName = ServiceUtility.getCodingSchemeName(codingScheme, version);
			
			return transformer.transform(
					lexBIGServiceConvenienceMethods.getHierarchyPathToRoot(
							codingSchemeName, versionOrTag, null, code, codeNamespace, false, HierarchyPathResolveOption.ALL, null));
		} catch (LBException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the transformer.
	 * 
	 * @return the transformer
	 */
	public Transformer<AssociationList, List<LexEvsTreeNode>> getTransformer() {
		return transformer;
	}

	/**
	 * Sets the transformer.
	 * 
	 * @param transformer the transformer
	 */
	public void setTransformer(
			Transformer<AssociationList, List<LexEvsTreeNode>> transformer) {
		this.transformer = transformer;
	}
}
