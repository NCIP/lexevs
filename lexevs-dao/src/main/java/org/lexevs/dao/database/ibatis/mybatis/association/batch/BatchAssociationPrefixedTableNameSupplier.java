package org.lexevs.dao.database.ibatis.mybatis.association.batch;

import java.util.List;

import org.lexevs.dao.database.ibatis.AbstractIbatisDao;
import org.lexevs.dao.database.schemaversion.LexGridSchemaVersion;

public class BatchAssociationPrefixedTableNameSupplier extends AbstractIbatisDao {
	
		public static String prefixedTable = null;
		
		public static final String name_property = "entityAssnsToEntity";


	    public static String getPrefixedTable() {
			return prefixedTable;
		}

		public static void setPrefixedTable(String prefixedTable) {
			BatchAssociationPrefixedTableNameSupplier.prefixedTable = prefixedTable;
		}

		public static String getNameProperty() {
			return name_property;
		}

		public static String getResolvedStaticPrefix() {
	       return prefixedTable + name_property;
	    }

		@Override
		public List<LexGridSchemaVersion> doGetSupportedLgSchemaVersions() {
			// TODO Auto-generated method stub
			return null;
		}

}
