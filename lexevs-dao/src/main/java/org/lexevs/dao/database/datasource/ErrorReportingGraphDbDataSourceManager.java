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
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.CodingSchemeAliasHolder;
import org.lexevs.system.service.SystemResourceService.CodingSchemeMatcher;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;

public class ErrorReportingGraphDbDataSourceManager implements InitializingBean {
	private LgLoggerIF logger;
	private boolean strictArangoRequirement;
	private SystemVariables systemVariables;
	private ConcurrentHashMap<String, GraphDbDataSourceInstance> graphDbCache = 
			new ConcurrentHashMap<String, GraphDbDataSourceInstance>();

	
	

	@Override
	public void afterPropertiesSet() throws Exception {

		logger.info("Connecting to graph database");
		System.out.println("Starting Graph Database Source Manager");
		String url = systemVariables.getGraphdbUrl();
		String port = systemVariables.getGraphdbPort();
		String user = systemVariables.getGraphdbUser();
		String password = systemVariables.getGraphdbpwd();
		ArangoDB db = null;
		try {
			db = new ArangoDB.Builder().host(url, Integer.valueOf(port).intValue()).user(user).password(password)
					.build();
		} catch (BeanCreationException | ArangoDBException e) {
			if (strictArangoRequirement) {
				System.out.println("Unable to connect to ArangoDb at: " + url + ":" + port);
				logger.error("Unable to connect to ArangoDb at: " + url + ":" + port);
				throw new RuntimeException("Unable to connect to ArangoDb at: " + url + ":" + port);
			}
			logger.warn("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
					+ "Continuing with LexEVS db support only");
			System.out.println("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
					+ "Continuing with LexEVS db support only");
			return;
		}
		if (db == null) {
			System.out.println("Unable to connect to ArangoDb at: " + url + ":" + port);
			logger.error("Unable to connect to ArangoDb at: " + url + ":" + port);
			if (strictArangoRequirement) {
				throw new RuntimeException("Unable to connect to ArangoDb at: " + url + ":" + port);
			} else {
				logger.warn("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
						+ "Continuing with LexEVS db support only");
				System.out.println("Unable to connect to ArangoDb at: " + url + ":" + port + ". "
						+ "Continuing with LexEVS db support only");
			}

		}
		System.out.println("Displaying loaded graph databases");
		try {
			db.getAccessibleDatabases().stream().forEach(x -> System.out.println(x));
		} catch (ArangoDBException e) {
			logger.warn("Able to connect to ArangoDb at: " + url + ":" + port + ". " + "but an error occurred", e);
			System.out.println("Able to connect to ArangoDb at: " + url + ":" + port + ". " + "but an error occurred");
			e.printStackTrace();
		} finally {
			if(db != null)
			{db.shutdown();}
		}
	}
	
	public GraphDbDataSourceInstance getDataSource(String schemeUri){
		if(schemeUri == null)
			{throw new RuntimeException("Cannot create data source when scheme URI is null");}
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

		entry =  entries
				.stream()
				.filter(x -> x.getTag() != null && x.getTag()
						.equals("PRODUCTION"))
				.collect(Collectors.toList())
				.get(0);
			if(entry == null){
			List<Timestamp> tmstp = entries.stream().map(x -> x.getLastUpdateDate()).collect(Collectors.toList());
			tmstp.sort((e1, e2)-> e1.compareTo(e2));
			entry = entries
					.stream()
					.filter(x -> x.getLastUpdateDate().equals(tmstp.get(0)))
					.collect(Collectors.toList())
					.get(0);
			}
		String name = null;
		try {
			name = LexEvsServiceLocator
					.getInstance()
					.getSystemResourceService()
					.getInternalCodingSchemeNameForUserCodingSchemeName(
							entry.getResourceUri(), entry.getResourceVersion());
		} catch (LBParameterException e) {
			e.printStackTrace();
		}
		
		return new GraphDbDataSourceInstance(name);
	}
	
	
	public RegistryEntry getProductionEntry(List<RegistryEntry> list){
		if(list == null || list.size() == 0)
		{ throw new RuntimeException("Registry Entry list has no information, cannot return PRODUCTION entry"); }
		return  list
				.stream()
				.filter(x -> x.getTag().equals("PRODUCTION")).
				findFirst().
				get();

	}
	
	public RegistryEntry getLatestUpdateEntry(List<RegistryEntry> list){
		if(list == null || list.size() == 0)
		{ throw new RuntimeException("Registry Entry list has no information, cannot return latest entry"); }
		List<Timestamp> tmstp = list.stream().map(x -> x.getLastUpdateDate()).collect(Collectors.toList());
		tmstp.sort((e1, e2)-> e1.compareTo(e2));
		
		 return list
		.stream()
		.filter(x -> x.getLastUpdateDate().equals(tmstp.get(0)))
		.collect(Collectors.toList())
		.get(0);
		}
	
	public void removeDataSource(String schemeUri){
		if(schemeUri == null)
		{throw new RuntimeException("Cannot drop data source when scheme URI is null");}
		if(graphDbCache.containsKey(schemeUri)){
			graphDbCache.get(schemeUri).dropGraphsAndDatabaseForDataSource();
			graphDbCache.remove(schemeUri);
		}
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

	/**
	 * @return the systemVariables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * @param systemVariables the systemVariables to set
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

}
