
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.Constants;

public class AssociationFactory {
    
    public static Association createHasSubType() {
        Association assoc = new Association();
        assoc.setAssociationName(Constants.VALUE_HAS_SUB_TYPE);
        
        
        // associated concepts
        AssociatedConceptList assocConList = new AssociatedConceptList();
        AssociatedConcept assocCon = new AssociatedConcept();
        
//        assocCon.s
        
//        assocConList.setAssociatedConcept(arg0)
//        assoc.setAssociatedConcepts(associatedConcepts)
        return null;
    }

}