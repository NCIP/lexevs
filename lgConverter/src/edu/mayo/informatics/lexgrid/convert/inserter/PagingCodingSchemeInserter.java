
package edu.mayo.informatics.lexgrid.convert.inserter;

/**
 * The Interface PagingCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PagingCodingSchemeInserter extends CodingSchemeInserter {

    /**
     * Sets the entity page size.
     * 
     * @param pageSize the new entity page size
     */
    public void setEntityPageSize(int pageSize);
    
    /**
     * Sets the association instance page size.
     * 
     * @param pageSize the new association instance page size
     */
    public void setAssociationInstancePageSize(int pageSize);
}