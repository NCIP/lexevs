
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;

public class EntityFactory {
    
    /*
     * example XML from lbTest, automobiles2.xml
     */
    
    
    /* 
     * <lgCon:entity entityCode="005" isActive="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Domestic Auto Makers</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation propertyName="textualPresentation" propertyId="p1" isPreferred="true">
                <lgCommon:value>Domestic Auto Makers</lgCommon:value>
            </lgCon:presentation>
            <lgCon:presentation propertyName="textualPresentation" propertyId="p2" isPreferred="false">
                <lgCommon:value>>American Car Companies</lgCommon:value>
            </lgCon:presentation>
        </lgCon:entity>

     */
    public static Entity createEntityAutoMaker() {
        Entity entity = new Entity();
        entity.setEntityCode("005");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Domestic Auto Makers");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("p1");
        p1.setIsPreferred(new Boolean(true));
        Text text = new Text();
        text.setContent("Domestic Auto Makers");
        p1.setValue(text);
        
        Presentation p2 = new Presentation();
        p2.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p2.setPropertyId("p2");
        p2.setIsPreferred(new Boolean(false));
        Text text2 = new Text();
        text2.setContent("American Car Companies");
        p2.setValue(text2);
        
        Presentation[] presentationAr = {p1, p2};
        
        entity.setPresentation(presentationAr);
        
        return entity;
        
    }
    
    /*    
        <lgCon:entity entityCode="Ford" isActive="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Ford Motor Company</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation propertyName="textualPresentation" propertyId="p1" isPreferred="true">
                <lgCommon:value>Ford</lgCommon:value>
            </lgCon:presentation>
            <lgCon:presentation propertyName="textualPresentation" propertyId="p2" isPreferred="false">
                <lgCommon:value>Ford Motor Company</lgCommon:value>
            </lgCon:presentation>
        </lgCon:entity>
    */
    public static Entity createEntityFord() {
        Entity entity = new Entity();
        entity.setEntityCode("Ford");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Ford Motor Company<");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("p1");
        p1.setIsPreferred(new Boolean(true));
        Text text = new Text();
        text.setContent("Ford");
        p1.setValue(text);
        
        Presentation p2 = new Presentation();
        p2.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p2.setPropertyId("p2");
        p2.setIsPreferred(new Boolean(false));
        Text text2 = new Text();
        text2.setContent("Ford Motor Company");
        p2.setValue(text2);
        
        Presentation[] presentationAr = {p1, p2};
        
        entity.setPresentation(presentationAr);
        
        return entity;
        
    }

    
    /*
       <lgCon:entity  entityCode="A0001" status="asfd" isActive="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Automobile</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation language="en" propertyName="textualPresentation" propertyId="t1" isPreferred="true" matchIfNoContext="true">
                <lgCommon:source>A0001</lgCommon:source>
                <lgCommon:value dataType="textplain">Automobile</lgCommon:value>
            </lgCon:presentation>
            <lgCon:definition language="en"  propertyName="definition" propertyId="p1" isPreferred="true">
                <lgCommon:source>A0001</lgCommon:source>
                <lgCommon:value dataType="textplain">An automobile</lgCommon:value>
            </lgCon:definition>
        </lgCon:entity>
     */
    public static Entity createEntityAutomobile() {
        Entity entity = new Entity();
        entity.setEntityCode("A0001");
        entity.setStatus("asfd");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Automobile");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentation
        Presentation t1 = new Presentation();
        t1.setLanguage(Constants.VALUE_LANG_EN);
        t1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        t1.setPropertyId("t1");
        t1.setIsPreferred(new Boolean(true));
        t1.setMatchIfNoContext(new Boolean(true));
        
        Source source1 = new Source();
        source1.setContent("A0001");
        Source[] sAr = {source1};
        t1.setSource(sAr);
        
        Text text1 = new Text();
        text1.setDataType("textplain");
        text1.setContent("Automobile");
        t1.setValue(text1);

        Presentation[] presentationAr = {t1};
        entity.setPresentation(presentationAr);
        
        // defintion
        Definition def = new Definition();
        def.setLanguage(Constants.VALUE_LANG_EN);
        def.setPropertyName(Constants.VALUE_PROP_NAME_DEFINITON);
        def.setPropertyId("p1");
        def.setIsPreferred(new Boolean(true));
        
        Source source2 = new Source();
        source2.setContent("A0001");
        Source[] sAr2 = {source2};
        def.setSource(sAr2);

        Text text2 = new Text();
        text2.setDataType("textplain");
        text2.setContent("An automobile");
        def.setValue(text2);
        
        Definition[] defAr = {def};
        entity.setDefinition(defAr);
        
        return entity;
        
    }

