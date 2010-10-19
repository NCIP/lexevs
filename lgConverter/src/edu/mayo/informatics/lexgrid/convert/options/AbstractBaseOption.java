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

import org.springframework.util.Assert;

/**
 * The Class AbstractOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseOption<T> {

    /** The option value. */
    private T optionValue;
    
    /** The option name. */
    private String optionName;
    
    private String helpText;

    /**
     * Instantiates a new abstract option.
     * 
     * @param optionName the option name
     */
    public AbstractBaseOption(String optionName){
        this.optionName = optionName;
    }
    
    /**
     * Instantiates a new abstract option.
     * 
     * @param optionName the option name
     * @param defaultValue the default value
     */
    public AbstractBaseOption(String optionName, T defaultValue){
        this(optionName, defaultValue, null);
    }
    
    public AbstractBaseOption(String optionName, T defaultValue, String helpText){
        this.optionName = optionName;
        optionValue = defaultValue;
        this.helpText = helpText;
    }
 
    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.Option#getOptionName()
     */
    public String getOptionName(){
            return optionName;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.Option#getOptionValue()
     */
    public T getOptionValue() {
        Assert.notNull("Option " + optionName + " has not been set and has no default value");
        return optionValue;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.Option#setOptionValue(java.lang.Object)
     */
    public void setOptionValue(T optionValue) {
        this.optionValue = optionValue;
    }   
}