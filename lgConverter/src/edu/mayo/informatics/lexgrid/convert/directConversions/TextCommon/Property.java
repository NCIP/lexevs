
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

/**
 * Holder for Concept Properties loaded from Text files.
 * 
 * @author <A HREF="mailto:Dwarkanath.Sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class Property {
    private String conceptCode;
    private String propertyName;
    private String propertyId;
    private String propertyType;
    private String propertyValue;
    private String language;
    private String presentationFormat;
    private String isPreferred;
    private String degreeOfFidelity;
    private String matchIfNoContext;
    private String representationForm;
    private String qualifierName;
    private String qualifierValue;

    public Property(String conceptCode, String propertyName, String propertyId, String propertyType,
            String propertyValue, String language, String presentationFormat, String isPreferred,
            String degreeOfFidelity, String matchIfNoContext, String representationForm, String qualifierName,
            String qualifierValue) {
        this.conceptCode = conceptCode;
        this.propertyName = propertyName;
        this.propertyId = propertyId;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
        this.language = language;
        this.presentationFormat = presentationFormat;
        this.isPreferred = isPreferred;
        this.degreeOfFidelity = degreeOfFidelity;
        this.matchIfNoContext = matchIfNoContext;
        this.representationForm = representationForm;
        this.qualifierName = qualifierName;
        this.qualifierValue = qualifierValue;
    }

    /**
     * Parse the line with format Property="prop_name"
     * ConceptCode="concept_code"
     * PropertyType="property/presentation/definition/comment.."
     * PropertyId="prop_id" PropertyValue="prop_value" Language="lang"
     * PresentationFormat="text/plain" IsPreferred="true/false"
     * DegreeOfFidelity="" MatchIfNoContext="false" RepresentationForm=""
     * QualifierName="prop_qual_name" QualifierValue="prop_qual_value"
     * 
     * @param line
     */
    public Property(String line) {
        this.conceptCode = parseValue(line, "ConceptCode");
        this.propertyName = parseValue(line, "Property");
        this.propertyId = parseValue(line, "PropertyId");
        this.propertyType = parseValue(line, "PropertyType");
        this.propertyValue = parseValue(line, "PropertyValue");
        this.language = parseValue(line, "Language");
        this.presentationFormat = parseValue(line, "PresentationFormat");
        this.isPreferred = parseValue(line, "IsPreferred");
        this.degreeOfFidelity = parseValue(line, "DegreeOfFidelity");
        this.matchIfNoContext = parseValue(line, "MatchIfNoContext");
        this.representationForm = parseValue(line, "RepresentationForm");
        this.qualifierName = parseValue(line, "QualifierName");
        this.qualifierValue = parseValue(line, "QualifierValue");
    }

    public String parseValue(String line, String name) {
        String value = null;
        int pos;
        if ((pos = line.indexOf(name)) != -1) {
            int firstQuoteIndex = line.indexOf("\"", pos);
            int secondQuoteIndex = -1;
            if (firstQuoteIndex != -1) {
                secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
            }
            if (firstQuoteIndex != -1 && secondQuoteIndex != -1) {
                value = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
            }
        }
        return value;
    }

    public boolean isValid() {
        return (conceptCode != null && conceptCode.trim().length() != 0 && propertyName != null
                && propertyName.trim().length() != 0 && propertyType != null && propertyType.trim().length() != 0
                && propertyId != null && propertyId.trim().length() != 0 && propertyValue != null && propertyValue
                .trim().length() != 0);
    }

    /**
     * @return the conceptCode
     */
    public String getConceptCode() {
        return conceptCode;
    }

    /**
     * @return the degreeOfFidelity
     */
    public String getDegreeOfFidelity() {
        return degreeOfFidelity;
    }

    /**
     * @return the isPreferred
     */
    public String getIsPreferred() {
        return isPreferred;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return the matchIfNoContext
     */
    public String getMatchIfNoContext() {
        return matchIfNoContext;
    }

    /**
     * @return the presentationFormat
     */
    public String getPresentationFormat() {
        return presentationFormat;
    }

    /**
     * @return the propertyId
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the propertyType
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * @return the propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * @return the qualifierName
     */
    public String getQualifierName() {
        return qualifierName;
    }

    /**
     * @return the qualifierValue
     */
    public String getQualifierValue() {
        return qualifierValue;
    }

    /**
     * @return the representationForm
     */
    public String getRepresentationForm() {
        return representationForm;
    }

}