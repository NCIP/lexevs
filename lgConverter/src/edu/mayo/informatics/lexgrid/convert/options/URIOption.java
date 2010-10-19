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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class URIOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class URIOption extends AbstractOption<URI> implements org.LexGrid.LexBIG.Extensions.Load.options.URIOption {

    /** The allowed file extensions. */
    private List<String> allowedFileExtensions = new ArrayList<String>();
    
    /** The is folder. */
    private boolean isFolder;
    
    /**
     * Instantiates a new uRI option.
     * 
     * @param optionName the option name
     */
    public URIOption(String optionName) {
        super(optionName);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#getAllowedFileExtensions()
     */
    public List<String> getAllowedFileExtensions() {
        return allowedFileExtensions;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#addAllowedFileExtensions(java.lang.String)
     */
    public void addAllowedFileExtensions(String allowedFileExtensions) {
        this.getAllowedFileExtensions().add(allowedFileExtensions);
    }

    /**
     * Sets the allowed file extensions.
     * 
     * @param allowedFileExtensions the new allowed file extensions
     */
    public void setAllowedFileExtensions(List<String> allowedFileExtensions) {
        this.allowedFileExtensions = allowedFileExtensions;
    }

    /**
     * Sets the folder.
     * 
     * @param isFolder the new folder
     */
    public void setFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.options.URIOption#isFolder()
     */
    public boolean isFolder() {
        return isFolder;
    }
}