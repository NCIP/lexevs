package org.lexevs.dao.database.ibatis.tablemetadata;

import org.lexevs.dao.database.access.tablemetadata.TableMetadataDao;
import org.lexevs.dao.database.ibatis.tablemetadata.parameter.InsertTableMetadataBean;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisTableMetadataDao extends SqlMapClientDaoSupport implements TableMetadataDao {
	
	private PrefixResolver prefixResolver;
	
	public String insertVersionAndDescription(String version, String description) {
		InsertTableMetadataBean bean = new InsertTableMetadataBean();
		bean.setPrefix(prefixResolver.resolveDefaultPrefix());
		bean.setDescription(description);
		bean.setVersion(version);
		
		return (String) this.getSqlMapClientTemplate().insert("insertTableMetadata", bean);
	}
	
	public String getDescription() {
		return (String) this.getSqlMapClientTemplate().queryForObject("getDescription",
				prefixResolver.resolveDefaultPrefix());
	}

	public String getVersion() {
		return (String) this.getSqlMapClientTemplate().queryForObject("getVersion",
				prefixResolver.resolveDefaultPrefix());
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}
}
