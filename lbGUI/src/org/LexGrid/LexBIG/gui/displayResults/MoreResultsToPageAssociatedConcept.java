
package org.LexGrid.LexBIG.gui.displayResults;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

public class MoreResultsToPageAssociatedConcept extends AssociatedConcept {

    private static final long serialVersionUID = 4935276794357489992L;
    
    private String text = "... more results to page";
    public MoreResultsToPageAssociatedConcept() {
        
        this.setCode(text);
    }
}