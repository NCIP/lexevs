
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.naming.SupportedAssociation;

public class SupportedAssociationFactory {
    
    /*
     * example XML from lbTest, automobiles2.xml
     */
    
    /*
     *  <lgNaming:supportedAssociation uri="urn:oid:1.3.6.1.4.1.2114.108.1.8.1" localId="hasSubtype">hasSubtype</lgNaming:supportedAssociation>
     */
    
    public static SupportedAssociation createSupportedAssociationHasSubType() {
        return SupportedAssociationFactory.createSupportedAssociation(
                "urn:oid:1.3.6.1.4.1.2114.108.1.8.1", 
                "hasSubType", 
                "hasSubType");
    }
    
    /*
     * <lgNaming:supportedAssociation uri="urn:oid:11.11.0.1" localId="uses">uses</lgNaming:supportedAssociation>
     */
    public static SupportedAssociation createSupportedAssociationUses() {
        return SupportedAssociationFactory.createSupportedAssociation(
                "urn:oid:11.11.0.7", 
                "uses", 
                "uses");        
    }
    
    private static SupportedAssociation createSupportedAssociation(String uri, String localId, String content) {
        SupportedAssociation sa = new SupportedAssociation();
        sa.setUri(uri);
        sa.setLocalId(localId);
        sa.setContent(content);
        return sa;                
    }    
}