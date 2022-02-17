
package org.lexevs.tree.dao.pathtoroot;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.HierarchyPathResolveOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.transform.AssociationListToTreeNodeList;
import org.lexevs.tree.transform.Transformer;
import org.LexGrid.LexBIG.Utility.ServiceUtility;

/**
 * The Class LexEvsPathToRootResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
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