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
package org.LexGrid.LexBIG.Impl.loaders;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.directConversions.StreamingXMLToSQL;
import edu.mayo.informatics.lexgrid.convert.directConversions.TextToSQL;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OWL files into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */
public class LexGridLoaderImpl extends BaseLoader implements LexGrid_Loader {
    private static final long serialVersionUID = 5405545553067402760L;
    public final static String name = "LexGrid_Loader";
    private final static String description = "This loader loads LexGrid XML files into the LexGrid database.";
    
    public final static String FORCE_VALIDATION = "Do not Validate";
    private boolean validate = false;
    
    public LexGridLoaderImpl() {
       super();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#validate(java.net.URI, int)
     */
    public void validate(URI uri, int validationLevel) throws LBParameterException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader#load(java.net.URI, boolean, boolean)
     */
    public void load(URI uri, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {
      //
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#declareAllowedOptions(org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder)
     */
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        BooleanOption forceValidation = new BooleanOption(LexGridLoaderImpl.FORCE_VALIDATION, false);
        holder.getBooleanOptions().add(forceValidation);
        return holder;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.loaders.BaseLoader#doLoad()
     */
    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        StreamingXMLToSQL loader = new StreamingXMLToSQL();
        
        CodingScheme codingScheme = loader.load(
                this.getResourceUri(), 
                this.getLogger(), 
                this.getOptions().getBooleanOption(LexGridLoaderImpl.FORCE_VALIDATION).getOptionValue());
   
        this.getStatus().setState(ProcessState.COMPLETED);
        this.getStatus().setErrorsLogged(false);
        return this.constructVersionPairsFromCodingSchemes(codingScheme);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridLoaderImpl.class.getName());
        temp.setDescription(LexGridLoaderImpl.description);
        temp.setName(LexGridLoaderImpl.name);
        
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
        getLogger().loadLogDebug("Freeing LexGridLoaderImpl");
        super.finalize();
    }
    /**
     * @param args
     */
    public static void main(String[] args){
        LexGridLoaderImpl loader = new LexGridLoaderImpl();
        loader.addBooleanOptionValue(LexGridLoaderImpl.FORCE_VALIDATION, false);
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