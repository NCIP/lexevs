
package org.lexgrid.loader.dao.template;

import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.data.codingScheme.SimpleCodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * The Class CachingSupportedAttribuiteTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplate extends AbstractSupportedAttributeTemplate implements JobExecutionListener, StepExecutionListener {
	
	private DatabaseServiceManager databaseServiceManager;

	/** The attribute cache. */
	private ConcurrentHashMap<String,CodingSchemeIdHolder<URIMap>> attributeCache = new ConcurrentHashMap<String,CodingSchemeIdHolder<URIMap>>();

	
	@Override
	public void afterJob(JobExecution arg0) {
		this.flushCache();
	}

	@Override
	public void beforeJob(JobExecution arg0) {
		//
	}
	
	@Override
	public ExitStatus afterStep(StepExecution step) {
		this.flushCache();
		
		return step.getExitStatus();
	}

	@Override
	public void beforeStep(StepExecution step) {
		this.flushCache();
	}	

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#insert(org.LexGrid.persistence.model.CodingSchemeSupportedAttrib)
	 */
	@Override
	protected void insert(String codingSchemeUri, String codingSchemeVersion, final URIMap uriMap){
	
		String key = this.buildCacheKey(uriMap);
		
		CodingSchemeIdHolder<URIMap> holder = new CodingSchemeIdHolder<URIMap>(
					createCodingSchemeIdSetter(codingSchemeUri, codingSchemeVersion), uriMap);

		attributeCache.putIfAbsent(key, holder);
	}
	
	protected CodingSchemeIdSetter createCodingSchemeIdSetter(String uri, String version) {
		return new SimpleCodingSchemeIdSetter(uri,version);
	}

	public void flushCache() {
		this.getLogger().info("Flushing SupportedAttribute Cache.");
		
		this.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<Void>() {

			@Override
			public Void execute(DaoManager daoManager) {
				Enumeration<CodingSchemeIdHolder<URIMap>> elements = attributeCache.elements();

				while(elements.hasMoreElements()) {
					CodingSchemeIdHolder<URIMap> element = elements.nextElement();
					String codingSchemeUri = element.getCodingSchemeIdSetter().getCodingSchemeUri();
					String codingSchemeVersion = element.getCodingSchemeIdSetter().getCodingSchemeVersion();

					CodingSchemeDao dao = 
						daoManager.getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);

					String codingSchemeUid = dao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);

					dao.insertOrUpdateURIMap(codingSchemeUid, element.getItem());
				}
				
				return null;
			}
		});
	}
	
	protected String buildCacheKey(URIMap map){
		return map.getClass().getName() +
			map.getContent() +
			map.getLocalId() +
			map.getUri();
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}

	protected Map<String,CodingSchemeIdHolder<URIMap>> getAttributeCache() {
		return this.attributeCache;
	}
}