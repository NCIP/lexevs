
package edu.mayo.informatics.lexgrid.convert.options;

import java.util.ArrayList;

/**
 * The Class StringOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class StringArrayOption extends AbstractMultiValueOption<String>{

    /**
     * Instantiates a new string option.
     * 
     * @param optionName the option name
     */
    public StringArrayOption(String optionName) {
        super(optionName);
        this.setOptionValue(new ArrayList<String>());
    }
}