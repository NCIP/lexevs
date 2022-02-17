
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.prefixresolver;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class DefaultLexEvsPrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsPrefixResolver implements PrefixResolver {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8005105567332097673L;

	/* (non-Javadoc)
	 * @see org.lexevs.tree.dao.prefixresolver.PrefixResolver#getPrefix(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	public String getPrefix(String codingSchemeName, CodingSchemeVersionOrTag tagOrVersion) throws Exception {
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
	
		String version = ServiceUtility.getVersion(codingSchemeName, tagOrVersion);
		String codingSchemeUri = systemResourceService.getUriForUserCodingSchemeName(codingSchemeName, version);
                    
		return LexEvsServiceLocator.getInstance().
			getLexEvsDatabaseOperations().getPrefixResolver().
				resolvePrefixForCodingScheme(codingSchemeUri, version);
	}
}