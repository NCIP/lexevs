/**
 * 
 */
package org.lexevs.cts2.author;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Versionable;
import org.lexevs.cts2.core.update.RevisionInfo;

/**
 * @author m004181
 *
 */
public class UsageContextAuthoringOperationImpl implements
		UsageContextAuthoringOperation {

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#activateUsageContext(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean activateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo)
			throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#addUsageContextProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean addUsageContextProperty(String usageContextId,
			Property newProperty, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#createUsageContext(java.lang.String, java.lang.String, org.lexevs.cts2.core.update.RevisionInfo, java.lang.String, java.lang.String, boolean, org.LexGrid.commonTypes.Properties, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public String createUsageContext(String usageContextId,
			String usageContextName, RevisionInfo revisionInfo,
			String description, String status, boolean isActive,
			Properties properties, CodingSchemeVersionOrTag versionOrTag)
			throws LBException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#deactivateUsageContext(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean deactivateUsageContext(String usageContextId,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revisionInfo)
			throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#removeUsageContextProperty(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean removeUsageContextProperty(String usageContextId,
			String propertyId, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextProperty(java.lang.String, org.LexGrid.commonTypes.Property, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextProperty(String usageContextId,
			Property changedProperty, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revision) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextStatus(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextStatus(String usageContextId,
			String newStatus, CodingSchemeVersionOrTag versionOrTag,
			RevisionInfo revisionInfo) throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.author.UsageContextAuthoringOperation#updateUsageContextVersionable(java.lang.String, org.LexGrid.commonTypes.Versionable, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.lexevs.cts2.core.update.RevisionInfo)
	 */
	@Override
	public boolean updateUsageContextVersionable(String usageContextId,
			Versionable changedVersionable,
			CodingSchemeVersionOrTag versionOrTag, RevisionInfo revision)
			throws LBException {
		// TODO Auto-generated method stub
		return false;
	}

}