    /*
        <lgCon:entity entityCode="@" status="asdf" isActive="true" isAnonymous="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Top level node for relationships</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation propertyName="textualPresentation" propertyId="t1" isPreferred="true" matchIfNoContext="true">
                <lgCommon:source>GermanMadeParts</lgCommon:source>
                <lgCommon:value>Top level node for relationships</lgCommon:value>
            </lgCon:presentation>
        </lgCon:entity>
     */
    public static Entity createEntityRoot() {
        Entity entity = new Entity();
        entity.setEntityCode("@");
        entity.setStatus("asdf");
        entity.setIsActive(new Boolean(true));
        entity.setIsAnonymous(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Top level node for relationships");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("t1");
        p1.setIsPreferred(new Boolean(true));
        p1.setMatchIfNoContext(new Boolean(true));
        
        //set the source
        Source source1 = new Source();
        source1.setContent("GermanMadeParts");
        Source[] sAr = {source1};
        p1.setSource(sAr);
        
        // set the value
        Text text = new Text();
        text.setContent("Top level node for relationships");
        p1.setValue(text);
        
        Presentation[] presentationAr = {p1};
        entity.setPresentation(presentationAr);
        return entity;
        
    }

    /*
        <lgCon:entity entityCode="C0001" status="asf" isActive="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Car</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation language="en" propertyName="textualPresentation" propertyId="c1" isPreferred="true" matchIfNoContext="true">
                <lgCommon:value>Car</lgCommon:value>
            </lgCon:presentation>
        </lgCon:entity>
     */
    public static Entity createEntityCar() {
        Entity entity = new Entity();
        entity.setEntityCode("C0001");
        entity.setStatus("asf");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Car");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setLanguage(Constants.VALUE_LANG_EN);
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("c1");
        p1.setIsPreferred(new Boolean(true));
        p1.setMatchIfNoContext(new Boolean(true));
        
        // set the value
        Text text = new Text();
        text.setContent("Car");
        p1.setValue(text);
        
        Presentation[] presentationAr = {p1};
        entity.setPresentation(presentationAr);
        return entity;
        
    }

    /*    
        <lgCon:entity entityCode="T0001" status="a65" isActive="true" entityCodeNamespace="Automobiles">
            <lgCommon:entityDescription>Truck</lgCommon:entityDescription>
            <lgCon:entityType>concept</lgCon:entityType>
            <lgCon:presentation language="en" propertyName="textualPresentation" propertyId="t1" isPreferred="true" matchIfNoContext="true">
                <lgCommon:value>Truck</lgCommon:value>
            </lgCon:presentation>
        </lgCon:entity>
    */
    public static Entity createEntityTruck() {
        Entity entity = new Entity();
        entity.setEntityCode("T0001");
        entity.setStatus("a65");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Truck");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setLanguage(Constants.VALUE_LANG_EN);
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("t1");
        p1.setIsPreferred(new Boolean(true));
        p1.setMatchIfNoContext(new Boolean(true));
        
        // set the value
        Text text = new Text();
        text.setContent("Truck");
        p1.setValue(text);
        
        Presentation[] presentationAr = {p1};
        entity.setPresentation(presentationAr);
        return entity;
        
    }
    
    public static Entity createEntityTires() {
        Entity entity = new Entity();
        entity.setEntityCode("Tires");
        entity.setStatus("a66");
        entity.setIsActive(new Boolean(true));
        entity.setEntityCodeNamespace(Constants.VALUE_AUTOMOBILES_NAME_SPACE);
        
        // description
        EntityDescription ed = new EntityDescription();
        ed.setContent("Tires");
        entity.setEntityDescription(ed);
        
        // entityType
        String[] stringAr = {Constants.VALUE_ENTITY_TYPE_CONCEPT};
        entity.setEntityType(stringAr);
        
        // presentations
        Presentation p1 = new Presentation();
        p1.setLanguage(Constants.VALUE_LANG_EN);
        p1.setPropertyName(Constants.VALUE_PROP_NAME_TEXT_PRES);
        p1.setPropertyId("t1");
        p1.setIsPreferred(new Boolean(true));
        p1.setMatchIfNoContext(new Boolean(true));
        
        // set the value
        Text text = new Text();
        text.setContent("Tires");
        p1.setValue(text);
        
        Presentation[] presentationAr = {p1};
        entity.setPresentation(presentationAr);
        return entity;
    }    
}