/**
 * 
 */
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.impl.LexEVSUsageContextServicesImpl;

/**
 * Implementation of LexEVS CTS2 Usage Context Query Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class UsageContextQueryOperationImpl implements
		UsageContextQueryOperation {
	private transient LexEVSUsageContextServices ucServ_;
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextCodedNodeSet(java.lang.String, java.lang.String)
	 */
	@Override
	public CodedNodeSet getUsageContextCodedNodeSet(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextCodedNodeSet(codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextCodingScheme(java.lang.String, java.lang.String)
	 */
	@Override
	public CodingScheme getUsageContextCodingScheme(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextCodingScheme(codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextEntitisWithName(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getUsageContextEntitisWithName(String usageContextName, String codeSystemNameOrURI, String codeSystemVersion, 
			SearchDesignationOption option, String matchAlgorithm, String language) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextEntitisWithName(usageContextName, codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), option, matchAlgorithm, language);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextEntity(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Entity getUsageContextEntity(String usageContextId, String namespace, String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, namespace, codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#listAllUsageContextEntities(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> listAllUsageContextEntities(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexEVSUsageContextServices().listAllUsageContextEntities(codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#listAllUsageContextIds(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> listAllUsageContextIds(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexEVSUsageContextServices().listAllUsageContextIds(codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion));
	}
	
	/**
	 * Gets the LexEVS usage context services.
	 * 
	 * @return the LexEVS usage context services
	 */
	private LexEVSUsageContextServices getLexEVSUsageContextServices() {
		if (ucServ_ == null)
			ucServ_ = LexEVSUsageContextServicesImpl.defaultInstance();
		
		return ucServ_;
	}
}
