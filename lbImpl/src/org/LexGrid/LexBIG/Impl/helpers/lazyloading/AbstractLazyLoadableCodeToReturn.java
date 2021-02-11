
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class LazyLoadableCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLazyLoadableCodeToReturn extends CodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2847101693632620073L;

    /** The document id. */
    private int documentId;
    
    /** The is hydrated. */
    private boolean isHydrated = false;
    
    private transient SystemResourceService systemResourceService;
    private transient EntityIndexService entityIndexService;
    
    /**
     * Instantiates a new lazy loadable code to return.
     */
    public AbstractLazyLoadableCodeToReturn(){
        super();
    }
    
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param scoreDoc the score doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     */
    public AbstractLazyLoadableCodeToReturn(ScoreDoc scoreDoc){
        this(  
                scoreDoc.score, 
                scoreDoc.doc);
    }
       
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public AbstractLazyLoadableCodeToReturn(
            float score, 
            int documentId){
        super();
        this.setScore(score);
        this.documentId = documentId;  
        
        this.systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        this.entityIndexService = LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
    }
    
    public void compact() {
        this.doCompact();
        this.isHydrated = false;
    }
    
    protected abstract void doCompact();
    
    /**
     * Hydrate.
     * 
     * @throws Exception the exception
     */
    public void hydrate() throws Exception{
         
        Document doc = buildDocument();
 
        String codeField = SQLTableConstants.TBLCOL_ENTITYCODE;
        
        this.setCode(doc.get(codeField));
        
        this.setEntityUid(doc.get(LuceneLoaderCode.ENTITY_UID_FIELD));
        
        this.setEntityDescription(
                doc.get(SQLTableConstants.TBLCOL_ENTITYDESCRIPTION));
        
        if(super.getUri() == null) {
            this.setUri(
                doc.get(LuceneLoaderCode.CODING_SCHEME_ID_FIELD));
        }
        
        if(super.getVersion() == null) {
            this.setVersion(
                doc.get(LuceneLoaderCode.CODING_SCHEME_VERSION_FIELD));
        }
        
        this.setNamespace(
                doc.get(SQLTableConstants.TBLCOL_ENTITYCODENAMESPACE));
        this.setEntityTypes(
                doc.getValues("type"));
        isHydrated = true;       
    }
    
    protected abstract Document buildDocument() throws Exception;
    
    protected EntityIndexService getEntityIndexService() {
        if(this.entityIndexService == null) {
            this.entityIndexService = LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
        }
        return this.entityIndexService;
    }
 
    protected SystemResourceService getSystemResourceService() {
        if(this.systemResourceService == null) {
            this.systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        }
        return this.systemResourceService;
    }

    /**
     * Checks if is hydrated.
     * 
     * @return true, if is hydrated
     */
    public boolean isHydrated() {
        return isHydrated;
    } 
    
    /**
     * Gets the document id.
     * 
     * @return the document id
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document id.
     * 
     * @param documentId the new document id
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
}