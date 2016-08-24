package org.LexGrid.LexBIG.Impl.Extensions.tree.test;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.hierarchy.HierarchyResolver;
import org.LexGrid.naming.SupportedHierarchy;

public class StaticHierarchyResolver implements HierarchyResolver {

	private SupportedHierarchy supportedHierarchy;
	{
		supportedHierarchy = new SupportedHierarchy();
		supportedHierarchy.setAssociationNames(new String[]{"hasSubtype"});
		supportedHierarchy.setLocalId("is_a");
		supportedHierarchy.setRootCode("@");
		supportedHierarchy.setIsForwardNavigable(true);
	}
	
	public SupportedHierarchy[] getHierarchies(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag) {
		return new SupportedHierarchy[]{supportedHierarchy};
	}

	public SupportedHierarchy getHierarchy(String codingScheme,
			CodingSchemeVersionOrTag versionOrTag, String hierarchyId) {
		if(hierarchyId.equals("is_a")){
			return supportedHierarchy;
		} else {
			throw new RuntimeException("Hierarchy Not Found.");
		}
	}
}
