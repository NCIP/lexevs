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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OBO_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.codingSchemes.CodingScheme;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2.OBO2EMFMain;
import edu.mayo.informatics.lexgrid.convert.emfConversions.obo1_2.OBOFormatValidator;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOLoaderImpl extends BaseLoader implements OBO_Loader {
    private static final long serialVersionUID = 8418561158634673381L;
    public final static String name = "OBOLoader";
    private final static String description = "This loader loads version 1.2 OBO files into the LexGrid format.";

    public OBOLoaderImpl() {
        super();
    }

    public String getOBOVersion() {
        return "1.2";
    }

    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OBOLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OBOLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion( getOBOVersion() );

        return temp;
    }

    public void validate(URI uri, URI metaSource, int validationLevel) throws LBParameterException {
        try {
            setInUse();

            if (validationLevel == 0) {
                if (!OBOFormatValidator.isValidDocumentHeader(uri)) {
                    throw new LBParameterException("The OBO file header was  malformed while validating  "+ uri);
                }
                if (!OBOFormatValidator.isValidDocumentContent(uri)) {
                    throw new LBParameterException("The OBO file content was malformed while validating "+ uri);
                }

            }
            if (metaSource != null) {
                try {
                    Reader temp;
                    if (metaSource.getScheme().equals("file")) {
                        temp = new FileReader(new File(metaSource));
                    } else {
                        temp = new InputStreamReader(metaSource.toURL().openConnection().getInputStream());
                    }
                    if (validationLevel == 0) {
                        SAXBuilder saxBuilder = new SAXBuilder();
                        saxBuilder.build(new BufferedReader(temp));
                    }
                }

                catch (Exception e) {
                    throw new ConnectionFailure("The meta source file '" + metaSource
                            + "' cannot be read or is invalid");
                }
            }
        } catch (ConnectionFailure e) {
            throw new LBParameterException("The OBO file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
                    "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void load(URI uri, URI metaSource, boolean stopOnErrors, boolean async) throws LBParameterException,
            LBInvocationException {
       //
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws Exception {
        // TODO Auto-generated method stub (IMPLEMENT!)
        DatabaseServiceManager databaseServiceManager = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
        
        
        OBO2EMFMain mainTxfm = new OBO2EMFMain();
        CodingScheme codingScheme = mainTxfm.map(this.getResourceUri(), null, this.getMessageDirector());
        // Apply manifest changes
        ManifestUtil manifestUtil = new ManifestUtil(null, this.getMessageDirector());
        //manifestUtil.applyManifest(this.getCodingSchemeManifest(), scheme);
  
        databaseServiceManager.getCodingSchemeService().insertCodingScheme(codingScheme);
        URNVersionPair  urnVersion= new URNVersionPair(codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion());
        return new URNVersionPair[]{urnVersion};
    }

    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing OBOLoaderImpl");
        super.finalize();
    }

}