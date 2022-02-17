
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.List;

/**
 * The Interface CodeHolder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodeHolder extends Serializable, Cloneable {
    
    /**
     * Difference.
     * 
     * @param otherCodes the other codes
     */
    public void difference(CodeHolder otherCodes);
    
    /**
     * Union.
     * 
     * @param otherCodes the other codes
     */
    public void union(CodeHolder otherCodes); 
    
    /**
     * Intersect.
     * 
     * @param otherCodes the other codes
     */
    public void intersect(CodeHolder otherCodes);
    
    /**
     * Contains.
     * 
     * @param code the code
     * 
     * @return true, if successful
     */
    public boolean contains(CodeToReturn code);
    
    /**
     * Gets the all codes.
     * 
     * @return the all codes
     */
    public List<CodeToReturn> getAllCodes();
    
    /**
     * Removes the.
     * 
     * @param code the code
     */
    public void remove(CodeToReturn code);
    
    /**
     * Gets the number of codes.
     * 
     * @return the number of codes
     */
    public int getNumberOfCodes();
    
    public CodeHolder clone() throws CloneNotSupportedException;
}