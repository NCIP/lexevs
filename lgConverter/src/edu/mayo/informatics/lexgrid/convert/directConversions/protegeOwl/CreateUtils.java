
package edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl;

import java.util.Collection;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.types.PropertyTypes;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.Conversions.SupportedMappings;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

public class CreateUtils {
    // /////////////////////////////////////////////
    // // Create and register LexGrid objects //////
    // /////////////////////////////////////////////

    public static AssociationData createAssociationTextData(String data) {
        AssociationData lgData = new AssociationData();
        lgData.setAssociationDataText(createText(data));
        return lgData;
    }

    public static AssociationQualification createAssociationQualification(RDFProperty rdfProp, SupportedMappings lgSupportedMappings_) {
        String brText = rdfProp.getBrowserText();
        Collection labels = rdfProp.getLabels();
        AssociationQualification lgQual = createAssociationQualification(brText, rdfProp.getURI(),
                labels.isEmpty() ? brText : labels.iterator().next().toString(), lgSupportedMappings_);
        return lgQual;
    }

    public static AssociationQualification createAssociationQualification(String name, String uri, String descriptiveText, SupportedMappings lgSupportedMappings_) {
        AssociationQualification lgQual = new AssociationQualification();
        lgQual.setAssociationQualifier(name);
        lgQual.setQualifierText(createText(descriptiveText));
        lgSupportedMappings_.registerSupportedAssociationQualifier(name, uri, descriptiveText, false);
        return lgQual;
    }

    public static AssociationSource createAssociationSource(String lgConceptCode, String namespace) {
        AssociationSource lgSrc = new AssociationSource();
        if (StringUtils.isNotBlank(lgConceptCode))
            lgSrc.setSourceEntityCode(lgConceptCode);
        if (StringUtils.isNotBlank(namespace))
            lgSrc.setSourceEntityCodeNamespace(namespace);
        return lgSrc;
    }

    public static AssociationTarget createAssociationTarget(String lgConceptCode, String namespace) {
        AssociationTarget lgTgt = new AssociationTarget();
        if (lgConceptCode != null)
            lgTgt.setTargetEntityCode(lgConceptCode);
        if (namespace != null)
            lgTgt.setTargetEntityCodeNamespace(namespace);
        return lgTgt;
    }

    public static Comment createComment(String propID, String propName, String text, SupportedMappings lgSupportedMappings_, String propURI, String lang) {
        Comment lgComment = new Comment();
        lgComment.setPropertyId(propID);
        lgComment.setPropertyName(propName);
        lgComment.setValue(createText(text));
        lgComment.setLanguage(lang);
        lgSupportedMappings_.registerSupportedProperty(propName, propURI, propName, PropertyTypes.COMMENT, false);
        return lgComment;
    }

    public static Definition createDefinition(String propID, String propName, String text, Boolean isPreferred, SupportedMappings lgSupportedMappings_, String propURI, String lang) {
        Definition lgDefn = new Definition();
        lgDefn.setPropertyId(propID);
        lgDefn.setPropertyName(propName);
        lgDefn.setValue(createText(text));
        lgDefn.setLanguage(lang);
        if (isPreferred != null)
            lgDefn.setIsPreferred(isPreferred);
        lgSupportedMappings_.registerSupportedProperty(propName, propURI, propName, PropertyTypes.DEFINITION, false);
        return lgDefn;
    }

    public static Presentation createPresentation(String propID, String propName, String text, Boolean isPreferred, SupportedMappings lgSupportedMappings_, String propURI, String lang) {
        
        Presentation lgPres = new Presentation();
        lgPres.setPropertyId(propID);
        lgPres.setPropertyName(propName);
        lgPres.setValue(createText(text));
        lgPres.setLanguage(lang);
        if (isPreferred != null)
            lgPres.setIsPreferred(isPreferred);
        lgSupportedMappings_.registerSupportedProperty(propName, propURI, propName, PropertyTypes.PRESENTATION, false);
        return lgPres;
    }

    public static Property createProperty(String propID, String propName, String text, SupportedMappings lgSupportedMappings_, String propURI, String lang) {
        
        Property lgProp = new Property();
        lgProp.setPropertyName(propName);
        lgProp.setPropertyId(propID);
        lgProp.setValue(createText(text));
        lgProp.setLanguage(lang);
        lgSupportedMappings_.registerSupportedProperty(propName, propURI, propName, PropertyTypes.PROPERTY, false);
        return lgProp;
    }

    public static PropertyQualifier createPropertyQualifier(String tag, String text, SupportedMappings lgSupportedMappings_) {
        PropertyQualifier lgPropQual = new PropertyQualifier();
        lgPropQual.setPropertyQualifierName(tag);
        lgPropQual.setValue(createText(text));
        lgSupportedMappings_.registerSupportedPropertyQualifier(tag, null, tag, false);
        return lgPropQual;
    }

    public static Source createSource(String value, String role, String subref, SupportedMappings lgSupportedMappings_) {
        Source lgSource = new Source();
        lgSource.setContent(value);
        lgSource.setRole(role);
        lgSource.setSubRef(subref);
        if (value != null)
            lgSupportedMappings_.registerSupportedSource(value, null, value, null, false);
        if (role != null)
            lgSupportedMappings_.registerSupportedSourceRole(role, null, role, false);
        return lgSource;
    }

    public static Text createText(String value) {
        Text lgText = new Text();
        lgText.setContent(value);
        lgText.setDataType(ProtegeOwl2LGConstants.DATATYPE_STRING);
        return lgText;
    }
}