/*
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
        this.optionName = optionName;
        optionValue = defaultValue;
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
