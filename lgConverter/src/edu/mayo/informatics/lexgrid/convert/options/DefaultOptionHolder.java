
package edu.mayo.informatics.lexgrid.convert.options;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.Extensions.Load.options.MultiValueOption;
import org.LexGrid.LexBIG.Extensions.Load.options.Option;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Extensions.Load.options.URIOption;

/**
 * The Class DefaultOptionHolder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultOptionHolder implements OptionHolder {

    /** The boolean options. */
    private List<Option<Boolean>> booleanOptions = new ArrayList<Option<Boolean>>();
    
    /** The integer options. */
    private List<Option<Integer>> integerOptions = new ArrayList<Option<Integer>>();
    
    /** The string options. */
    private List<Option<String>> stringOptions = new ArrayList<Option<String>>();
    
    /** The string options. */
    private List<MultiValueOption<String>> stringArrayOptions = new ArrayList<MultiValueOption<String>>();
    
    /** The uri options. */
    private List<URIOption> uriOptions = new ArrayList<URIOption>();
    
    /** The uri options. */
    private List<Option<?>> genericOptions = new ArrayList<Option<?>>();
    
    /** The resource uri allowed file types. */
    private List<String> resourceUriAllowedFileTypes = new ArrayList<String>();
    { resourceUriAllowedFileTypes.add("*"); }
    
    /** The is resource uri folder. */
    private boolean isResourceUriFolder = false;
    
    public MultiValueOption<String> getStringArrayOption(String optionName){
        return this.findMultiValueOption(optionName, this.stringArrayOptions); 
     }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getBooleanOption(java.lang.String)
     */
    public Option<Boolean> getBooleanOption(String optionName){
       return this.findOption(optionName, this.booleanOptions); 
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getStringOption(java.lang.String)
     */
    public Option<String> getStringOption(String optionName){
        return this.findOption(optionName, this.stringOptions); 
     }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getIntegerOption(java.lang.String)
     */
    public Option<Integer> getIntegerOption(String optionName){
        return this.findOption(optionName, this.integerOptions); 
     }
    

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#getURIOption(java.lang.String)
     */
    public URIOption getURIOption(String optionName) {
        return findTypedOption(optionName, uriOptions);
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#getGenericOption(java.lang.String, java.lang.Class)
     */
    public <T> Option<T> getGenericOption(String optionName, Class<T> optionClass) {
        return findOption(optionName, optionClass, genericOptions);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#findOption(java.lang.String, java.util.List)
     */
    public <T> Option<T> findOption(String optionName, List<Option<T>> optionList){
        for(Option<T> option : optionList){
            if(option.getOptionName().equals(optionName)){
                return option;
            }
        }
        throw new RuntimeException("Option:" + optionName + " not found.");
    }

    @SuppressWarnings("unchecked")
    public <T> Option<T> findOption(String optionName, Class<T> optionClass, List<Option<?>> optionList){
        for(Option<?> option : optionList){
            if(option.getOptionName().equals(optionName)){
                try {
                    return (Option<T>)option;
                } catch (ClassCastException e) {
                    throw new RuntimeException("Option:" + optionName + " is not of type: " + optionClass.getName() + ".");
                }
            }
        }
        throw new RuntimeException("Option:" + optionName + " not found.");
    }
    
    public <T> MultiValueOption<T> findMultiValueOption(String optionName, List<MultiValueOption<T>> optionList){
        for(MultiValueOption<T> option : optionList){
            if(option.getOptionName().equals(optionName)){
                return option;
            }
        }
        throw new RuntimeException("Option:" + optionName + " not found.");
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#findOption(java.lang.String, java.util.List)
     */
    /**
     * Find typed option.
     * 
     * @param optionName the option name
     * @param optionList the option list
     * 
     * @return the t
     */
    public <T extends Option<?>> T findTypedOption(String optionName, List<T> optionList){
        for(T option : optionList){
            if(option.getOptionName().equals(optionName)){
                return option;
            }
        }
        throw new RuntimeException("Option:" + optionName + " not found.");
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getBooleanOptions()
     */
    public List<Option<Boolean>> getBooleanOptions() {
        return booleanOptions;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#setBooleanOptions(java.util.List)
     */
    /**
     * Sets the boolean options.
     * 
     * @param booleanOptions the new boolean options
     */
    public void setBooleanOptions(List<Option<Boolean>> booleanOptions) {
        this.booleanOptions = booleanOptions;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getIntegerOptions()
     */
    public List<Option<Integer>> getIntegerOptions() {
        return integerOptions;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#setIntegerOptions(java.util.List)
     */
    /**
     * Sets the integer options.
     * 
     * @param integerOptions the new integer options
     */
    public void setIntegerOptions(List<Option<Integer>> integerOptions) {
        this.integerOptions = integerOptions;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#getStringOptions()
     */
    public List<Option<String>> getStringOptions() {
        return stringOptions;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.options.OptionHolderI#setStringOptions(java.util.List)
     */
    /**
     * Sets the string options.
     * 
     * @param stringOptions the new string options
     */
    public void setStringOptions(List<Option<String>> stringOptions) {
        this.stringOptions = stringOptions;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#getURIOptions()
     */
    public List<URIOption> getURIOptions() {
        return uriOptions;
    }
    
    public List<MultiValueOption<String>> getStringArrayOptions() {
        return stringArrayOptions;
    }

    public void setStringArrayOptions(List<MultiValueOption<String>> stringArrayOptions) {
        this.stringArrayOptions = stringArrayOptions;
    }

    public List<URIOption> getUriOptions() {
        return uriOptions;
    }

    public void setUriOptions(List<URIOption> uriOptions) {
        this.uriOptions = uriOptions;
    }

    public List<Option<?>> getGenericOptions() {
        return genericOptions;
    }

    public void setGenericOptions(List<Option<?>> genericOptions) {
        this.genericOptions = genericOptions;
    }

    /**
     * Sets the resource uri allowed file types.
     * 
     * @param resourceUriAllowedFileTypes the new resource uri allowed file types
     */
    public void setResourceUriAllowedFileTypes(List<String> resourceUriAllowedFileTypes) {
        this.resourceUriAllowedFileTypes = resourceUriAllowedFileTypes;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#getResourceUriAllowedFileTypes()
     */
    public List<String> getResourceUriAllowedFileTypes() {
        return resourceUriAllowedFileTypes;
    }


    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#isResourceUriFolder()
     */
    public boolean isResourceUriFolder() {
        return isResourceUriFolder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder#setIsResourceUriFolder(boolean)
     */
    public void setIsResourceUriFolder(boolean isResourceUriFolder) {
        this.isResourceUriFolder = isResourceUriFolder;
    }
}