
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.relations.Relations;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractRefereshingPageableIterator;

/**
 * The Class AbstractMappingTripleIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMappingTripleIterator<T extends Iterator<String>> extends AbstractRefereshingPageableIterator<T,ResolvedConceptReference> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    /** The triple uid iterator. */
    private T tripleUidIterator;
    
    /** The refs. */
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    /** The uri. */
    private String uri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    private static int DEFAULT_PAGE_SIZE = 50;
    
    /**
     * The Class MappingAbsoluteCodingSchemeVersionReferences.
     */
    protected static class MappingAbsoluteCodingSchemeVersionReferences implements Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -5960065222089972290L;
        
        /** The source coding scheme name. */
        private String sourceCodingSchemeName;
        
        /** The source coding scheme. */
        private AbsoluteCodingSchemeVersionReference sourceCodingScheme;
        
        /** The target coding scheme name. */
        private String targetCodingSchemeName;
        
        /** The target coding scheme. */
        private AbsoluteCodingSchemeVersionReference targetCodingScheme;

        /**
         * Gets the source coding scheme name.
         * 
         * @return the source coding scheme name
         */
        protected String getSourceCodingSchemeName() {
            return sourceCodingSchemeName;
        }
        
        /**
         * Sets the source coding scheme name.
         * 
         * @param sourceCodingSchemeName the new source coding scheme name
         */
        protected void setSourceCodingSchemeName(String sourceCodingSchemeName) {
            this.sourceCodingSchemeName = sourceCodingSchemeName;
        }
        
        /**
         * Gets the target coding scheme name.
         * 
         * @return the target coding scheme name
         */
        protected String getTargetCodingSchemeName() {
            return targetCodingSchemeName;
        }
        
        /**
         * Sets the target coding scheme name.
         * 
         * @param targetCodingSchemeName the new target coding scheme name
         */
        protected void setTargetCodingSchemeName(String targetCodingSchemeName) {
            this.targetCodingSchemeName = targetCodingSchemeName;
        }
        
        /**
         * Gets the source coding scheme.
         * 
         * @return the source coding scheme
         */
        protected AbsoluteCodingSchemeVersionReference getSourceCodingScheme() {
            return sourceCodingScheme;
        }
        
        /**
         * Sets the source coding scheme.
         * 
         * @param sourceCodingScheme the new source coding scheme
         */
        protected void setSourceCodingScheme(AbsoluteCodingSchemeVersionReference sourceCodingScheme) {
            this.sourceCodingScheme = sourceCodingScheme;
        }
        
        /**
         * Gets the target coding scheme.
         * 
         * @return the target coding scheme
         */
        protected AbsoluteCodingSchemeVersionReference getTargetCodingScheme() {
            return targetCodingScheme;
        }
        
        /**
         * Sets the target coding scheme.
         * 
         * @param targetCodingScheme the new target coding scheme
         */
        protected void setTargetCodingScheme(AbsoluteCodingSchemeVersionReference targetCodingScheme) {
            this.targetCodingScheme = targetCodingScheme;
        }  
    }
 
    /**
     * Instantiates a new mapping triple iterator.
     */
    public AbstractMappingTripleIterator() {
        super();
    }

    public AbstractMappingTripleIterator(
            String uri, 
            String version,
            String relationsContainerName) throws LBParameterException {
        super(DEFAULT_PAGE_SIZE);
        this.uri = uri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
        this.refs = this.getMappingAbsoluteCodingSchemeVersionReference(uri, version, relationsContainerName);
    }
    
    protected void initializetMappingTripleIterator(){
        try {
            this.tripleUidIterator = createTripleIterator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract T createTripleIterator() throws Exception;

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doGetRefresh()
     */
    @Override
    protected T doGetRefresh() {
        return this.tripleUidIterator;
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doRefresh(java.lang.Object)
     */
    @Override
    protected void doRefresh(T refresh) {
        this.tripleUidIterator = refresh;
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
    @Override
    protected List<? extends ResolvedConceptReference> doPage(int currentPosition, int pageSize) {
        List<String> tripleUidList = new ArrayList<String>();

        for(int i=0;i<pageSize && tripleUidIterator.hasNext();i++) {
            tripleUidList.add(this.tripleUidIterator.next());
        }
        
        return this.buildList(tripleUidList);
    }
    
    /**
     * Builds the list.
     * 
     * @param tripleUids the triple uids
     * 
     * @return the list<? extends resolved concept reference>
     */
    private List<? extends ResolvedConceptReference> buildList(List<String> tripleUids){
        if(CollectionUtils.isEmpty(tripleUids)){
            return null;
        }
        List<? extends ResolvedConceptReference> list = 
            LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().
                    getCodedNodeGraphService().
                        getMappingTriples(
                                uri, 
                                version, 
                                refs.getSourceCodingScheme(), 
                                refs.getTargetCodingScheme(), 
                                relationsContainerName, 
                                tripleUids);
        
        return addCodingSchemeInfo(list);
    }
    
    /**
     * Adds the coding scheme info.
     * 
     * @param list the list
     * 
     * @return the list<? extends resolved concept reference>
     */
    private List<? extends ResolvedConceptReference> addCodingSchemeInfo(List<? extends ResolvedConceptReference> list) {

        for(ResolvedConceptReference ref : list) {
            if(this.refs.getSourceCodingScheme() != null) {
                ref.setCodingSchemeURI(this.refs.getSourceCodingScheme().getCodingSchemeURN());
                ref.setCodingSchemeVersion(this.refs.getSourceCodingScheme().getCodingSchemeVersion());
            }
            ref.setCodingSchemeName(this.refs.getSourceCodingSchemeName());
            
            for(Association assoc : ref.getSourceOf().getAssociation()) {
                for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                    if(this.refs.getTargetCodingScheme() != null) {
                        ac.setCodingSchemeURI(this.refs.getTargetCodingScheme().getCodingSchemeURN());
                        ac.setCodingSchemeVersion(this.refs.getTargetCodingScheme().getCodingSchemeVersion());
                    }
                    ac.setCodingSchemeName(this.refs.getTargetCodingSchemeName());
                }
            }
        }
      
        return list;
    }

    /**
     * Gets the mapping absolute coding scheme version reference.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * 
     * @return the mapping absolute coding scheme version reference
     * 
     * @throws LBParameterException the LB parameter exception
     */
    private MappingAbsoluteCodingSchemeVersionReferences getMappingAbsoluteCodingSchemeVersionReference(
            final String uri, 
            final String version,
            final String relationsContainerName) throws LBParameterException {
        MappingAbsoluteCodingSchemeVersionReferences refs = new MappingAbsoluteCodingSchemeVersionReferences();
        
        Relations relations = 
            LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().
                    getDaoCallbackService().
                        executeInDaoLayer(new DaoCallback<Relations>() {

            @Override
            public Relations execute(DaoManager daoManager) {
                String codingSchemeUid = daoManager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
                
                String relationsUid = daoManager.getAssociationDao(uri, version).getRelationUId(codingSchemeUid, relationsContainerName);
                
                return daoManager.getAssociationDao(uri, version).getRelationsByUId(
                        codingSchemeUid, 
                        relationsUid, 
                        false);
            }
        });
        
        if(! relations.getIsMapping()) {
            throw new LBParameterException("RelationsContainer: " + relations.getContainerName() + " is not a Mapping Relations Container.");
        }
        
        AbsoluteCodingSchemeVersionReference source = ServiceUtility.resolveCodingSchemeFromLocalName(uri, version, relations.getSourceCodingScheme(), relations.getSourceCodingSchemeVersion());
        AbsoluteCodingSchemeVersionReference target = ServiceUtility.resolveCodingSchemeFromLocalName(uri, version, relations.getTargetCodingScheme(), relations.getTargetCodingSchemeVersion());
        
        refs.setSourceCodingScheme(source);
        refs.setSourceCodingSchemeName(relations.getSourceCodingScheme());
        
        refs.setTargetCodingScheme(target);
        refs.setTargetCodingSchemeName(relations.getTargetCodingScheme());
        
        return refs;
    }

    protected String getUri() {
        return uri;
    }

    protected String getVersion() {
        return version;
    }

    protected String getRelationsContainerName() {
        return relationsContainerName;
    }

    protected MappingAbsoluteCodingSchemeVersionReferences getRefs() {
        return refs;
    }

    protected T getTripleUidIterator() {
        return tripleUidIterator;
    }
}