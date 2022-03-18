
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.apache.lucene.search.ScoreDoc;

/**
 * The Class NonProxyLazyCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNonProxyLazyCodeToReturn extends AbstractLazyLoadableCodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8339301513416366127L;
    
    public AbstractNonProxyLazyCodeToReturn() {
        super();
    }
    
    public AbstractNonProxyLazyCodeToReturn(ScoreDoc scoreDoc){
        super(  
                scoreDoc.score, 
                scoreDoc.doc);
    }
    
    public AbstractNonProxyLazyCodeToReturn(
            float score, 
            int documentId){
        super(score,documentId);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getCode()
     */
    @Override
    public String getCode() {
        hydrateIfNeeded();
        return super.getCode();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getEntityDescription()
     */
    @Override
    public String getEntityDescription() {
        hydrateIfNeeded();
        return super.getEntityDescription();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getEntityTypes()
     */
    @Override
    public String[] getEntityTypes() {
        hydrateIfNeeded();
        return super.getEntityTypes();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getNamespace()
     */
    @Override
    public String getNamespace() {
        hydrateIfNeeded();
        return super.getNamespace();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getUri()
     */
    @Override
    public String getUri() {
        hydrateIfNeeded();
        return super.getUri();
    }

    @Override
    public String getVersion() {
        hydrateIfNeeded();
        return super.getVersion();
    }

    @Override
    public String getEntityUid() {
        hydrateIfNeeded();
        return super.getEntityUid();
    }

    @Override
    protected void doCompact() {
        this.setCode(null);
        this.setEntityDescription(null);
        this.setEntityTypes(null);
        this.setNamespace(null);
        this.setUri(null);
        this.setEntityUid(null);
        this.setVersion(null);
    }

    /**
     * Hydrate if needed.
     */
    protected void hydrateIfNeeded(){
        if(!super.isHydrated()){
            try {
                super.hydrate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}