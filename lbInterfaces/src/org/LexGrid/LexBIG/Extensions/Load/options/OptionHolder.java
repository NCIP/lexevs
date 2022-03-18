
package org.LexGrid.LexBIG.Extensions.Load.options;

import java.util.List;

/**
 * The Interface OptionHolder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface OptionHolder {
	
	/**
	 * Gets the resource uri allowed file types.
	 * 
	 * @return the resource uri allowed file types
	 */
	public List<String> getResourceUriAllowedFileTypes();
	
	/**
	 * Checks if is resource uri folder.
	 * 
	 * @return true, if is resource uri folder
	 */
	public boolean isResourceUriFolder();
	
	/**
	 * Sets the checks if is resource uri folder.
	 * 
	 * @param isResourceUriFolder the new checks if is resource uri folder
	 */
	public void setIsResourceUriFolder(boolean isResourceUriFolder);
	
    /**
     * Gets the boolean option.
     * 
     * @param optionName the option name
     * 
     * @return the boolean option
     */
    public Option<Boolean> getBooleanOption(String optionName);

    /**
     * Gets the string option.
     * 
     * @param optionName the option name
     * 
     * @return the string option
     */
    public Option<String> getStringOption(String optionName);
    
    public MultiValueOption<String> getStringArrayOption(String optionName);

    /**
     * Gets the integer option.
     * 
     * @param optionName the option name
     * 
     * @return the integer option
     */
    public Option<Integer> getIntegerOption(String optionName);
    
    /**
     * Gets the uRI option.
     * 
     * @param optionName the option name
     * 
     * @return the uRI option
     */
    public URIOption getURIOption(String optionName);
    
    /**
     * Gets the generic option.
     * 
     * @param optionName the option name
     * @param optionClass the option class
     * 
     * @return the generic option
     */
    public <T> Option<T> getGenericOption(String optionName, Class<T> optionClass);

    /**
     * Gets the boolean options.
     * 
     * @return the boolean options
     */
    public List<Option<Boolean>> getBooleanOptions();

    /**
     * Gets the integer options.
     * 
     * @return the integer options
     */
    public List<Option<Integer>> getIntegerOptions();

    /**
     * Gets the string options.
     * 
     * @return the string options
     */
    public List<Option<String>> getStringOptions();
    
    /**
     * Gets the string array options.
     * 
     * @return the string array options
     */
    public List<MultiValueOption<String>> getStringArrayOptions();
    
    /**
     * Gets the uRI options.
     * 
     * @return the uRI options
     */
    public List<URIOption> getURIOptions();
    
    /**
     * Gets the generic option.
     * 
     * @return the generic option
     */
    public List<Option<?>> getGenericOptions();
}