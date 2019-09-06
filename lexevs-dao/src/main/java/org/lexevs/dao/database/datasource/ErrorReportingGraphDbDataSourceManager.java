package org.lexevs.dao.database.datasource;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.log4j.LogManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.Logger;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.service.CodingSchemeAliasHolder;
import org.lexevs.system.service.SystemResourceService.CodingSchemeMatcher;
import org.springframework.beans.factory.InitializingBean;

import com.arangodb.ArangoDB;

public class ErrorReportingGraphDbDataSourceManager implements InitializingBean {
	private LgLoggerIF logger;
	private boolean strictArangoRequirement;
	private ConcurrentHashMap<String, GraphDbDataSourceInstance> graphDbCache = 
			new ConcurrentHashMap<String, GraphDbDataSourceInstance>();

	
	

	@Override
	public void afterPropertiesSet() throws Exception {
//
//		String url = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getGraphdbUrl();
//		String port = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables().getGraphdbPort();
//		logger.info("Connecting to graph database");
		System.out.println("Starting Graph Database Source Manager");
//		ArangoDB db = new ArangoDB.Builder().host(url, Integer.getInteger(port)).build();
//		if(db == null && strictArangoRequirement){
//			logger.fatal("Unable to connect to ArangoDb at: " + url + ":" + port);
//			throw new RuntimeException("Unable to connect to ArangoDb at: " + url + ":" + port);
//		}
//		else{
//			logger.warn("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
//					+ "Continuing with LexEVS db support only");
//			System.out.println("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
//					+ "Continuing with LexEVS db support only");
//		}
//		db.getAccessibleDatabases().stream().forEach(x -> System.out.println(x));
	}
	
	public GraphDbDataSourceInstance getDataSource(String schemeUri){
		if(graphDbCache.containsKey(schemeUri)){
			return graphDbCache.get(schemeUri);
		}
		else{
			GraphDbDataSourceInstance gbDataSource = getFreshDataSource(schemeUri);
			graphDbCache.put(schemeUri, gbDataSource);
			return gbDataSource;
		}
	}
	
	private GraphDbDataSourceInstance getFreshDataSource(String schemeUri){
		List<RegistryEntry> entries = LexEvsServiceLocator
		.getInstance()
		.getRegistry()
		.getAllRegistryEntriesOfTypeAndURI(
				ResourceType.CODING_SCHEME, schemeUri);
		RegistryEntry entry;
		if(entries.stream().anyMatch(x -> x.getTag().equals("PRODUCTION"))){
		entry =  entries
				.stream()
				.filter(x -> x.getTag()
						.equals("PRODUCTION"))
				.collect(Collectors.toList())
				.get(0);}
		else{
			List<Timestamp> tmstp = entries.stream().map(x -> x.getLastUpdateDate()).collect(Collectors.toList());
			tmstp.sort((e1, e2)-> e1.compareTo(e2));
			entry = entries
					.stream()
					.filter(x -> x.getLastUpdateDate().equals(tmstp.get(0)))
					.collect(Collectors.toList())
					.get(0);}
		String name = null;
		try {
			name = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(entry.getResourceUri(), entry.getResourceVersion());
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new GraphDbDataSourceInstance(name);
	}
	
	
	public RegistryEntry getProductionEntry(List<RegistryEntry> list){
		return  list
				.stream()
				.filter(x -> x.getTag().equals("PRODUCTION")).
				findFirst().
				get();

	}
	
	public RegistryEntry getLatestUpdateEntry(List<RegistryEntry> list){
		List<Timestamp> tmstp = list.stream().map(x -> x.getLastUpdateDate()).collect(Collectors.toList());
		tmstp.sort((e1, e2)-> e1.compareTo(e2));
		
		 return list
		.stream()
		.filter(x -> x.getLastUpdateDate().equals(tmstp.get(0)))
		.collect(Collectors.toList())
		.get(0);
		}

	/**
	 * @return the strictArangoRequirement
	 */
	public boolean isStrictArangoRequirement() {
		return strictArangoRequirement;
	}

	/**
	 * @param strictArangoRequirement the strictArangoRequirement to set
	 */
	public void setStrictArangoRequirement(boolean strictArangoRequirement) {
		this.strictArangoRequirement = strictArangoRequirement;
	}

	/**
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

}
