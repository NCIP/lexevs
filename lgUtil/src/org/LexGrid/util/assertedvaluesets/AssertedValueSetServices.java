package org.LexGrid.util.assertedvaluesets;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;

public class AssertedValueSetServices {

    public static final String RESOLVED_AGAINST_CODING_SCHEME_VERSION= "resolvedAgainstCodingSchemeVersion";
    public static final String VERSION= "version";
    public static final String GENERIC= "generic";
    public static final String CS_NAME= "codingSchemeName";
    public static final String SOURCE_SCHEME_NAME= "textualPresentation";
    
    public static final String DEFAULT_DO_PUBLISH_NAME ="Publish_Value_Set";
    public static final String DEFAULT_DO_PUBLISH_VALUE = "yes";
    public static final String BROWSER_VS_DEFINITION = "Term_Browser_Value_Set_Description";
    public static final String DEFINITION = "DEFINITION";
    public static final String CONCEPT_DOMAIN = "Semantic_Type";
    public static final String SOURCE_NAME = "Contributing_Source";
    public static final String SOURCE = "source";
    public static final String BASE = "http://evs.nci.nih.gov/valueset/";
    public static final String DEFAULT_SOURCE = "NCI";
    public static final String DEFAULT_CODINGSCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
    public static final String DEFAULT_CODINGSCHEME_NAME = "NCI_Thesaurus";
    public final static String ASSERTED_VALUESET_RELATION = "Concept_In_Subset";
    
    public static boolean isPublishableValueSet(Entity entity, boolean force) {
        if(entity.getPropertyAsReference().stream().anyMatch(x -> x.
                   getPropertyName().equals(DEFAULT_DO_PUBLISH_NAME))){
           String publish =  entity.getPropertyAsReference().stream().filter(x -> x.
                    getPropertyName().equals(DEFAULT_DO_PUBLISH_NAME)).findFirst().get().getValue().getContent();
          if(publish.equalsIgnoreCase(DEFAULT_DO_PUBLISH_VALUE)){
              return true;
          }
       }
           return force;
       }
    
    public static String getValueSetDefinition(Entity entity){

        boolean isPresent = entity.getPropertyAsReference().stream().anyMatch(x -> 
        x.getPropertyName().equals(BROWSER_VS_DEFINITION));
        
        boolean isDefinition = entity.getPropertyAsReference().stream().anyMatch(x -> 
        x.getPropertyName().equals(DEFINITION));
        
        if(isPresent){
            return entity.getPropertyAsReference().stream().filter(x -> x.
                    getPropertyName().equals(BROWSER_VS_DEFINITION)).findFirst().get().getValue().getContent();
        }
        else if(isDefinition){
            return entity.getPropertyAsReference().stream().filter(x -> 
            x.getPropertyName().equals(DEFINITION)).findFirst().get().getValue().getContent();
        }
        else{
        return entity.getEntityDescription().getContent();
        }
    }
    
    public static String getConceptDomainValueFromEntityProperty(Entity entity, String conceptDomainPropertyName){
        List<Property> props = entity.getPropertyAsReference();
        if(props.stream().anyMatch(x -> x.getPropertyName().equals(conceptDomainPropertyName))){
            return  props.stream().filter(x -> x.getPropertyName().equals(conceptDomainPropertyName)).
                    findFirst().get().getValue().getContent();
        }
        System.out.println( "No Concept Domain Recorded for property name: " + conceptDomainPropertyName);
        return "No concept domain recorded";
    }
    
    public static SupportedConceptDomain getSupportedConceptDomain(Entity entity, String propertyName, String codingSchemeUri){
       return createSupportedConceptDomain(getConceptDomainValueFromEntityProperty(entity, propertyName),
               codingSchemeUri);
    }
    
    public static SupportedConceptDomain createSupportedConceptDomain(String conceptDomain, String codingSchemeUri) {
        SupportedConceptDomain domain = new SupportedConceptDomain();
        domain.setContent(conceptDomain);
        domain.setLocalId(conceptDomain);
        domain.setUri(codingSchemeUri);
         return domain;  
    }
    
    public static SupportedCodingScheme createSupportedCodingScheme(String codingScheme, String uri) {
        SupportedCodingScheme scheme = new SupportedCodingScheme();
        scheme.setContent(codingScheme);
        scheme.setLocalId(codingScheme);
        scheme.setIsImported(true);
        scheme.setUri(uri);
        return scheme;
    }

    public static SupportedNamespace createSupportedNamespace(String entityCodeNamespace,
            String equivalentCodingScheme, String uri) {
        SupportedNamespace nmsp = new SupportedNamespace();
        nmsp.setContent(entityCodeNamespace);
        nmsp.setLocalId(entityCodeNamespace);
        nmsp.setUri(uri);
        nmsp.setEquivalentCodingScheme(equivalentCodingScheme);
        return nmsp;
    }
    
    
    public static SupportedSource createSupportedSource(String source, String uri){
        SupportedSource newSource = new SupportedSource();
        newSource.setLocalId(source);
        newSource.setContent(source);
        newSource.setUri(uri);
        return newSource;
    }
    
