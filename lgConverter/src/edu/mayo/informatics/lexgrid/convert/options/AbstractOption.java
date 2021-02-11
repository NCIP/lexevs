
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