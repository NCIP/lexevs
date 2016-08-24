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

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.MrMap_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.informatics.lexgrid.convert.directConversions.MrmapToSQL;
import edu.mayo.informatics.lexgrid.convert.options.URIOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Loader class for MrMap and MrSat RRF files resulting in a 
 * mapping coding scheme or schemes depending on the content of these files.
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class MrmapRRFLoader extends BaseLoader implements MrMap_Loader{
    
    private static final long serialVersionUID = -689653003698478622L;
    

    public final static String VALIDATE = "Validate";
      public final static String MRSAT_URI = "MRSAT file path";
      public final static String MANIFEST_URI = "add'l Manifest";
      
    @SuppressWarnings("unused")
    private static boolean validate = true;

    public MrmapRRFLoader(){
        
        super();
    }


    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
    URIOption mrSatURI  = new URIOption(MRSAT_URI);
    holder.getURIOptions().add(mrSatURI);
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
        CachingMessageDirectorIF messages = getMessageDirector();
      MrmapToSQL map = new MrmapToSQL();
    if(getCodingSchemeManifest() != null){
        messages.warn("Pre-load of manifests is not supported in the MrMap Loader.  " +
        		"Manifest files can be post loaded instead");
    }
    if(getLoaderPreferences() != null){
        messages.warn("Loader Preferences are not supported in the MrMap Loader");
    }
     CodingScheme[] schemes = map.load(getMessageDirector(), 
              this.getResourceUri(), 
              this.getOptions().getURIOption(MRSAT_URI).getOptionValue(),
              null, null, null, null, null, null, null, null, null,
              this.getCodingSchemeManifest());
     setDoApplyPostLoadManifest(false);
     return this.constructVersionPairsFromCodingSchemes((Object[])schemes);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MrmapRRFLoader.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MrmapRRFLoader.class.getName());
        temp.setDescription(MrmapRRFLoader.description);
        temp.setName(MrmapRRFLoader.name);
        
        return temp;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public void load(URI mrMapsource, URI mrSatSource, String nameForMappingScheme, String nameForMappingVersion,
            String nameforMappingURI, boolean stopOnErrors, boolean async) throws LBException{
        this.load(mrMapsource, mrSatSource, nameForMappingScheme, 
                nameForMappingVersion, nameforMappingURI, null, null, 
                null, null, null, null, stopOnErrors, async);
    }
    @Override
    public void load(URI mrMapsource, URI mrSatSource, String nameForMappingScheme, String nameForMappingVersion,
            String nameforMappingURI, String sourceScheme, String sourceVersion, String sourceURI, String targetScheme,
            String targetVersion, String targetURI, boolean stopOnErrors, boolean async) throws LBException{
        this.getOptions().getURIOption(MRSAT_URI).setOptionValue(mrSatSource);

        this.load(mrMapsource);
    }

    @Override
    public void validate(String source, int validationLevel) throws LBException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.MRMAP;
    }

}