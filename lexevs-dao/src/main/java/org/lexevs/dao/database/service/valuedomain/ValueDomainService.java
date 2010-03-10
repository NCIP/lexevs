package org.lexevs.dao.database.service.valuedomain;

import java.util.List;

import org.LexGrid.valueDomains.ValueDomainDefinition;
import org.LexGrid.valueDomains.ValueDomains;
import org.lexevs.dao.database.service.DatabaseService;

public interface ValueDomainService extends DatabaseService {

	public void insertValueDomainDefinition(ValueDomainDefinition definition);
	
	public void insertValueDomains(ValueDomains valueDomains, String systemReleaseUri);
	
	public List<String> findByValueUrisByDomainName(String valueDomainName);
	
	public ValueDomainDefinition getValueDomainDefinitionByUri(String uri);
}
