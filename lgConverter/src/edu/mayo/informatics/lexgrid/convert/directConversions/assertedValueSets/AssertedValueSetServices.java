package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedConceptDomain;

public class AssertedValueSetServices {

    public static String RESOLVED_AGAINST_CODING_SCHEME_VERSION= "resolvedAgainstCodingSchemeVersion";
    public static String VERSION= "version";
    public static String GENERIC= "generic";
    public static String CS_NAME= "codingSchemeName";
    
    public static final String DEFAULT_DO_PUBLISH_NAME ="Publish_Value_Set";
    public static final String DEFAULT_DO_PUBLISH_VALUE = "yes";
    public static final String BROWSER_VS_DEFINITION = "Term_Browser_Value_Set_Description";
    public static final String DEFINITION = "DEFINITION";
    public static final String CONCEPT_DOMAIN = "Semantic_Type";
    
    public static boolean isPublishableValueSet(Entity entity) {
        if(entity.getPropertyAsReference().stream().filter(x -> x.
                   getPropertyName().equals(DEFAULT_DO_PUBLISH_NAME)).findFirst().isPresent()){
           String publish =  entity.getPropertyAsReference().stream().filter(x -> x.
                    getPropertyName().equals(DEFAULT_DO_PUBLISH_NAME)).findFirst().get().getValue().getContent();
          if(publish.equalsIgnoreCase(DEFAULT_DO_PUBLISH_VALUE)){
              return true;
          }
       }
           return false;
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

    
    
    

}
