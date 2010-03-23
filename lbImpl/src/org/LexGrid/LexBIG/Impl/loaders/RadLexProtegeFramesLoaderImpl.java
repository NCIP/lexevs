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

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.RadlexProtegeFrames_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.emfConversions.radlex.RadLex2EMFMain;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.ProtegeFrames;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Validates and/or loads RadLex content, provided by the
 * Radiological Society of North America in Protégé frame format.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class RadLexProtegeFramesLoaderImpl extends BaseLoader implements RadlexProtegeFrames_Loader {
    private static final long serialVersionUID = 6279347193782597340L;
    public final static String name = "RadLexFramesLoader";
    private final static String description = "This loader loads the RadLex Protégé frame representation into the LexGrid format.";

    public RadLexProtegeFramesLoaderImpl() {
        super();
    }

    public void validate(URI uri, int validationLevel) throws LBParameterException {
        try {
            setInUse();
            ProtegeFrames pFrames = new ProtegeFrames(uri);
            pFrames.setManifestLocation(this.getCodingSchemeManifestURI());
            pFrames.testConnection();

        } catch (ConnectionFailure e) {
            throw new LBParameterException("The Protégé file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
                    "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void load(URI uri, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {
       //
    }

    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing RadLexProtegeFramesLoaderImpl");
        super.finalize();
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
         return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        RadLex2EMFMain radlexLoader = new RadLex2EMFMain();
        CodingScheme codingScheme = radlexLoader.map(this.getResourceUri(), this.getMessageDirector());
        
        super.persistCodingSchemeToDatabase(codingScheme);
        
        return super.constructVersionPairsFromCodingSchemes(codingScheme);
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(RadLexProtegeFramesLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(RadLexProtegeFramesLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        
        return temp;
    }
}