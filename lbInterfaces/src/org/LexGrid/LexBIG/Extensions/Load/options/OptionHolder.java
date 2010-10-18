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
    
    public List<MultiValueOption<String>> getStringArrayOptions();
    
    /**
     * Gets the uRI options.
     * 
     * @return the uRI options
     */
    public List<URIOption> getURIOptions();

}