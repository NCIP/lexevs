
package org.lexgrid.loader.rrf.factory;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import edu.mayo.informatics.lexgrid.convert.directConversions.UmlsCommon.UMLSBaseCode;

/**
 * A factory for creating IsoMap objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@SuppressWarnings("deprecation")
public class IsoMapFactory extends LoggingBean implements FactoryBean {
	
	/** The system variables. */
	private LexEvsServiceLocator lexEvsServiceLocator;
	
	/** The IS o_ ma p_ fil e_ name. */
	public static String ISO_MAP_FILE_NAME = "UMLS_SAB_ISO_Map.txt";
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getObject() throws Exception {
		Map<String,String> isoMap = UMLSBaseCode.getIsoMap();
		
		String path = this.getPath();
		
		if(path == null){
			this.getLogger().warn("Cound not determine path to user defined UMLS SAB->ISO mappings, using defaults.");
			return isoMap;
		}
		
		Resource resource = new FileSystemResource(
				
				this.getPath()
				
					);
		
		if(!resource.exists()){
			this.getLogger().warn("No user defined UMLS SAB->ISO mappings, using defaults.");
			return isoMap;
		} else {
			this.getLogger().warn("User defined UMLS SAB->ISO mappings found at: " + resource.getFile().getPath());
			
			Properties props = new Properties();
			props.load(resource.getInputStream());
			
			for(Object key : props.keySet()) {
				String sab = (String)key;
				String iso = (String)props.get(key);
				Object oldValue = 
					isoMap.put(sab, iso);
				
				if(oldValue != null) {
					this.getLogger().warn("Old value SAB: " + sab + " ISO: " + oldValue +
						" was replaced with SAB: " + sab + " ISO: " + iso + ".");
				} else {
					this.getLogger().warn("User defined entry SAB: " + sab + " ISO: " + iso +
						" was added.");
				}
			}
			
			return isoMap;
		}
	}
	
	protected String getPath() {
		String configPath = 
			lexEvsServiceLocator.getSystemResourceService().
				getSystemVariables().getConfigFileLocation();
		
		if(configPath == null){
			return null;
		}
		
		return configPath.substring(0, configPath.lastIndexOf(File.separator) + 1) + ISO_MAP_FILE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	public LexEvsServiceLocator getLexEvsServiceLocator() {
		return lexEvsServiceLocator;
	}

	public void setLexEvsServiceLocator(LexEvsServiceLocator lexEvsServiceLocator) {
		this.lexEvsServiceLocator = lexEvsServiceLocator;
	}
}