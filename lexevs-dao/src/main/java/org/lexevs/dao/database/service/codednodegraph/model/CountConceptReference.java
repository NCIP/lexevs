
package org.lexevs.dao.database.service.codednodegraph.model;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

/**
 * This class is used when I want to create a conceptReference that needs to also hold its child count.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CountConceptReference extends ConceptReference {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 752926435292695777L;
    
    /** The child count. */
    private int childCount=0;

    /**
     * Instantiates a new count concept reference.
     */
    public CountConceptReference(){
    	super();
    }

    /**
     * Instantiates a new count concept reference.
     * 
     * @param cr the cr
     * @param childCount the child count
     */
    public CountConceptReference(ConceptReference cr,  int childCount) {
        this.setCodingSchemeName(cr.getCodingSchemeName());
        this.setCode(cr.getConceptCode());
        this.setCodeNamespace(cr.getCodeNamespace());
        this.childCount= childCount;
    }
    
    /**
     * Instantiates a new count concept reference.
     * 
     * @param codingScheme the coding scheme
     * @param namespace the namespace
     * @param conceptCode the concept code
     * @param childCount the child count
     */
    public CountConceptReference(String codingScheme,  String namespace, String conceptCode, int childCount) {
        this.setCodingSchemeName(codingScheme);
        this.setCode(conceptCode);
        this.setCodeNamespace(namespace);
        this.childCount= childCount;
    }
    
    /**
     * Gets the child count.
     * 
     * @return the child count
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * Sets the child count.
     * 
     * @param childCount the new child count
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}