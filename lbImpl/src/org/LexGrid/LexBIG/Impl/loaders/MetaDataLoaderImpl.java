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
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.MetaData_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.jdom.input.SAXBuilder;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;
import edu.mayo.informatics.lexgrid.convert.options.StringOption;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OBO files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MetaDataLoaderImpl extends BaseLoader implements MetaData_Loader {
    private static final long serialVersionUID = -205479865592766865L;
    public final static String name = "MetaDataLoader";
    private final static String description = "This loader loads metadata xml files into the system.";
    
    private static String URI_OPTION = "URI";
    private static String VERSION_OPTION = "Version";
    private static String OVERWRITE_OPTION = "Overwrite Existing";
    

    public MetaDataLoaderImpl() {
       super();
       this.setDoComputeTransitiveClosure(false);
       this.setDoIndexing(false);
       this.setDoRegister(false);
       this.getOptions().getStringArrayOption(LOADER_POST_PROCESSOR_OPTION).getOptionValue().clear();
    }

    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(MetaDataLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(MetaDataLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    public void validateAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBParameterException {
        try {
            setInUse();

            if (codingSchemeVersion == null || codingSchemeVersion.getCodingSchemeURN() == null
                    || codingSchemeVersion.getCodingSchemeURN().length() == 0
                    || codingSchemeVersion.getCodingSchemeVersion() == null
                    || codingSchemeVersion.getCodingSchemeVersion().length() == 0) {
                throw new LBParameterException("The coding scheme URN and version must be supplied.");
            }

            try {
                Reader temp;
                if (source.getScheme().equals("file")) {
                    temp = new FileReader(new File(source));
                } else {
                    temp = new InputStreamReader(source.toURL().openConnection().getInputStream());
                }
                if (validationLevel == 0) {
                    SAXBuilder saxBuilder = new SAXBuilder();
                    saxBuilder.build(new BufferedReader(temp));
                }
            }

            catch (Exception e) {
                throw new ConnectionFailure("The meta source file '" + source + "' cannot be read or is invalid.", e);
            }

        } catch (ConnectionFailure e) {
            throw new LBParameterException("The metadata file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
            "Each loader can only do one thing at a time.  Please create a new loader to do multiple loads at once.");
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new LBParameterException(e.getMessage());
        } finally {
            inUse = false;
        }

    }

    public void loadAuxiliaryData(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {
        this.getOptions().getStringOption(URI_OPTION).setOptionValue(codingSchemeVersion.getCodingSchemeURN());
        this.getOptions().getStringOption(VERSION_OPTION).setOptionValue(codingSchemeVersion.getCodingSchemeVersion());
        this.getOptions().getBooleanOption(OVERWRITE_OPTION).setOptionValue(overwrite);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        
        this.load(source);
    }

    public void loadAuxiliaryData(Map<Object, Object> source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            boolean overwrite, boolean stopOnErrors, boolean async) throws LBException {
        throw new UnsupportedOperationException();
    }

    public void loadLexGridManifest(CodingSchemeManifest source,
            AbsoluteCodingSchemeVersionReference codingSchemeURNVersion, boolean stopOnErrors, boolean async)
    throws LBException {
       //
    }

    public void loadLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeURNVersion,
            boolean stopOnErrors, boolean async) throws LBException {
        //
    }

    public void validateLexGridManifest(URI source, AbsoluteCodingSchemeVersionReference codingSchemeVersion,
            int validationLevel) throws LBException {
        // TODO Auto-generated method stub
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.getStringOptions().add(new StringOption(URI_OPTION));
        holder.getStringOptions().add(new StringOption(VERSION_OPTION));
        
        holder.getBooleanOptions().add(new BooleanOption(OVERWRITE_OPTION, false));
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        try {
            LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
            getMetadataIndexService().indexMetadata(
                    this.getOptions().getStringOption(URI_OPTION).getOptionValue(), 
                    this.getOptions().getStringOption(VERSION_OPTION).getOptionValue(), 
                    this.getResourceUri(), 
                    !this.getOptions().getBooleanOption(OVERWRITE_OPTION).getOptionValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return new URNVersionPair[] {new URNVersionPair(
                    this.getOptions().getStringOption(URI_OPTION).getOptionValue(),
                    this.getOptions().getStringOption(VERSION_OPTION).getOptionValue()
        )};
    }
}