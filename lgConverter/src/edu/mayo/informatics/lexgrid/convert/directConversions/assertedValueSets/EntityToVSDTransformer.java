package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityToVSDTransformer {
    private static final Object PRODUCTION = "PRODUCTION";
    CodedNodeGraphService codedNodeGraphDao;
    EntityService entityService;
    CodingSchemeService csService;
    SystemResourceService resourceService;
    Registry registry;
    private String SOURCE_NAME = "Contributing_Source";
    String baseURI = null;
    String codingSchemeURI;
       
    private LgMessageDirectorIF messages_;
    
   public EntityToVSDTransformer(String baseURI){
       codedNodeGraphDao = LexEvsServiceLocator.getInstance().
               getDatabaseServiceManager().getCodedNodeGraphService();
       entityService = LexEvsServiceLocator.getInstance().
               getDatabaseServiceManager().getEntityService();
       csService = LexEvsServiceLocator.getInstance().
               getDatabaseServiceManager().getCodingSchemeService();
       resourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
       registry = LexEvsServiceLocator.getInstance().getRegistry();
       this.baseURI = baseURI;
       
   }
    
    protected String getPredicateGuidForValueSetRelation(String associationName, String codingSchemeUri,
            String codingSchemeVersion){
        List<String> list = new ArrayList<String>();
        list.add(associationName);
        List<String> uids = codedNodeGraphDao.getAssociationPredicateUidsForNames(
                codingSchemeUri, codingSchemeVersion, null, list);
        if( uids == null || uids.size() < 1){
            throw new RuntimeException("No association is asserted for :" 
        + associationName + " in " + codingSchemeUri + ":" + codingSchemeVersion);
        }
        return uids.get(0);
    }
    
 
    
    protected Entity getEntityByCodeAndNamespace(String codingSchemeUri, String version, String code, String namespace){
        Entity entity = entityService.getEntity(codingSchemeUri, version, code, namespace);
        return entity;
    }
    
    //Entity with more than one source will be processed into more than one definition
    public List<ValueSetDefinition> transformEntityToValueSetDefinitions(Entity entity, String sourceName) throws LBParameterException{
       String uri = resourceService.getUriForUserCodingSchemeName(entity.getEntityCodeNamespace(), null);
       final String source = getDefaultSourceIfNull(sourceName);
       List<Property> props = entity.getPropertyAsReference();
       List<Property> list = props.stream().filter(x -> x.getPropertyName().equals(source)).collect(Collectors.toList());
       List<ValueSetDefinition> defs = new ArrayList<ValueSetDefinition>();
       List<String> definedSources = new ArrayList<String>();
       ValueSetDefinition def = new ValueSetDefinition();
       DefinitionEntry entry = new DefinitionEntry();
       Mappings mappings = new Mappings();
       EntityReference ref = new EntityReference();
       ref.setEntityCode(entity.getEntityCode());
       ref.setEntityCodeNamespace(entity.getEntityCodeNamespace());
       mappings.setSupportedNamespace(0, createSupportedNamespace(entity.getEntityCodeNamespace(),uri));
       mappings.setSupportedCodingScheme(0, createSupportedCodingScheme(entity.getEntityCodeNamespace(), uri));
      for(Property p: list){
          if(p.getPropertyName().equals(sourceName)){
              definedSources.add(p.getValue().getContent());
              mappings.addSupportedSource(createSupportedSource(entity.getEntityCodeNamespace(), uri));
          }     
          if(p.getPropertyName().equals("Semantic_Type")){
            def.setConceptDomain(p.getValue().getContent());
            mappings.addSupportedConceptDomain(createSupportedConceptDomain(p.getValue().getContent(), uri));
          }
          

          
      }
       return defs;
    }

    private SupportedCodingScheme createSupportedCodingScheme(String codingScheme, String uri) {
        SupportedCodingScheme scheme = new SupportedCodingScheme();
        scheme.setContent(codingScheme);
        scheme.setLocalId(codingScheme);
        scheme.setIsImported(true);
        scheme.setUri(uri);
        return scheme;
    }

    private SupportedNamespace createSupportedNamespace(String entityCodeNamespace, String uri) {
        SupportedNamespace nmsp = new SupportedNamespace();
        nmsp.setContent(entityCodeNamespace);
        nmsp.setLocalId(entityCodeNamespace);
        nmsp.setUri(uri);
        nmsp.setEquivalentCodingScheme(entityCodeNamespace);
        return nmsp;
    }

    private SupportedConceptDomain createSupportedConceptDomain(String content, String uri) {
       SupportedConceptDomain domain = new SupportedConceptDomain();
       domain.setContent(content);
       domain.setLocalId(content);
       domain.setUri(uri);
        return domain;
    }
 
    private String getDefaultSourceIfNull(String sourceName) {
        return sourceName == null?SOURCE_NAME: sourceName;
    }
    
    private SupportedSource createSupportedSource(String source, String uri){
        SupportedSource newSource = new SupportedSource();
        newSource.setLocalId(source);
        newSource.setContent(source);
        newSource.setUri(uri);
        return newSource;
    }
    
    protected String getProductionVersionForCodingSchemeURI(String uri) throws LBParameterException{
        List<RegistryEntry> entries =  registry.getEntriesForUri(uri);
        return entries.stream().filter(x -> x.getTag().equals(PRODUCTION)).findFirst().get().getResourceVersion();
    }

}
