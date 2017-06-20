package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;


public class EntityToVSDTransformer{
    private static final Object PRODUCTION = "PRODUCTION";
    private static final String DEFAULT_SOURCE = "source";
    private Registry registry;
    private String baseURI = null;
    private String codingSchemeURI;
    private String codingSchemeName;
    private String association;
    private LgMessageDirectorIF messages_;
    private String owner;
    private String conceptDomainPropertyName = "Semantic_Type";
    
   public EntityToVSDTransformer(String baseURI, 
           String codingSchemeUri, 
           String codingSchemeVersion, 
           String codingSchemeName,
           String owner, 
           String definingAssociation,
           String conceptDomainIndicator){
       registry = LexEvsServiceLocator.getInstance().getRegistry();
       this.baseURI = baseURI;
       this.codingSchemeURI = codingSchemeUri;
       this.owner = owner;
       this.association = definingAssociation;
       messages_ = LoggerFactory.getLogger();
       conceptDomainPropertyName = conceptDomainIndicator;
       
   }
    
    //Entity with more than one source will be processed into more than one definition
    //Assumption is that this source representation is always a flat list of values
    public List<ValueSetDefinition> transformEntityToValueSetDefinitions(Entity entity, String sourceName) throws LBParameterException{
      
       final String source = AssertedValueSetServices.getDefaultSourceIfNull(sourceName);
     
       List<ValueSetDefinition> defs = new ArrayList<ValueSetDefinition>();
       if(!AssertedValueSetServices.isPublishableValueSet(entity, false)){
           return defs;
       }
       
       List<Property> props = entity.getPropertyAsReference();
       HashMap<String, String> definedSources = new HashMap<String, String>();
       List<Property> sourcelist = getPropertiesForPropertyName(props, source);
       
       sourcelist.stream().forEach(s -> definedSources.put(s.getValue().getContent(), 
               getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()) != null?
                       getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()): 
                           AssertedValueSetServices.getValueSetDefinition(entity)));
       
      definedSources.forEach((x,y) -> defs.add(tranformEntityToValueSet(entity,x,y))); 
      if(definedSources.size() == 0){
      defs.add(tranformEntityToValueSet(entity, null, AssertedValueSetServices.getValueSetDefinition(entity)));
      }
       return defs;
    }

    
    private ValueSetDefinition tranformEntityToValueSet(Entity entity, String source, String definition) {
        ValueSetDefinition def = initValueSetDefintion(entity.getEntityCodeNamespace(), true, "1", owner);
        def.setValueSetDefinitionName(entity.getEntityDescription().getContent());
        DefinitionEntry entry = initDefinitionEntry(0, DefinitionOperator.OR);
        Mappings mappings = new Mappings();
        EntityReference ref = initEntityReference(entity, this.association);
        entry.setEntityReference(ref);
        def.addDefinitionEntry(entry);
        mappings.addSupportedNamespace(AssertedValueSetServices.createSupportedNamespace(entity.getEntityCodeNamespace(), codingSchemeName, codingSchemeURI));
        mappings.addSupportedCodingScheme(
                AssertedValueSetServices.createSupportedCodingScheme(entity.getEntityCodeNamespace(), codingSchemeURI));
        def.setMappings(mappings);

        String conceptDomain = AssertedValueSetServices.getConceptDomainValueFromEntityProperty(entity, conceptDomainPropertyName);
        def.setConceptDomain(conceptDomain);
        def.getMappings().addSupportedConceptDomain(AssertedValueSetServices.createSupportedConceptDomain(conceptDomain, codingSchemeURI));

        def.setValueSetDefinitionURI(
                AssertedValueSetServices.createUri(baseURI, source, def.getDefinitionEntry(0).getEntityReference().getEntityCode()));
        def.setEntityDescription(Constructors.createEntityDescription(definition));
        if(source != null){
        def.getMappings().addSupportedSource(AssertedValueSetServices.createSupportedSource(source, codingSchemeURI));
        }
        return def;
    }
    
    protected List<Property> getPropertiesForPropertyName(List<Property> props, String  name){
        return props.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList());
    }
    
    protected String getPropertyQualifierValueForSource(List<PropertyQualifier> quals){
        if(quals.stream().anyMatch(pq -> pq.getPropertyQualifierName().equals(DEFAULT_SOURCE))){
            return quals.stream().filter(pq -> pq.getPropertyQualifierName().equals(DEFAULT_SOURCE)).findFirst().get().getValue().getContent();
        }
        return null;
    }
    
    protected String getProductionVersionForCodingSchemeURI(String uri) throws LBParameterException{
        List<RegistryEntry> entries =  registry.getEntriesForUri(uri);
        return entries.stream().filter(x -> x.getTag().equals(PRODUCTION)).
                findFirst().get().getResourceVersion();
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
        entry.setIsActive(true);
        entry.setStatus("1");
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
