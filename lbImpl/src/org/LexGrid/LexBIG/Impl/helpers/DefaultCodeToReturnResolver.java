
package org.LexGrid.LexBIG.Impl.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.annotations.LgProxyClass;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.concepts.Entity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.access.entity.EntityDao;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.springframework.util.Assert;

/**
 * The Class DefaultCodeToReturnResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@LgProxyClass
public class DefaultCodeToReturnResolver implements CodeToReturnResolver {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2128719170709905064L;

    protected ResolvedConceptReference doBuildResolvedConceptReference(CodeToReturn codeToReturn, 
            LocalNameList restrictToProperties,
            PropertyType[] restrictToPropertyTypes,
            Filter[] filters,
            boolean resolve) throws LBInvocationException {
        SystemResourceService resourceManager = LexEvsServiceLocator.getInstance().getSystemResourceService();
        
        // Always assign the basics...
        ResolvedConceptReference rcr = new ResolvedConceptReference();
        try {
            
            rcr.setCodingSchemeURI(codeToReturn.getUri());
            rcr.setCodingSchemeVersion(codeToReturn.getVersion());
            rcr.setCode(codeToReturn.getCode());
            rcr.setCodeNamespace(codeToReturn.getNamespace());
            EntityDescription ed = new EntityDescription();
            ed.setContent(codeToReturn.getEntityDescription());
            rcr.setEntityDescription(ed);
            rcr.setEntityType(codeToReturn.getEntityTypes());
            rcr.setCodingSchemeName(
                    resourceManager.getInternalCodingSchemeNameForUserCodingSchemeName(codeToReturn.getUri(), codeToReturn.getVersion()));

        } catch (LBParameterException e) {
            // this should only happen when the codedNodeSet was constructed
            // from a graph -
            // and if a source or target concept in the graph is not available
            // in the system.
            rcr.setEntity(null);
        }

        // these (two) stay null by design
        rcr.setSourceOf(null);
        rcr.setTargetOf(null);

        // these (two) stay null by design
        rcr.setSourceOf(null);
        rcr.setTargetOf(null);

        if (filters != null && filters.length > 0) {
            for (int i = 0; i < filters.length; i++) {
                if (!filters[i].match(rcr)) {
                    return null;
                }
            }
        }

        return rcr;
    }
     
    public ResolvedConceptReference buildResolvedConceptReference(CodeToReturn codeToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, Filter[] filters,
            boolean resolve) throws LBInvocationException {
        ResolvedConceptReferenceList returnList = this.buildResolvedConceptReference(
                DaoUtility.createNonTypedList(codeToReturn), restrictToProperties, restrictToPropertyTypes, filters, resolve);
   
        Assert.state(returnList.getResolvedConceptReferenceCount() <= 1);
        
        if(returnList.getResolvedConceptReferenceCount() == 1) {
            return returnList.getResolvedConceptReference(0);
        } else {
            return null;
        }
    }

    public ResolvedConceptReferenceList buildResolvedConceptReference(List<CodeToReturn> codesToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, Filter[] filters,
            boolean resolve) throws LBInvocationException {

        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        for(CodeToReturn codeToReturn : codesToReturn){
            returnList.addResolvedConceptReference(
                    doBuildResolvedConceptReference(
                            codeToReturn, 
                            restrictToProperties, 
                            restrictToPropertyTypes, 
                            filters, 
                            resolve));
        }
        
        if(resolve) {
            return this.addEntities(returnList, codesToReturn, restrictToProperties, restrictToPropertyTypes);
        } else {
            return returnList;
        }
    }
    
    private ResolvedConceptReferenceList addEntities(
            ResolvedConceptReferenceList unresolvedList, 
            List<CodeToReturn> codesToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes) throws LBInvocationException {
        Map<String,Entity> entityMap = this.buildCodedEntry(
                codesToReturn, 
                restrictToProperties, 
                restrictToPropertyTypes);
        
        for(ResolvedConceptReference ref : unresolvedList.getResolvedConceptReference()) {
            if(ref == null) {continue;}
            
            String refKey = this.getKey(ref);
            ref.setEntity(entityMap.get(refKey));
        }
        
        return unresolvedList;
    }

    
    private Map<String,Entity> buildCodedEntry(List<CodeToReturn> codesToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes) throws LBInvocationException {
        Map<String,Entity> entityMap = new HashMap<String,Entity>();
        
        Map<UriVersionPair,List<String>> entityUids = new HashMap<UriVersionPair,List<String>>();
        
        for(CodeToReturn codeToReturn : codesToReturn){
            String entityUid = codeToReturn.getEntityUid();
            if(StringUtils.isNotBlank(entityUid)) {
                UriVersionPair pair = new UriVersionPair(codeToReturn.getUri(), codeToReturn.getVersion());
                if(!entityUids.containsKey(pair)) {
                    entityUids.put(pair, new ArrayList<String>());
                }
                entityUids.get(pair).add(entityUid);
                
            } else {
                entityMap.put(
                        getKey(codeToReturn), 
                        this.buildCodedEntry(
                                codeToReturn.getUri(), 
                                codeToReturn.getVersion(), 
                                codeToReturn.getCode(), 
                                codeToReturn.getNamespace(), 
                                restrictToProperties, 
                                restrictToPropertyTypes));
            }
        }
        
        if(!entityUids.isEmpty()) {
            List<Entity> totalEntities = new ArrayList<Entity>();
            
            for(UriVersionPair pair : entityUids.keySet()) {
                totalEntities.addAll(this.buildCodedEntry(
                        pair.getUri(), 
                        pair.getVersion(), 
                        entityUids.get(pair), 
                        restrictToProperties, 
                        restrictToPropertyTypes));
            }
            
            for(Entity entity : totalEntities) {
                entityMap.put(this.getKey(entity), entity);
            }
        }
        
      return entityMap;
    }
    
    private String getKey(ConceptReference ref) {
        return getKey(ref.getCode(), ref.getCodeNamespace());
    } 
    
    private String getKey(CodeToReturn codeToReturn) {
        return getKey(codeToReturn.getCode(), codeToReturn.getNamespace());
    } 
    
    private String getKey(Entity entity) {
        return getKey(entity.getEntityCode(), entity.getEntityCodeNamespace());
    } 
    
    private String getKey(String code, String namespace) {
        return Integer.toString(
                code.hashCode()) + 
                Integer.toString(namespace.hashCode());
    } 
    
    private List<Entity> buildCodedEntry(
            final String codingSchemeUri, 
            final String codingSchemeVersion, 
            final List<String> entityUids,
            final LocalNameList restrictToProperties, 
            final PropertyType[] restrictToPropertyTypes) throws LBInvocationException {
        
        DaoCallbackService callbackService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService();

        return callbackService.executeInDaoLayer(new DaoCallback<List<Entity>>() {

            @Override
            public List<Entity> execute(DaoManager daoManager) {
                CodingSchemeDao codingSchemeDao = daoManager.
                    getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);
               
                String codingSchemeUid = codingSchemeDao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
                
                EntityDao entityDao = daoManager.
                    getEntityDao(codingSchemeUri, codingSchemeVersion);
                
                return entityDao.getEntities(
                        codingSchemeUid,
                        DaoUtility.localNameListToString(restrictToProperties),
                        DaoUtility.propertyTypeArrayToString(restrictToPropertyTypes), 
                        entityUids);
            }       
        });   
    }

    private Entity buildCodedEntry(String codingSchemeUri, String codingSchemeVersion, String code, String namespace,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes) throws LBInvocationException {
        if(DaoUtility.containsNulls(codingSchemeUri, codingSchemeVersion,code,namespace)){
            return null;
        }
        
        EntityService entityService = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();

        return entityService.getEntity(
                codingSchemeUri, 
                codingSchemeVersion,
                code,
                namespace,
                DaoUtility.localNameListToString(restrictToProperties),
                DaoUtility.propertyTypeArrayToString(restrictToPropertyTypes));
    }
    
    private class UriVersionPair {

        private String uri;
        private String version;

        public UriVersionPair(String uri, String version) {
            super();
            this.uri = uri;
            this.version = version;
        }
        public String getUri() {
            return uri;
        }
        public void setUri(String uri) {
            this.uri = uri;
        }
        public String getVersion() {
            return version;
        }
        public void setVersion(String version) {
            this.version = version;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((uri == null) ? 0 : uri.hashCode());
            result = prime * result + ((version == null) ? 0 : version.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            UriVersionPair other = (UriVersionPair) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (uri == null) {
                if (other.uri != null)
                    return false;
            } else if (!uri.equals(other.uri))
                return false;
            if (version == null) {
                if (other.version != null)
                    return false;
            } else if (!version.equals(other.version))
                return false;
            return true;
        }
        private DefaultCodeToReturnResolver getOuterType() {
            return DefaultCodeToReturnResolver.this;
        }  
    }
}