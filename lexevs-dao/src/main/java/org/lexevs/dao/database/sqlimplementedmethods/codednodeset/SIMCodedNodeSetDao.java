
package org.lexevs.dao.database.sqlimplementedmethods.codednodeset;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

/**
 * The Class SIMCodedNodeSetDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SIMCodedNodeSetDao extends AbstractBaseDao implements CodedNodeSetDao {
	
	/** The supported datebase version. */
	private LexGridSchemaVersion supportedDatebaseVersionNMinus1 = LexGridSchemaVersion.parseStringToVersion("1.8");
	private LexGridSchemaVersion supportedDatebaseVersionNMinus2 = LexGridSchemaVersion.parseStringToVersion("1.7");

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#buildCodedEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.lexevs.dao.database.access.property.PropertyDao.PropertyType[])
	 */
	public Entity buildCodedEntry(String codingSchemeName, String version,
			String code, String namespace, LocalNameList restrictToProperties,
			PropertyType[] restrictToPropertyTypes) {
		try {
			return null;
			//return SQLImplementedMethods.buildCodedEntry(codingSchemeName, version, code, namespace, restrictToProperties, restrictToPropertyTypes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#getCodingScheme(java.lang.String, java.lang.String)
	 */
	public CodingScheme getCodingScheme(String codingSchemeName,
			String versionString) {
		try {
			return null;
			//return SQLImplementedMethods.buildCodingScheme(codingSchemeName, versionString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#getCodingSchemeCopyright(java.lang.String, java.lang.String)
	 */
	public String getCodingSchemeCopyright(String codingSchemeName,
			String tagOrVersion) {
		try {
			return null;
			//return SQLImplementedMethods.getCodingSchemeCopyright(codingSchemeName, tagOrVersion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.AbstractBaseDao#doGetSupportedLgSchemaVersions()
	 */
	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		List<LexGridSchemaVersion> supportedDBVersions = new ArrayList<LexGridSchemaVersion>();
		supportedDBVersions.add(supportedDatebaseVersionNMinus1);
		supportedDBVersions.add(supportedDatebaseVersionNMinus2);
		return supportedDBVersions;
//		return DaoUtility.createNonTypedList(supportedDatebaseVersion);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao#buildCodedEntry(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.lexevs.dao.database.access.property.PropertyDao.PropertyType[])
	 */
	public Entity buildCodedEntry(
			String codingSchemeName,
			String version,
			String code,
			String namespace,
			LocalNameList restrictToProperties,
			org.lexevs.dao.database.access.property.PropertyDao.PropertyType[] restrictToPropertyTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao#executeInTransaction(org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback)
	 */
	public <T> T executeInTransaction(IndividualDaoCallback<T> callback) {
		throw new UnsupportedOperationException();
	}
}