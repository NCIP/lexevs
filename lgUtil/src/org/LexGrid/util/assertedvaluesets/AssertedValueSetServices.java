package org.LexGrid.util.assertedvaluesets;


import java.net.URI;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Utility.Constructors;
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

    public static final String RESOLVED_AGAINST_CODING_SCHEME_VERSION = "resolvedAgainstCodingSchemeVersion";
    public static final String VERSION = "version";
    public static final String GENERIC = "generic";
    public static final String CS_NAME = "codingSchemeName";
    public static final String SOURCE_SCHEME_NAME = "textualPresentation";
    
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

    
    private static final String ELIPSIS = "...";

    public enum BaseName {
        CODE("Code"), NAME("Name");
        private String value;

        BaseName(String name) {
            value = name;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    
    public static boolean isPublishableValueSet(Entity entity, boolean force) {
        nullEntityCheck(entity);
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
        nullEntityCheck(entity);
        boolean isPresent = entity.getPropertyAsReference().stream().anyMatch(x -> 
        x.getPropertyName().equals(BROWSER_VS_DEFINITION));
        
        boolean isDefinition = entity.getDefinitionAsReference().stream().anyMatch(x -> 
        x.getPropertyName().equals(DEFINITION));
        
        if(isPresent){
            return entity.getPropertyAsReference().stream().filter(x -> x.
                    getPropertyName().equals(BROWSER_VS_DEFINITION)).findFirst().get().getValue().getContent();
        }
        else if(isDefinition){
            return entity.getDefinitionAsReference().stream().filter(x -> 
            x.getPropertyName().equals(DEFINITION)).findFirst().get().getValue().getContent();
        }
        else{
        return entity.getEntityDescription().getContent();
        }
    }
    
    public static String getConceptDomainValueFromEntityProperty(Entity entity, String conceptDomainPropertyName){
        nullEntityCheck(entity);
        if(conceptDomainPropertyName == null) { throw new RuntimeException("Concept Domain Cannot Be Null");}
        List<Property> props = entity.getPropertyAsReference();
        if(props.stream().anyMatch(x -> x.getPropertyName().equals(conceptDomainPropertyName))){
            return  props.stream().filter(x -> x.getPropertyName().equals(conceptDomainPropertyName)).
                    findFirst().get().getValue().getContent();
        }
        System.out.println( "No Concept Domain Recorded for property name: " + conceptDomainPropertyName);
        return "No concept domain recorded";
    }
    
    public static SupportedConceptDomain getSupportedConceptDomain(Entity entity, String propertyName, String codingSchemeUri){
        nullEntityCheck(entity);
        if(propertyName == null || codingSchemeUri == null) { throw new RuntimeException("Property Name or URI cannot be null");}
       return createSupportedConceptDomain(getConceptDomainValueFromEntityProperty(entity, propertyName),
               codingSchemeUri);
    }
    
    public static SupportedConceptDomain createSupportedConceptDomain(String conceptDomain, String codingSchemeUri) {
        if(conceptDomain == null || codingSchemeUri == null) { throw new RuntimeException("Concept Domain or URI cannot be null");}
        SupportedConceptDomain domain = new SupportedConceptDomain();
        domain.setContent(conceptDomain);
        domain.setLocalId(conceptDomain);
        domain.setUri(codingSchemeUri);
         return domain;  
    }
    
    public static SupportedCodingScheme createSupportedCodingScheme(String codingScheme, String uri) {
        if(codingScheme == null || uri == null) { throw new RuntimeException("Coding scheme or URI cannot be null");}
        SupportedCodingScheme scheme = new SupportedCodingScheme();
        scheme.setContent(codingScheme);
        scheme.setLocalId(codingScheme);
        scheme.setIsImported(true);
        scheme.setUri(uri);
        return scheme;
    }

    public static SupportedNamespace createSupportedNamespace(String entityCodeNamespace,
            String equivalentCodingScheme, String uri) {
        if(entityCodeNamespace == null || equivalentCodingScheme == null || uri == null)
        { throw new RuntimeException("namespace, coding scheme or URI cannot be null");}
        SupportedNamespace nmsp = new SupportedNamespace();
        nmsp.setContent(entityCodeNamespace);
        nmsp.setLocalId(entityCodeNamespace);
        nmsp.setUri(uri);
        nmsp.setEquivalentCodingScheme(equivalentCodingScheme);
        return nmsp;
    }
    
    
    public static SupportedSource createSupportedSource(String source, String uri){
        if(source == null || uri == null) { throw new RuntimeException("source or URI cannot be null");}
        SupportedSource newSource = new SupportedSource();
        newSource.setLocalId(source);
        newSource.setContent(source);
        newSource.setUri(uri);
        return newSource;
    }
    
    public static PropertyQualifier createPropertyQualifier(String name, String value) {
        if(name == null || value == null) 
        {throw new RuntimeException("name or value cannot be null");}
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
        if(code == null) {throw new RuntimeException("Code in URI contruct cannot be null");}
        return (base == null? BASE: base) + (source != null ?source + "/":"") + code;
     }
    
   public static List<Property> getPropertiesForPropertyName(List<Property> props, String  name){
       if(props == null || props.size() < 1) {throw new RuntimeException("Propertly list cannot be empty or null");}
       if(name == null) {throw new RuntimeException("Name of property cannot be null");}
        return props.stream().filter(x -> x.getPropertyName().equals(name)).collect(Collectors.toList());
    }
     
    public static String getPropertyQualifierValueForSource(List<PropertyQualifier> quals){
        if(quals.stream().anyMatch(pq -> pq.getPropertyQualifierName().equals(SOURCE))){
            return quals.stream().filter(pq -> pq.getPropertyQualifierName().equals(SOURCE)).findFirst().get().getValue().getContent();
        }
        return null;
    } 
    
    public static String createSuffixForSourceDefinedResolvedValueSet(String source){
        if(source == null) {throw new RuntimeException("Name of source cannot be null");}
        return "_" + source;
    }
    
    public static CodingScheme transform(Entity entity, String source, String description, Entities entities, 
            String version, String codingSchemeURN)
            throws LBException {
        nullEntityCheck(entity);
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
        cs.setCodingSchemeName(truncateDefNameforCodingSchemeName(codingSchemeName, false));
        String definitionProperty = null;
        if(entity.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(DEFINITION)).findAny().isPresent())
        {definitionProperty = entity.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(DEFINITION)).
        map(x -> x.getValue().getContent()).collect(Collectors.toList()).get(0);}
        cs.setEntityDescription(Constructors.createEntityDescription(definitionProperty));
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
    
    public static ResolvedConceptReference transformEntityToRCR(Entity entity, AssertedValueSetParameters params) {
        ResolvedConceptReference ref = new ResolvedConceptReference();
        ref.setCode(entity.getEntityCode());
        ref.setCodeNamespace(entity.getEntityCodeNamespace());
        ref.setEntityType(entity.getEntityType());
        ref.setCodingSchemeName(params.getCodingSchemeName());
        ref.setCodingSchemeURI(params.getCodingSchemeURI());
        ref.setCodingSchemeVersion(params.getCodingSchemeVersion());
        ref.setConceptCode(ref.getCode());
        ref.setCode(ref.getCode());
        ref.setEntity(entity);
        if(StringUtils.isNotBlank(entity.getEntityDescription().getContent())){
            ref.setEntityDescription(Constructors.createEntityDescription(
                    entity.getEntityDescription().getContent()));
        }
        return ref;
    }
    
    public static Entity transformRCRtoEntity(ResolvedConceptReference ref, AssertedValueSetParameters params) {
        Entity entity = new Entity();
        entity.setEntityCode(ref.getCode());
         entity.setEntityCodeNamespace(ref.getCodeNamespace());
         entity.setEntityType(ref.getEntityType());
        if(ref.getEntityDescription() != null && StringUtils.isNotBlank(ref.getEntityDescription().getContent())){
            entity.setEntityDescription(
                    ref.getEntityDescription());
        }
        entity.setProperty(ref.getEntity().getAllProperties());
        return entity;
    }

    private static Mappings createMappings(Entity entity) {
        nullEntityCheck(entity);
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
    
    public static List<String> getDiff(final String a, final String b){
        List<String> diffs;
        if(a.length() > b.length()){
        List<String> compareTo = Arrays.asList(b.split(" ")); 
        diffs = Arrays.asList(a.split(" ")).stream().filter(x -> !compareTo.contains(x)).collect(Collectors.toList());
        }
        else{
            List<String> compareTo = Arrays.asList(a.split(" ")); 
            diffs = Arrays.asList(b.split(" ")).stream().filter(x -> !compareTo.contains(x)).collect(Collectors.toList());
        }
        return diffs;
    }
    
    
    public static String processForDiff(String shortName, String similarName, HashMap<String, String> truncatedNames) {
        String originalValue = truncatedNames.get(shortName);
        List<String> diff = getDiff(similarName, originalValue);
        return createDifferentBaseName(shortName, diff);
    }

    public static String createDifferentBaseName(String shortName, List<String> diff) {
        if(!diffInShortName(shortName, diff)){
            String diffConcat = diff.stream().reduce((x,y) -> x.concat(" " + y)).get(); 
            shortName = shortName.substring(0, shortName.length() - diffConcat.length());
            if(shortName.contains(" ")){
            shortName = shortName.substring(0, shortName.lastIndexOf(" ") + 1);
            }
            shortName = shortName.concat(diffConcat);
            return shortName;
        }
        return shortName;
    }
    
    protected static List<SupportedSource> getSupportedSources(Entity entity) {
        nullEntityCheck(entity);
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
    
    public static String truncateDefNameforCodingSchemeName(String name, boolean stored){
        if(name == null) {throw new RuntimeException("Coding Scheme name cannot be null");}
        if(!stored){ return name;}
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
    
    private static void nullEntityCheck(Entity entity) {
        if(entity == null) {throw new RuntimeException("Enity cannot be null");}
    }
    
    public static boolean diffInShortName(String shortName, List<String> diff) {
        return diff.stream().allMatch(x -> shortName.contains(x));
    }

    public static String getCononicalDiffValue(String diff){
        Set<String> canonicalValue =  Arrays.asList(BaseName.values()).stream().
                filter(base -> diff.contains(base.value)).
                map(value -> value.value).
                collect(Collectors.toSet());
        if (canonicalValue.size() == 1){
            return canonicalValue.iterator().next();
        }
        return null;
    }
    
    public static String truncateDefNameforCodingSchemeName(final String name, HashMap<String, String> truncatedNames){
        if (StringUtils.isNotEmpty(name) && name.length() > 50) {
            String breakAdj = name;
            breakAdj = breakOnCommonDiff(breakAdj);
            String shortName = abbreviateFromMiddle(breakAdj, ELIPSIS, 50);
          if(truncatedNames.containsKey(shortName)){
                shortName = AssertedValueSetServices.processForDiff(shortName, breakAdj, truncatedNames);
                if(shortNameExistsAsKey(shortName, truncatedNames.keySet())){
                    shortName = getAlternativeNamingForShortName(shortName, breakAdj, truncatedNames);
                    System.out.println(shortName + " : " + name);
                }
            }
            truncatedNames.put(shortName, name);
            return shortName;
        }
        return name;
    }

    public static String getAlternativeNamingForShortName(String shortName, final String name, HashMap<String, String> truncatedNames) {
            List<String> diffs = getDiff(name, truncatedNames.get(shortName));
            //Already abbreviated with elipsis
            if(!(shortName.indexOf(ELIPSIS) == -1)){
            shortName = shortName.substring(0,shortName.length() - shortName.indexOf(ELIPSIS) + 3);
            }
            else{
                shortName = shortName.substring(0, shortName.length()/2);
            }
            final String temp = shortName;
            boolean success = false;
            if(diffs.stream().filter(s -> truncatedNames.get(temp.concat(s)) == null).findAny().isPresent()){
                shortName = shortName.concat(diffs.stream().filter(s -> truncatedNames.get(temp.concat(s)) == null).findAny().get());
                return shortName;
            }
            int counter = 0;
            while(!success){
                shortName = temp.concat(String.valueOf(counter));
                if(truncatedNames.get(temp) == null){
                    success = true;
                }
                counter++;
            }
            return shortName;
    }

   public static String abbreviateFromMiddle(final String name, String string, int i) {
     if(name == null || name.length() <= i){
         return name;
     }
     String first = name.substring(0, (i - string.length())/2);
     String remainder = name.substring(name.length() - (i - string.length())/2, name.length());
     return first + string  + remainder;
    }

public static String breakOnCommonDiff(String name) {
        String[] tokenNames = StringUtils.split(name);
        List<BaseName> canonicalValues = Arrays.asList(BaseName.values());
        Set<String> breakOn = canonicalValues.stream().
                filter(x -> Arrays.asList(tokenNames).
                contains(x.value)).
                map(y -> y.value).
                collect(Collectors.toSet());
        if(breakOn.size() > 0){
        String value = breakOn.iterator().next();
        name = name.substring(0, name.indexOf(value) + value.length());
        }
        return name;
    }

    public static boolean shortNameExistsAsKey(String shortName, Set<String> keySet) {
        return keySet.stream().anyMatch(x -> x.equals(shortName));
    }

    public static String getNameSpaceForCodingScheme(CodingScheme scheme){
        //Currently we only declare one namespace for a coding scheme
        //It is possible to have more than one namespace for contained entities however
        //So this implementation is limited in its scope
        if(scheme.getMappings().enumerateSupportedNamespace().hasMoreElements()){
        return scheme.getMappings().enumerateSupportedNamespace().nextElement().getContent();
        }else{
            return scheme.getMappings().enumerateSupportedCodingScheme().hasMoreElements()?
                scheme.getMappings().enumerateSupportedCodingScheme().nextElement().getLocalId():
                    scheme.getCodingSchemeName();
        }
    }
    
    public static String getDefinedSource(String sourceName, Entity entity) {
        String source = null;
        if(entity.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(sourceName)).findAny().isPresent())
        {source = entity.getPropertyAsReference().stream().filter(x -> x.getPropertyName().equals(sourceName))
        .map(x -> x.getValue().getContent()).collect(Collectors.toList()).get(0);}
        return source;
    }

}
