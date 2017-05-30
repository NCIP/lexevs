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
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;

import junit.framework.TestCase;

public class EntityToVSDTransformer{
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
      
        final String source = getDefaultSourceIfNull(sourceName);
        List<Property> props = entity.getPropertyAsReference();

       List<ValueSetDefinition> defs = new ArrayList<ValueSetDefinition>();
       HashMap<String, String> definedSources = new HashMap<String, String>();
       List<Property> sourcelist = getPropertiesForPropertyName(props, source);
       
       sourcelist.stream().forEach(s -> definedSources.put(s.getValue().getContent(), 
               getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()) != null?
                       getPropertyQualifierValueForSource(s.getPropertyQualifierAsReference()): 
                           entity.getEntityDescription().getContent()));
       
      definedSources.forEach((x,y) -> defs.add(tranformEntityToValueSet(entity,x,y))); 
      if(definedSources.size() > 1 || definedSources.size() == 0 || !definedSources.containsValue(owner)){
      defs.add(tranformEntityToValueSet(entity, owner, entity.getEntityDescription().getContent()));
      }
       return defs;
    }

    
    private ValueSetDefinition tranformEntityToValueSet(Entity entity, String sourceName, String definition) {
        List<Property> props = entity.getPropertyAsReference();
        final String source = getDefaultSourceIfNull(sourceName);
        ValueSetDefinition def = initValueSetDefintion(entity.getEntityCodeNamespace(), true, "1", owner);
        def.setValueSetDefinitionName(entity.getEntityDescription().getContent());
        DefinitionEntry entry = initDefinitionEntry(0, DefinitionOperator.OR);
        Mappings mappings = new Mappings();
        EntityReference ref = initEntityReference(entity, this.association);
        entry.setEntityReference(ref);
        def.addDefinitionEntry(entry);
        mappings.addSupportedNamespace(createSupportedNamespace(entity.getEntityCodeNamespace(), codingSchemeURI));
        mappings.addSupportedCodingScheme(
                createSupportedCodingScheme(entity.getEntityCodeNamespace(), codingSchemeURI));
        def.setMappings(mappings);

        String conceptDomain = null;
                        if(props.stream().filter(x -> x.getPropertyName().equals("Semantic_Type")).findFirst().isPresent()){
                            conceptDomain = props.stream().filter(x -> x.getPropertyName().equals("Semantic_Type")).
                                    findFirst().get().getValue().getContent();
                        }
        def.setConceptDomain(conceptDomain);
        def.getMappings().addSupportedConceptDomain(createSupportedConceptDomain(conceptDomain, codingSchemeURI));

        def.setValueSetDefinitionURI(
                createUri(baseURI, source, def.getDefinitionEntry(0).getEntityReference().getEntityCode()));
        def.setEntityDescription(Constructors.createEntityDescription(definition));
        def.getMappings().addSupportedSource(createSupportedSource(source, codingSchemeURI));
        return def;
    }
    
    protected List<Property> getPropertiesForPropertyName(List<Property> props, String  name){
        return props.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList());
    }
    
    protected String getPropertyQualifierValueForSource(List<PropertyQualifier> quals){
        if(quals.stream().filter(pq -> pq.getPropertyQualifierName().equals("source")).findFirst().isPresent()){
            return quals.stream().filter(pq -> pq.getPropertyQualifierName().equals("source")).findFirst().get().getValue().getContent();
        }
        return null;
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
