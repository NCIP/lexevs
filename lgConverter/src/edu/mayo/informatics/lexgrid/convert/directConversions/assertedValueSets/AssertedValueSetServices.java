package edu.mayo.informatics.lexgrid.convert.directConversions.assertedValueSets;

import org.LexGrid.concepts.Entity;

public class AssertedValueSetServices {

    public static String RESOLVED_AGAINST_CODING_SCHEME_VERSION= "resolvedAgainstCodingSchemeVersion";
    public static String VERSION= "version";
    public static String GENERIC= "generic";
    public static String CS_NAME= "codingSchemeName";
    
    private static final String DEFAULT_DO_PUBLISH_NAME ="Publish_Value_Set";
    private static final String DEFAULT_DO_PUBLISH_VALUE = "yes";
    
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
    
    
    
    

}
