
package edu.mayo.informatics.lexgrid.convert.options;

/**
 * The Class IntegerOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IntegerOption extends AbstractOption<Integer>{

    /**
     * Instantiates a new integer option.
     * 
     * @param optionName the option name
     */
    public IntegerOption(String optionName) {
        super(optionName);
    }
    
    public IntegerOption(String optionName, int defaultValue) {
        super(optionName, defaultValue);
    }

}