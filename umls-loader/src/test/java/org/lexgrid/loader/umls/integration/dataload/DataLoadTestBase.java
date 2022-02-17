
package org.lexgrid.loader.umls.integration.dataload;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Before;

import util.integration.LoadUmlsForIntegration;

/**
 * The Class DataLoadTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DataLoadTestBase {
	
	/** The lbs. */
	protected LexBIGService lbs;
	protected CodedNodeSet cns;
	protected CodedNodeGraph cng;
	
	/**
	 * Sets the up lbs.
	 * @throws LBException 
	 */
	@Before
	public void setUpLbs() throws LBException{
		System.setProperty("LG_CONFIG_FILE", LoadUmlsForIntegration.CONFIG_FILE);
		
		//Use this for local testing
		//System.setProperty("LG_CONFIG_FILE", "w:/services/lexbig/5_0_1_BatchLoader/resources/config/lbconfig.props");
		
		lbs = LexBIGServiceImpl.defaultInstance();
		cns = lbs.getCodingSchemeConcepts(LoadUmlsForIntegration.UMLS_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadUmlsForIntegration.UMLS_VERSION));
		cng = lbs.getNodeGraph(LoadUmlsForIntegration.UMLS_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadUmlsForIntegration.UMLS_VERSION), null);
	}
	
	public CodedNodeSet getCodedNodeSet() throws LBException {
		this.setUpLbs();
		return lbs.getCodingSchemeConcepts(LoadUmlsForIntegration.UMLS_URN, Constructors.createCodingSchemeVersionOrTagFromVersion(LoadUmlsForIntegration.UMLS_VERSION));
	}
}