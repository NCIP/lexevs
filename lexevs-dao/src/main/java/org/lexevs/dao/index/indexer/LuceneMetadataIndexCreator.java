
package org.lexevs.dao.index.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.index.access.IndexDaoManager;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.metadata.BaseMetaDataLoader;

public class LuceneMetadataIndexCreator implements MetadataIndexCreator {

	private BaseMetaDataLoader baseMetaDataLoader;
	private IndexDaoManager indexDaoManager;
	
	@Override
	public void indexMetadata(
			String codingSchemeUri,
			String codingSchemeVersion, 
			URI metaDataLocation,
			boolean appendToExistingMetaData) throws Exception {
		MetadataDao metadataDao = indexDaoManager.getMetadataDao();
		
		if(!appendToExistingMetaData) {
			indexDaoManager.getMetadataDao().removeMetadata(codingSchemeUri, codingSchemeVersion);
		}
		
		List<org.apache.lucene.document.Document> documents = this.loadContent(
				codingSchemeUri,
				codingSchemeVersion,
				metaDataLocation);
		
		metadataDao.
			addDocuments(
					codingSchemeUri, 
					codingSchemeVersion, 
					documents, 
					BaseMetaDataLoader.getMetadataAnalyzer());
	}

	 @SuppressWarnings("unchecked")
	    private List<org.apache.lucene.document.Document> loadContent(
	    		String codingSchemeUri,
				String codingSchemeVersion, 
				URI metaDataLocation) throws Exception {
	    	List<org.apache.lucene.document.Document> returnList = new ArrayList<org.apache.lucene.document.Document>();
	    	
	        BufferedReader reader = null;
	        if (metaDataLocation.getScheme().equals("file")) {
	            reader = new BufferedReader(new FileReader(new File(metaDataLocation)));
	        } else {
	            reader = new BufferedReader(new InputStreamReader(metaDataLocation.toURL().openConnection()
	                    .getInputStream()));
	        }

	        SAXBuilder saxBuilder = new SAXBuilder();
	        Document document = saxBuilder.build(reader);

	        Element root = document.getRootElement();

	        ArrayList<String> parentPath = new ArrayList<String>();

	        String name = root.getName();
	        String value = root.getTextTrim();
	        parentPath.add(name);

	        if (value != null && value.length() > 0) {
	        	returnList.add(baseMetaDataLoader.addProperty(
	        			codingSchemeUri,
	        			codingSchemeVersion,
	        			null, 
	        			name, 
	        			value));
	        }

	        returnList.addAll(processAttributes(
	        		codingSchemeUri,
        			codingSchemeVersion,parentPath, 
        			root.getAttributes()));
	        returnList.addAll(processChildren(
	        		codingSchemeUri,
        			codingSchemeVersion,
        			parentPath, 
        			root.getChildren()));
	        
	        return returnList;
	    }

	    private List<org.apache.lucene.document.Document> processAttributes(String codingSchemeUri,
				String codingSchemeVersion, ArrayList<String> parentPath, List<Attribute> attributeList) throws Exception {
	    	List<org.apache.lucene.document.Document> returnList = new ArrayList<org.apache.lucene.document.Document>();
	    	
	    	for (Iterator<Attribute> attIter = attributeList.iterator(); attIter.hasNext();) {
	            Attribute currentAttribute = (Attribute) attIter.next();

	            String name = currentAttribute.getName();
	            String value = currentAttribute.getValue();
	            returnList.add(baseMetaDataLoader.addProperty(
	            		codingSchemeUri,
	            		codingSchemeVersion, 
	            		parentPath, 
	            		name, 
	            		value));
	        }
	    	return returnList;
	    }

	    @SuppressWarnings("unchecked")
	    private List<org.apache.lucene.document.Document> processChildren(
	    		String codingSchemeUri,
    			String codingSchemeVersion,
    			List<String> parentPath,
    			List<Element> elementList) throws Exception {
	    	List<org.apache.lucene.document.Document> returnList = new ArrayList<org.apache.lucene.document.Document>();
	    	
	    	for (Iterator<Element> eleIter = elementList.iterator(); eleIter.hasNext();) {
	            Element element = (Element) eleIter.next();

	            String name = element.getName();
	            String value = element.getTextTrim();

	            if (value != null && value.length() > 0) {
	            	returnList.add(baseMetaDataLoader.addProperty(
	            			codingSchemeUri,
	            			codingSchemeVersion,
	            			parentPath, 
	            			name, 
	            			value));
	            }

	            ArrayList<String> newParentPath = new ArrayList<String>(parentPath);
	            newParentPath.add(name);
	            returnList.addAll(processAttributes(
	            		codingSchemeUri,
	        			codingSchemeVersion,
	        			newParentPath, 
	        			element.getAttributes()));
	            returnList.addAll(processChildren(
	            		codingSchemeUri,
	        			codingSchemeVersion,
	        			newParentPath, 
	        			element.getChildren()));
	        }
	    	return returnList;
	    }

		public void setBaseMetaDataLoader(BaseMetaDataLoader baseMetaDataLoader) {
			this.baseMetaDataLoader = baseMetaDataLoader;
		}

		public BaseMetaDataLoader getBaseMetaDataLoader() {
			return baseMetaDataLoader;
		}

		public void setIndexDaoManager(IndexDaoManager indexDaoManager) {
			this.indexDaoManager = indexDaoManager;
		}

		public IndexDaoManager getIndexDaoManager() {
			return indexDaoManager;
		}
}