package org.LexGrid.util.assertedvaluesets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedConceptDomain;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.apache.commons.lang.StringUtils;

public class AssertedValueSetServices {

    public static String RESOLVED_AGAINST_CODING_SCHEME_VERSION= "resolvedAgainstCodingSchemeVersion";
    public static String VERSION= "version";
    public static String GENERIC= "generic";
    public static String CS_NAME= "codingSchemeName";
    public static String SOURCE_SCHEME_NAME= "textualPresentation";
    
    public static final String DEFAULT_DO_PUBLISH_NAME ="Publish_Value_Set";
    public static final String DEFAULT_DO_PUBLISH_VALUE = "yes";
    public static final String BROWSER_VS_DEFINITION = "Term_Browser_Value_Set_Description";
    public static final String DEFINITION = "DEFINITION";
    public static final String CONCEPT_DOMAIN = "Semantic_Type";
    public static final String SOURCE_NAME = "Contributing_Source";
    public static final String SOURCE = "source";
    public static final String BASE = "http://evs.nci.nih.gov/valueset/";
    private static final String ELIPSIS = "...";
    public enum BaseName{
        CODE("Code"), NAME("Name"); 
        private String value; 
        BaseName(String name){value = name;}
        @Override
        public String toString() {
            return value;
        }
        }
    
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


    
    
    

}