    public static PropertyQualifier createPropertyQualifier(String name, String value) {
        PropertyQualifier pq = new PropertyQualifier();
        pq.setPropertyQualifierName(name);
        Text pqtxt = new Text();
        pqtxt.setContent(value);
        pq.setValue(pqtxt);
        return pq;
    }

    public static String getDefaultSourceIfNull(String sourceName) {
        return sourceName == null?SOURCE_NAME: sourceName;
    }
    
    public static String createUri(String base, String source, String code){
        return base + (source != null ?source + "/":"") + code;
     }
    
   public static List<Property> getPropertiesForPropertyName(List<Property> props, String  name){
        return props.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList());
    }
     
    public static String getPropertyQualifierValueForSource(List<PropertyQualifier> quals){
        if(quals.stream().anyMatch(pq -> pq.getPropertyQualifierName().equals(SOURCE))){
            return quals.stream().filter(pq -> pq.getPropertyQualifierName().equals(SOURCE)).findFirst().get().getValue().getContent();
        }
        return null;
    } 
    
    public static String createSuffixForSourceDefinedResolvedValueSet(String source){
        return "_" + source;
    }
    
    public static CodingScheme transform(Entity entity, String source, String description, Entities entities, String version, String codingSchemeURN)
            throws LBException {
        String codingSchemeUri = AssertedValueSetServices.createUri(BASE, source, entity.getEntityCode());
        String codingSchemeVersion = version == null ? "UNASSIGNED":
                version;

        String codingSchemeName = description == null? entity.getEntityDescription().getContent(): description;

        CodingScheme cs = new CodingScheme();

        cs.setCodingSchemeURI(codingSchemeUri);
        cs.setRepresentsVersion(codingSchemeVersion);
        if (entity.getEffectiveDate() != null)
            cs.setEffectiveDate(entity.getEffectiveDate());
        if (entity.getExpirationDate() != null)
            cs.setExpirationDate(entity.getExpirationDate());
        cs.setEntryState(entity.getEntryState());
        cs.setFormalName(codingSchemeName);
        cs.setCodingSchemeName(truncateDefNameforCodingSchemeName(codingSchemeName));
        cs.setIsActive(entity.getIsActive());
        cs.setMappings(createMappings(entity));
        cs.setOwner(entity.getOwner());
        //init properties
        cs.setProperties(new org.LexGrid.commonTypes.Properties());
        Source lexSource = new Source();
        lexSource.setContent(source == null?DEFAULT_SOURCE:source);
        cs.setSource(new Source[]{lexSource});
        cs.setStatus(entity.getStatus());

        Property prop = new Property();
        prop.setPropertyType(AssertedValueSetServices.GENERIC);
        prop.setPropertyName(AssertedValueSetServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION);
        Text txt = new Text();
        txt.setContent(codingSchemeURN);
        prop.setValue(txt);
        PropertyQualifier pq = AssertedValueSetServices.createPropertyQualifier(AssertedValueSetServices.VERSION,
                version);
        prop.getPropertyQualifierAsReference().add(pq);

        if (codingSchemeName != null) {
            PropertyQualifier pQual = AssertedValueSetServices.createPropertyQualifier(AssertedValueSetServices.CS_NAME, codingSchemeName);
            prop.getPropertyQualifierAsReference().add(pQual);
        }
        cs.getProperties().addProperty(prop);

        cs.setEntities(entities);

        return cs;
    }

    private static Mappings createMappings(Entity entity) {
        Mappings mappings = new Mappings();
        SupportedCodingScheme scheme = AssertedValueSetServices.createSupportedCodingScheme(
                DEFAULT_CODINGSCHEME_NAME, DEFAULT_CODINGSCHEME_URI);
        SupportedConceptDomain domain = AssertedValueSetServices.getSupportedConceptDomain(
                entity, CONCEPT_DOMAIN, DEFAULT_CODINGSCHEME_URI);
        for(SupportedSource source : getSupportedSources(entity)){
            mappings.addSupportedSource(source);
        }
        mappings.addSupportedNamespace(AssertedValueSetServices.
                createSupportedNamespace(entity.getEntityCodeNamespace(), 
                        DEFAULT_CODINGSCHEME_NAME, 
                        DEFAULT_CODINGSCHEME_URI));
        mappings.addSupportedCodingScheme(scheme);
        mappings.addSupportedConceptDomain(domain);
        return mappings;
    }
    
    protected static List<SupportedSource> getSupportedSources(Entity entity) {
        List<SupportedSource> sources = new ArrayList<SupportedSource>();
        List<Property> props = entity.getPropertyAsReference();
        for(Property p: props){
        p.getPropertyQualifierAsReference().stream().
        filter(pq -> pq.getPropertyQualifierName().equals(SOURCE)).
        map(PropertyQualifier::getValue).
        forEach(qual -> 
        sources.add(AssertedValueSetServices.createSupportedSource(
                qual.getContent(),DEFAULT_CODINGSCHEME_URI )));
        }
        return sources;
    }
    
    protected static String truncateDefNameforCodingSchemeName(String name){
        if (StringUtils.isNotEmpty(name) && name.length() > 50) {
            name = name.substring(0, 49);
        }
        return name;
    }

    public static String getConceptCodeForURI(URI uri) {
        String temp = uri.toString();
        temp = temp.substring(temp.lastIndexOf("/") + 1);
        return temp;
    }  

}
