
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.Relations;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;

public class CodingSchemeFactory {
    
    public static CodingScheme createCodingScheme() {
        CodingScheme cs = new CodingScheme();
        cs.setApproxNumConcepts(new Long(4));
        cs.setCodingSchemeName("miniautomobiles");
        cs.setCodingSchemeURI(Constants.VALUE_CODING_SCHEME_URI);
        Text copyright = new Text();
        copyright.setContent("Copyright Mayo Clinic.");
        cs.setCopyright(copyright);
        cs.setDefaultLanguage(Constants.VALUE_LANG_EN);
        cs.setMappings(new Mappings());
        cs.setRepresentsVersion(Constants.VALUE_CODING_SCHEME_VERSION);
        
        SupportedAssociation saHasSubType = SupportedAssociationFactory.createSupportedAssociationHasSubType();
        cs.getMappings().addSupportedAssociation(saHasSubType);
        SupportedAssociation saUses = SupportedAssociationFactory.createSupportedAssociationUses();
        cs.getMappings().addSupportedAssociation(saUses);        
        
        return cs;
    }
    
    public static CodingScheme createCodingSchemeWithAssociationPredicate() {
        CodingScheme cs = new CodingScheme();
        cs.setApproxNumConcepts(new Long(4));
        cs.setCodingSchemeName("miniautomobiles");
        cs.setCodingSchemeURI(Constants.VALUE_CODING_SCHEME_URI);
        Text copyright = new Text();
        copyright.setContent("Copyright Mayo Clinic.");
        cs.setCopyright(copyright);
        cs.setDefaultLanguage(Constants.VALUE_LANG_EN);
        cs.setMappings(new Mappings());
        cs.setRepresentsVersion(Constants.VALUE_CODING_SCHEME_VERSION);
        
        SupportedAssociation saHasSubType = SupportedAssociationFactory.createSupportedAssociationHasSubType();
        cs.getMappings().addSupportedAssociation(saHasSubType);
        SupportedAssociation saUses = SupportedAssociationFactory.createSupportedAssociationUses();
        cs.getMappings().addSupportedAssociation(saUses);
        
        Relations rels = new Relations();
        rels.setContainerName("asD");
        
        AssociationPredicate ap = new AssociationPredicate();
        ap.setAssociationName("hasSubtype");
        
        rels.addAssociationPredicate(ap);
        
        ap = new AssociationPredicate();
        ap.setAssociationName("uses");
        
        rels.addAssociationPredicate(ap);
        
        cs.addRelations(rels);
        
        return cs;
    }
}