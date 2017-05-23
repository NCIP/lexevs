package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.system.service.SystemResourceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class EntityToVSDTransformer {
    private static final Object PRODUCTION = "PRODUCTION";
    private Registry registry;
    private String SOURCE_NAME = "Contributing_Source";
    private String baseURI = null;
    private String codingSchemeURI;
    private String association;
     
    private LgMessageDirectorIF messages_;
    private String codingSchemeVersion;
    private String owner;
    
   public EntityToVSDTransformer(String baseURI, String codingSchemeUri, 
           String codingSchemeVersion, String owner, String definingAssociation){
       registry = LexEvsServiceLocator.getInstance().getRegistry();
       this.baseURI = baseURI;
       this.codingSchemeURI = codingSchemeUri;
       this.codingSchemeVersion = codingSchemeVersion;
       this.owner = owner;
       this.association = definingAssociation;
       
   }
    

    
 

    
    //Entity with more than one source will be processed into more than one definition
    //Assumption is that this source representation is always a flat list of values
    public List<ValueSetDefinition> transformEntityToValueSetDefinitions(Entity entity, String sourceName) throws LBParameterException{
       //String uri = resourceService.getUriForUserCodingSchemeName(entity.getEntityCodeNamespace(), null);
       final String source = getDefaultSourceIfNull(sourceName);
       List<Property> props = entity.getPropertyAsReference();
       List<Property> list = props.stream().filter(x -> x.getPropertyName().equals(source)).collect(Collectors.toList());
       List<ValueSetDefinition> defs = new ArrayList<ValueSetDefinition>();
       HashMap<String, String> definedSources = new HashMap<String, String>();
       ValueSetDefinition def = initValueSetDefintion(entity.getEntityCodeNamespace(), true, 
               "1", owner);
       DefinitionEntry entry = initDefinitionEntry(0, DefinitionOperator.OR);
       Mappings mappings = new Mappings();
       EntityReference ref = initEntityReference(entity, this.association );
       entry.setEntityReference(ref);
       def.addDefinitionEntry(entry);
       mappings.addSupportedNamespace(createSupportedNamespace(entity.getEntityCodeNamespace(), 
               codingSchemeURI));
       mappings.addSupportedCodingScheme(createSupportedCodingScheme(entity.getEntityCodeNamespace(), 
               codingSchemeURI));
      for(Property p: list){
          if(p.getPropertyName().equals(sourceName)){
              definedSources.put(p.getValue().getContent(), "PlaceHolder");
              mappings.addSupportedSource(createSupportedSource(entity.getEntityCodeNamespace(), 
                      codingSchemeURI));
          }     
          if(p.getPropertyName().equals("Semantic_Type")){
            def.setConceptDomain(p.getValue().getContent());
            mappings.addSupportedConceptDomain(createSupportedConceptDomain(p.getValue().getContent(), 
                    codingSchemeURI));
          }
          if(p.getPropertyName().equals("ALT_DEFINITION")){
              for(PropertyQualifier pq: p.getPropertyQualifier()){
                  if(pq.getPropertyQualifierName().equals("source"))
                      if(definedSources.containsKey(pq.getValue().getContent())){
                         definedSources.replace(pq.getValue().getContent(), "PlaceHolder", p.getValue().getContent());
                      }
              }
          }
      }
      definedSources.forEach((x,y) -> defs.add(createVSDefinitionBySource(def,x,y)));

       return defs;
    }

    private ValueSetDefinition createVSDefinitionBySource(ValueSetDefinition def, String source, String definition) {
       ValueSetDefinition newDef = def;
       newDef.setValueSetDefinitionURI(createUri(baseURI, source, def.getDefinitionEntry(0).getEntityReference().getEntityCode()));
       newDef.setEntityDescription(Constructors.createEntityDescription(definition));
        return newDef;
    }

    protected SupportedCodingScheme createSupportedCodingScheme(String codingScheme, String uri) {
        SupportedCodingScheme scheme = new SupportedCodingScheme();
        scheme.setContent(codingScheme);
        scheme.setLocalId(codingScheme);
        scheme.setIsImported(true);
        scheme.setUri(uri);
        return scheme;
    }

    protected SupportedNamespace createSupportedNamespace(String entityCodeNamespace, String uri) {
        SupportedNamespace nmsp = new SupportedNamespace();
        nmsp.setContent(entityCodeNamespace);
        nmsp.setLocalId(entityCodeNamespace);
        nmsp.setUri(uri);
        nmsp.setEquivalentCodingScheme(entityCodeNamespace);
        return nmsp;
    }

    protected SupportedConceptDomain createSupportedConceptDomain(String content, String uri) {
       SupportedConceptDomain domain = new SupportedConceptDomain();
       domain.setContent(content);
       domain.setLocalId(content);
       domain.setUri(uri);
        return domain;
    }
 
    protected String getDefaultSourceIfNull(String sourceName) {
        return sourceName == null?SOURCE_NAME: sourceName;
    }
    
    protected SupportedSource createSupportedSource(String source, String uri){
        SupportedSource newSource = new SupportedSource();
        newSource.setLocalId(source);
        newSource.setContent(source);
        newSource.setUri(uri);
        return newSource;
    }
    
    protected String getProductionVersionForCodingSchemeURI(String uri) throws LBParameterException{
        List<RegistryEntry> entries =  registry.getEntriesForUri(uri);
        return entries.stream().filter(x -> x.getTag().equals(PRODUCTION)).
                findFirst().get().getResourceVersion();
    }
    
    protected String createUri(String base, String source, String code){
       return base + (source != null?source + "/":"") + code;
    }
    
    protected ValueSetDefinition initValueSetDefintion(String namespace, boolean isActive, 
            String status, String owner){
        ValueSetDefinition def = new ValueSetDefinition();
        def.setDefaultCodingScheme(namespace);
        def.setIsActive(isActive);
        def.setStatus(status);
        def.setOwner(owner);
        return def;
    }
    
    protected DefinitionEntry initDefinitionEntry( long ruleOrder, DefinitionOperator oper){
        DefinitionEntry entry = new DefinitionEntry();
        entry.setRuleOrder(0L);
        entry.setOperator(DefinitionOperator.OR);
        return entry;
    }
    
    protected EntityReference initEntityReference(Entity entity, String associationName){
        EntityReference ref = new EntityReference();
        ref.setLeafOnly(true);
        ref.setTargetToSource(true);
        ref.setTransitiveClosure(true);
        ref.setEntityCode(entity.getEntityCode());
        ref.setEntityCodeNamespace(entity.getEntityCodeNamespace());
        ref.setReferenceAssociation(associationName);
        return ref;
    }

}
