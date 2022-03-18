
package org.lexgrid.loader.meta;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.UmlsBatchLoader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.lexevs.dao.database.spring.DynamicPropertyApplicationContext;
import org.lexgrid.loader.AbstractSpringBatchLoader;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.properties.ConnectionPropertiesFactory;
import org.lexgrid.loader.properties.impl.DefaultLexEVSPropertiesFactory;
import org.lexgrid.loader.setup.JobRepositoryManager;
import org.lexgrid.loader.staging.StagingManager;
import org.springframework.context.ApplicationContext;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * The Class MetaBatchLoaderImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaBatchLoaderImpl extends AbstractSpringBatchLoader implements MetaBatchLoader {

	private static final long serialVersionUID = 1679334464081067538L;

	/** The connection properties factory. */
	private ConnectionPropertiesFactory connectionPropertiesFactory = new DefaultLexEVSPropertiesFactory();

	/** The MET a_ loade r_ config. */
	private String META_LOADER_CONFIG = "metaLoader.xml";
	
	public MetaBatchLoaderImpl(){
		super();
		super.setDoIndexing(false);
		super.setDoRegister(false);
		super.setDoComputeTransitiveClosure(false);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.meta.MetaBatchLoader#loadMeta(java.lang.String)
	 */
	public void loadMeta(URI rrfDir) throws Exception {
		this.load(rrfDir);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.meta.MetaBatchLoader#resumeMeta(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void resumeMeta(URI rrfDir, String uri, String version) throws Exception {
		Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
		connectionProps.put("rrfDir", rrfDir.toString());
		connectionProps.put("retry", "true");
		launchJob(connectionProps, META_LOADER_CONFIG, "metaJob");
	}

	@Override
	public void removeLoad(String uri, String version) throws LBParameterException {
		 Properties connectionProps = connectionPropertiesFactory.getPropertiesForExistingLoad(uri, version);
			connectionProps.put("retry", "true");
			
			DynamicPropertyApplicationContext ctx = new DynamicPropertyApplicationContext("metaLoaderStaging.xml", connectionProps);
			
			JobRepositoryManager jobRepositoryManager = (JobRepositoryManager)ctx.getBean("jobRepositoryManager");
			try {
				jobRepositoryManager.dropJobRepositoryDatabases();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			StagingManager stagingManager = (StagingManager)ctx.getBean("metaStagingManager");
			try {
				stagingManager.dropAllStagingDatabases();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}		
	}

	public static void main(String[] args) throws Exception { 
		 MetaBatchLoader mbl = (MetaBatchLoader) LexBIGServiceImpl.defaultInstance().getServiceManager(null).getLoader(
					"MetaBatchLoader");
		 mbl.loadMeta(new File("../lbTest/resources/testData/SAMPLEMETA").toURI());
	 }

	  @Override
		public String getName() {
			return UmlsBatchLoader.NAME;
		}

		@Override
		protected OptionHolder declareAllowedOptions(OptionHolder holder) {
			holder.setIsResourceUriFolder(true);
			return holder;
		}

		@Override
		protected URNVersionPair[] doLoad() {
			try {
				Properties connectionProps = connectionPropertiesFactory.getPropertiesForNewLoad();	
				connectionProps.put("rrfDir", this.getResourceUri().toString());
				connectionProps.put("retry", "false");
				launchJob(connectionProps, META_LOADER_CONFIG, "metaJob");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if(this.getStatus().getErrorsLogged()) {
				this.getStatus().setEndTime(new Date(System.currentTimeMillis()));
				return null;
			}
			return this.getLoadedCodingSchemes();
		}

		@Override
		protected URNVersionPair[] getLoadedCodingSchemes(ApplicationContext context) {
			CodingSchemeIdSetter codingSchemeIdSetter = (CodingSchemeIdSetter)context.getBean("metaCodingSchemeIdSetter");
			
			URNVersionPair scheme = new URNVersionPair(
					codingSchemeIdSetter.getCodingSchemeUri(), 
					codingSchemeIdSetter.getCodingSchemeVersion());
			return new URNVersionPair[]{scheme};
		}
		
		@Override
		protected ExtensionDescription buildExtensionDescription() {
			ExtensionDescription meta = new ExtensionDescription();
			meta.setExtensionBaseClass(MetaBatchLoader.class.getName());
			meta.setExtensionClass("org.lexgrid.loader.meta.MetaBatchLoaderImpl");
			meta.setDescription(MetaBatchLoader.DESCRIPTION);
			meta.setName(MetaBatchLoader.NAME);
			meta.setVersion(MetaBatchLoader.VERSION);
			return meta;
		}
}