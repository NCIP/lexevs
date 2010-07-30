/**
 * 
 */
package org.lexevs.cts2.query;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
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
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextCodedNodeSet(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodedNodeSet getUsageContextCodedNodeSet(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextCodedNodeSet(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextCodingScheme(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingScheme getUsageContextCodingScheme(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextCodingScheme(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextEntitisWithName(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getUsageContextEntitisWithName(String usageContextName,
			CodingSchemeVersionOrTag versionOrTag,
			SearchDesignationOption option, String matchAlgorithm,
			String language) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextEntitisWithName(usageContextName, versionOrTag, option, matchAlgorithm, language);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#getUsageContextEntity(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public Entity getUsageContextEntity(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSUsageContextServices().getUsageContextEntity(usageContextId, versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#listAllUsageContextEntities(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<Entity> listAllUsageContextEntities(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSUsageContextServices().listAllUsageContextEntities(versionOrTag);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.query.UsageContextQueryOperation#listAllUsageContextIds(org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> listAllUsageContextIds(
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexEVSUsageContextServices().listAllUsageContextIds(versionOrTag);
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
