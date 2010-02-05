package org.lexevs.dao.database.sqlimplementedmethods.codednodeset;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.Impl.dataAccess.SQLImplementedMethods;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.access.AbstractBaseDao;
import org.lexevs.dao.database.access.codednodeset.CodedNodeSetDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;
import org.lexevs.dao.database.utility.DaoUtility;

public class SIMCodedNodeSetDao extends AbstractBaseDao implements CodedNodeSetDao {
	
	private LexGridSchemaVersion supportedDatebaseVersion = LexGridSchemaVersion.parseStringToVersion("1.8");

	public Entity buildCodedEntry(String codingSchemeName, String version,
			String code, String namespace, LocalNameList restrictToProperties,
			PropertyType[] restrictToPropertyTypes) {
		try {
			return SQLImplementedMethods.buildCodedEntry(codingSchemeName, version, code, namespace, restrictToProperties, restrictToPropertyTypes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CodingScheme getCodingScheme(String codingSchemeName,
			String versionString) {
		try {
			return SQLImplementedMethods.buildCodingScheme(codingSchemeName, versionString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	public String getCodingSchemeCopyright(String codingSchemeName,
			String tagOrVersion) {
		try {
			return SQLImplementedMethods.getCodingSchemeCopyright(codingSchemeName, tagOrVersion);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
		return DaoUtility.createList(supportedDatebaseVersion, LexGridSchemaVersion.class);
	}
}
