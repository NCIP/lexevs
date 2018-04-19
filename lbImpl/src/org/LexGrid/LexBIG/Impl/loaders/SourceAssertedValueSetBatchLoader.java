package org.LexGrid.LexBIG.Impl.loaders;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.concepts.Entity;
import org.LexGrid.util.assertedvaluesets.AssertedValueSetParameters;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets.EntityToVSDTransformer;

public class SourceAssertedValueSetBatchLoader {
    
    private CodedNodeGraphService codedNodeGraphDao;
    private EntityService entityService;
    private SystemResourceService resourceService;
    private String codingSchemeUri;
    private String codingSchemeVersion;
    private String codingSchemeName;
    private String associationName;
    private boolean targetToSource;
    private EntityToVSDTransformer transformer;
    private ValueSetDefinitionService valueSetDefinitionService;
    private LgMessageDirectorIF messages_;
    

    
    public SourceAssertedValueSetBatchLoader(AssertedValueSetParameters params, String owner, String conceptDomainIndicator) throws LBParameterException{
        codedNodeGraphDao = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getCodedNodeGraphService();
        entityService = LexEvsServiceLocator.getInstance().
                getDatabaseServiceManager().getEntityService();
        resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
        this.codingSchemeUri = resourceService.getUriForUserCodingSchemeName(params.getCodingSchemeName(), params.getCodingSchemeVersion());
        valueSetDefinitionService = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getValueSetDefinitionService();
        this.codingSchemeVersion = params.getCodingSchemeVersion();
        this.codingSchemeName = params.getCodingSchemeName();
        this.associationName = params.getAssertedValueSetRelation();
        this.targetToSource = true;
        this.transformer = new EntityToVSDTransformer(params.getBaseValueSetURI(), codingSchemeUri, params.getCodingSchemeVersion(), codingSchemeName, owner, associationName, conceptDomainIndicator);
        messages_ = LoggerFactory.getLogger();
    }
    
    public void run(String sourceName){
        try {
            processEntitiesToDefinition(getEntitiesForAssociation(associationName, codingSchemeUri, codingSchemeVersion), sourceName);
        } catch (LBException e) {
            messages_.fatal("Processing of entity to coding scheme Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    protected List<Node> getEntitiesForAssociation(String association, String codingSchemeUri,String version){
       String associationPredicateUid = this.getPredicateGuidForValueSetRelation(association, codingSchemeUri, version);
       return codedNodeGraphDao.getDistinctTargetTriples(codingSchemeUri, version, associationPredicateUid);
    }
    
    public List<Node> getEnitiesForAssociationAndTargetEntity(String code, String namespace, String association, String codingSchemeUri, String version){
        String associationPredicateUid = this.getPredicateGuidForValueSetRelation(association, codingSchemeUri, version);
        return codedNodeGraphDao.getSourcesFromTarget(codingSchemeUri, version, code, namespace, associationPredicateUid);
    }
    
    protected String getPredicateGuidForValueSetRelation(String associationName, String codingSchemeUri,
            String codingSchemeVersion){
        List<String> list = new ArrayList<String>();
        list.add(associationName);
        List<String> uids = codedNodeGraphDao.getAssociationPredicateUidsForNames(
                codingSchemeUri, codingSchemeVersion, null, list);
        if( uids == null || uids.size() < 1){
            messages_.fatal("No association is asserted for :" 
        + associationName + " in " + codingSchemeUri + ":" + codingSchemeVersion);
            throw new RuntimeException("No association is asserted for :" 
        + associationName + " in " + codingSchemeUri + ":" + codingSchemeVersion);
        }
        return uids.get(0);
    }
    
    
    protected Entity getEntityByCodeAndNamespace(String codingSchemeUri, String version, String code, String namespace){
        Entity entity = entityService.getEntity(codingSchemeUri, version, code, namespace);
        return entity;
    }
    
    public void processEntitiesToDefinition(List<Node> nodes, String sourceName) throws LBException{
       for(Node n: nodes){
           Entity e = getEntityByCodeAndNamespace(codingSchemeUri, 
                   codingSchemeVersion, n.getEntityCode(), n.getEntityCodeNamespace());
           List<ValueSetDefinition> defs = transformer.transformEntityToValueSetDefinitions(e, sourceName);
           for(ValueSetDefinition d : defs){
               valueSetDefinitionService.insertValueSetDefinition(d, null, d.getMappings());
           }
       }
    }

    public static void main(String[] args) {
      try {
        AssertedValueSetParameters.Builder builder = new AssertedValueSetParameters.Builder("17.02d");
        AssertedValueSetParameters params = builder.
                codingSchemeName("NCI_Thesaurus").
                assertedDefaultHierarchyVSRelation("Concept_In_Subset").
                baseValueSetURI("http://evs.nci.nih.gov/valueset/").
                sourceName("Contributing_Source").
                build();
        new SourceAssertedValueSetBatchLoader( params ,"NCI", "Semantic_Type").run(params.getSourceName());
    } catch (LBParameterException e) {
        e.printStackTrace();
    }

    }

}
