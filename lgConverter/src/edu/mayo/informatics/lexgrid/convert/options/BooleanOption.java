
package edu.mayo.informatics.lexgrid.convert.options;

/**
 * The Class BooleanOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BooleanOption extends AbstractOption<Boolean>{
 
    /**
     * Instantiates a new boolean option.
     * 
     * @param optionName the option name
     */
    public BooleanOption(String optionName) {
        super(optionName);
    }

    /**
     * Instantiates a new boolean option.
     * 
     * @param optionName the option name
     * @param defaultValue the default value
     */
    public BooleanOption(String optionName, Boolean defaultValue) {
        super(optionName, defaultValue);
    }

}