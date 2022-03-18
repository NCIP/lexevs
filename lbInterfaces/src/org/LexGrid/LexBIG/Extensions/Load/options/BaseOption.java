
package org.LexGrid.LexBIG.Extensions.Load.options;

/**
 * The Interface BaseOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface BaseOption<T> {
	 
 	/**
 	 * Gets the option value.
 	 * 
 	 * @return the option value
 	 */
    public T getOptionValue();
    
    /**
     * Sets the option value.
     * 
     * @param optionValue the new option value
     */
    public void setOptionValue(T optionValue);
    
    /**
     * Gets the option name.
     * 
     * @return the option name
     */
    public String getOptionName();
    
    /**
     * Gets the help text.
     * 
     * @return the help text
     */
    public String getHelpText();
}