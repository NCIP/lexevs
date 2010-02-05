/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.Extensions;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Extensions.Extendable;

/**
 * The Class AbstractExtendable.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractExtendable implements Extendable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4053427150129368803L;
    
    /** The extension description. */
    private ExtensionDescription extensionDescription;
    
    /**
     * Instantiates a new abstract extendable.
     */
    protected AbstractExtendable(){
        this.setExtensionDescription(
                this.buildExtensionDescription());
    }

    /**
     * Sets the extension description.
     * 
     * @param extensionDescription the new extension description
     */
    private void setExtensionDescription(ExtensionDescription extensionDescription){
        this.extensionDescription = buildExtensionDescription();
    }

    /**
     * Gets the extension description.
     * 
     * @return the extension description
     */
    public ExtensionDescription getExtensionDescription() {
        return extensionDescription;
    }

    /**
     * Builds the extension description.
     * 
     * @return the extension description
     */
    protected abstract ExtensionDescription buildExtensionDescription();
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getDescription()
     */
    public String getDescription() {
        return extensionDescription.getDescription();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getName()
     */
    public String getName() {
       return extensionDescription.getName();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getProvider()
     */
    public String getProvider() {
        return extensionDescription.getExtensionProvider().getContent();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Extendable#getVersion()
     */
    public String getVersion() {
        return extensionDescription.getVersion();
    } 
}
