package org.LexGrid.util.assertedvaluesets;

import java.io.Serializable;

public final class AssertedValueSetParameters implements Serializable {
   
    
    /**
     * 
     */
    private static final long serialVersionUID = 4425605180121824876L;
    public static final String DEFAULT_DO_PUBLISH_NAME ="Publish_Value_Set";
    public static final String DEFAULT_DO_PUBLISH_VALUE = "Yes";
    public static final String BROWSER_VS_DEFINITION = "Term_Browser_Value_Set_Description";
    public static final String DEFINITION = "DEFINITION";
    public static final String CONCEPT_DOMAIN = "Semantic_Type";
    public static final String SOURCE_NAME = "Contributing_Source";
    public static final String SOURCE = "source";
    public static final String DEFAULT_SOURCE = "NCI";
    public static final String ROOT_URI = "http://evs.nci.nih.gov/valueset/";
    public static final String DEFAULT_CODINGSCHEME_URI = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
    public static final String DEFAULT_CODINGSCHEME_NAME = "NCI_Thesaurus";
    public static final String ASSERTED_VALUESET_RELATION = "Concept_In_Subset";
    public static final String DEFAULT_VS_HIERARCHY_RELATION = "subClassOf";
    public static final String ROOT_CONCEPT = "C54443";
    
    private final String publishName;
    private final String publishValue;
    private final String browserVsDescription;
    private final String definition;
    private final String conceptDomain;
    private final String sourceName;
    private final String source;
    private final String defaultSource;
    private final String baseValueSetURI;
    private final String codingSchemeURI;
    private final String codingSchemeName;
    private final String codingSchemeVersion;
    private final String assertedValueSetRelation;
    private final String defaultHierarchyVSRelation;
    private final String rootConcept;

    
    public static class Builder{
        
        private final String codingSchemeVersion;
        
        private String publishName = DEFAULT_DO_PUBLISH_NAME;
        private String publishValue = DEFAULT_DO_PUBLISH_VALUE ;
        private String browserVsDescription = BROWSER_VS_DEFINITION;
        private String definition = DEFINITION;
        private String conceptDomain = CONCEPT_DOMAIN;
        private String sourceName =  SOURCE_NAME;
        private String source = SOURCE;
        private String defaultSource = DEFAULT_SOURCE;
        private String baseValueSetURI = ROOT_URI;
        private String codingSchemeURI = DEFAULT_CODINGSCHEME_URI;
        private String codingSchemeName = DEFAULT_CODINGSCHEME_NAME;
        private String assertedValueSetRelation = ASSERTED_VALUESET_RELATION;
        private String defaultHierarchyVSRelation = DEFAULT_VS_HIERARCHY_RELATION;
        private String rootConcept = ROOT_CONCEPT;
        
        public Builder(String version){
            if(version == null){
                throw new RuntimeException("Coding Scheme version cannot be null");
            }
            this.codingSchemeVersion = version;
        }
        
        public Builder publishName(String val){
            publishName = val;
            return this;
        }
        

        public Builder publishValue(String value){
            publishValue = value;
            return this;
        }
        
        public Builder browserVsDescription(String value){
            browserVsDescription = value;
            return this;
        }
        
        public Builder definition(String value){
            definition = value;
            return this;
        }
        
        public Builder conceptDomain(String value){
            conceptDomain = value;
            return this;
        }
        
        public Builder sourceName(String value){
            sourceName = value;
            return this;
        }
        
        public Builder source(String value){
            source = value;
            return this;
        }
        
        public Builder defaultSource(String value){
            defaultSource = value;
            return this;
        }
        
        public Builder baseValueSetURI(String value){
            baseValueSetURI = value;
            return this;
        }
        
        public Builder codingSchemeURI(String value){
            codingSchemeURI = value;
            return this;
        }
        
        public Builder codingSchemeName(String value){
            codingSchemeName = value;
            return this;
        }
        
        public Builder assertedValueSetRelation(String value){
            assertedValueSetRelation = value;
            return this;
        }
        
        public Builder assertedDefaultHierarchyVSRelation(String value){
            this.defaultHierarchyVSRelation = value;
            return this;
        }
        
        public Builder rootConcept(String value) {
            this.rootConcept = value;
            return this;
        }

        public AssertedValueSetParameters build(){
            return new AssertedValueSetParameters(this);
        }
    }
    
    private AssertedValueSetParameters(Builder builder){
        this.publishName = builder.publishName;
        this.publishValue = builder.publishValue;
        this.browserVsDescription = builder.browserVsDescription;
        this.definition = builder.definition;
        this.conceptDomain = builder.conceptDomain;
        this.sourceName = builder.sourceName;
        this.source = builder.source;
        this.defaultSource = builder.defaultSource;
        this.baseValueSetURI = builder.baseValueSetURI;
        this.codingSchemeURI = builder.codingSchemeURI;
        this.codingSchemeName = builder.codingSchemeName;
        this.codingSchemeVersion = builder.codingSchemeVersion;
        this.assertedValueSetRelation = builder.assertedValueSetRelation;
        this.defaultHierarchyVSRelation = builder.defaultHierarchyVSRelation;
        this.rootConcept = builder.rootConcept;
    }
    
   
    /**
     * @return the publishName
     */
    public String getPublishName() {
        return publishName;
    }


    /**
     * @return the publishValue
     */
    public String getPublishValue() {
        return publishValue;
    }


    /**
     * @return the browserVsDescription
     */
    public String getBrowserVsDescription() {
        return browserVsDescription;
    }


    /**
     * @return the definition
     */
    public String getDefinition() {
        return definition;
    }


    /**
     * @return the conceptDomain
     */
    public String getConceptDomain() {
        return conceptDomain;
    }


    /**
     * @return the sourceName
     */
    public String getSourceName() {
        return sourceName;
    }


    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }


    /**
     * @return the defaultSource
     */
    public String getDefaultSource() {
        return defaultSource;
    }


    /**
     * @return the baseValueSetURI
     */
    public String getBaseValueSetURI() {
        return baseValueSetURI;
    }


    /**
     * @return the codingSchemeURI
     */
    public String getCodingSchemeURI() {
        return codingSchemeURI;
    }


    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return codingSchemeName;
    }


    /**
     * @return the assertedValueSetRelation
     */
    public String getAssertedValueSetRelation() {
        return assertedValueSetRelation;
    }


    /**
     * @return the defaultHierarchyVSRelation
     */
    public String getDefaultHierarchyVSRelation() {
        return defaultHierarchyVSRelation;
    }
    
    
    public String getRootConcept() {
        return rootConcept;
    }


    /**
     * @return the codingSchemeVersion
     */
    public String getCodingSchemeVersion() {
        return codingSchemeVersion;
    }


    public static String setParameter(String param, String defaultParam){
        return param == null? param :defaultParam;
    }
    

}
