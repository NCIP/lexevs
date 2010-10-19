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
package edu.mayo.informatics.lexgrid.convert.options;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Extensions.Load.options.Option;

/**
 * The Class AbstractOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractOption<T> extends AbstractBaseOption<T> implements Option<T>{

    private boolean multipleSelectionsAllowed = false;
    
    private List<T> pickList = new ArrayList<T>();
    
    /**
     * Instantiates a new abstract option.
     * 
     * @param optionName the option name
     */
    public AbstractOption(String optionName){
       super(optionName);
    }
    
    /**
     * Instantiates a new abstract option.
     * 
     * @param optionName the option name
     * @param defaultValue the default value
     */
    public AbstractOption(String optionName, T defaultValue){
        super(optionName, defaultValue);
    }

    
    public void setMultipleSelectionsAllowed(boolean multipleSelectionsAllowed) {
        this.multipleSelectionsAllowed = multipleSelectionsAllowed;
    }
    public boolean isMultipleSelectionsAllowed() {
        return multipleSelectionsAllowed;
    }

    public void setPickList(List<T> pickList) {
        this.pickList = pickList;
    }

    public List<T> getPickList() {
        return pickList;
    }
}