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
package org.LexGrid.LexBIG.Impl.loaders;

import java.net.URI;
import java.net.URISyntaxException;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.directConversions.StreamingXMLToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon.LexGridXMLProcessor;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class LexGridMultiLoaderImpl extends BaseLoader implements LexGrid_Loader {

    private static final long serialVersionUID = 5405545553067402760L;
    public final static String name = "LexGrid_Loader";
    private final static String description = "This loader loads LexGrid XML files into the LexGrid database.";
    
    public final static String VALIDATE = "Validate";
    private static boolean validate = true;
    
    public LexGridMultiLoaderImpl() {
       super();
    }
  
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#validate(java.net.URI, int)
     */
    public void validate(URI uri, int validationLevel) throws LBParameterException {
        throw new UnsupportedOperationException();
    }
    
    public void load(URI source, boolean stopOnErrors, boolean async) throws LBException {
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(source);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#declareAllowedOptions(org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder)
     */
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        BooleanOption forceValidation = new BooleanOption(LexGridMultiLoaderImpl.VALIDATE, validate);
        holder.getBooleanOptions().add(forceValidation);
        return holder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
     */
    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        StreamingXMLToSQL loader = new StreamingXMLToSQL();
        
        Object[] loadedObject = loader.load(
                this.getResourceUri(),
                this.getMessageDirector(),
                this.getOptions().getBooleanOption(LexGridMultiLoaderImpl.VALIDATE).getOptionValue(),
                this.getCodingSchemeManifest());
        
        if(loadedObject == null || loadedObject.length == 0) {
            return null;
        }
   
        URNVersionPair[] loadedCodingSchemes = this.constructVersionPairsFromCodingSchemes(loadedObject);
        
        if (loadedObject[0] instanceof ValueSetDefinition || loadedObject[0] instanceof PickListDefinition)
        {
            setDoIndexing(false);
            setDoApplyPostLoadManifest(false);
            setDoComputeTransitiveClosure(false);
            setDoRegister(false);
            this.getOptions().getStringArrayOption(LOADER_POST_PROCESSOR_OPTION).setOptionValue(null);
        }
        
        if(loadedCodingSchemes[0].getUrn().equals(LexGridXMLProcessor.NO_SCHEME_URL)
                &&  loadedCodingSchemes[0].getVersion().equals(LexGridXMLProcessor.NO_SCHEME_VERSION)) {
            return null;
        } else {
            return loadedCodingSchemes;
        }
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridMultiLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridMultiLoaderImpl.class.getName());
        temp.setDescription(LexGridMultiLoaderImpl.description);
        temp.setName(LexGridMultiLoaderImpl.name);
        
        return temp;
    }
    
   
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#getSchemaURL()
     */
    public URI getSchemaURL() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#getSchemaVersion()
     */
    public String getSchemaVersion() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing LexGridMultiLoaderImpl");
        super.finalize();
    }
    /**
     * @param args
     */
    public static void main(String[] args){
        LexGridMultiLoaderImpl loader = new LexGridMultiLoaderImpl();
        loader.addBooleanOptionValue(LexGridMultiLoaderImpl.VALIDATE, validate);
        URI uri = null;
        try {
            uri = new URI(args[0]);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loader.load(uri);
    }
}