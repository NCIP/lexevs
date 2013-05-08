/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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