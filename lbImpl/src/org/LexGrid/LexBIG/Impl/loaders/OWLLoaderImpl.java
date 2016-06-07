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

import java.io.File;
import java.net.URI;

import javax.xml.parsers.SAXParserFactory;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.OWLLoadPreferences.OWLLoaderPreferences;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl.ProtegeOwl2LGConstants;
import edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl.ProtegeOwl2LGMain;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.options.IntegerOption;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OWL files into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OWLLoaderImpl extends BaseLoader implements OWL_Loader {
    private static final long serialVersionUID = -6601893851902189345L;

    public OWLLoaderImpl() {
        super();
        this.setDoComputeTransitiveClosure(true);
    }

    protected ExtensionDescription buildExtensionDescription(){
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OWLLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OWLLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);

        return temp;
    }

    public void validate(URI uri, URI manifest, int validationLevel) throws LBParameterException {
        try {
            setInUse();
            try {
                //in_ = new Owl(uri, manifest);
                //in_.testConnection();
            } catch (Exception e) {
                throw new LBParameterException("The OWL file path appears to be invalid - " + e);
            }
            // Verify content ...

            if (validationLevel == 0) {
                SAXParserFactory.newInstance().newSAXParser().parse(new File(uri), new DefaultHandler());
            } else if (validationLevel == 1) {
                // TODO add support back for owl validation
                throw new IllegalArgumentException("Unsupported validation level");
            } else
                throw new IllegalArgumentException("Unsupported validation level");
        } catch (Exception e) {
            throw new LBParameterException(e.toString());
        } finally {
            inUse = false;
        }
    }

    public void load(URI source, URI codingSchemeManifestURI, int memorySafe, boolean stopOnErrors, boolean async)
            throws LBException {
        if(codingSchemeManifestURI != null) {
            this.setCodingSchemeManifestURI(codingSchemeManifestURI);
        }
        this.getOptions().getIntegerOption(Option.getNameForType(Option.MEMORY_SAFE)).setOptionValue(memorySafe);
        this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).setOptionValue(stopOnErrors);
        this.getOptions().getBooleanOption(ASYNC_OPTION).setOptionValue(async);
        
        this.load(source);  
    }

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        IntegerOption memSafeOption = new IntegerOption(Option.getNameForType(Option.MEMORY_SAFE), 
                ProtegeOwl2LGConstants.MEMOPT_LEXGRID_DIRECT_DB);
        
        memSafeOption.setHelpText(Option.getDescriptionForType(Option.MEMORY_SAFE));
        holder.getIntegerOptions().add(memSafeOption);
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        try {
            ProtegeOwl2LGMain owlLoader = new ProtegeOwl2LGMain(
                    this.getResourceUri(), 
                    this.getCodingSchemeManifest(), 
                    this.getLoaderPreferences(), 
                    this.getOptions().getBooleanOption(FAIL_ON_ERROR_OPTION).getOptionValue(),
                    this.getOptions().getIntegerOption(Option.getNameForType(Option.MEMORY_SAFE)).getOptionValue(), 
                    this.getMessageDirector()
            );

            CodingScheme owlScheme = owlLoader.map();

            if(this.getOptions().getIntegerOption(Option.getNameForType(Option.MEMORY_SAFE)).getOptionValue().equals(
                    ProtegeOwl2LGConstants.MEMOPT_ALL_IN_MEMORY)) {
                
                this.persistCodingSchemeToDatabase(owlScheme);
            } 
            
            return this.constructVersionPairsFromCodingSchemes(owlScheme);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing OWLLoaderImpl");
        super.finalize();
    }

    public void setLoaderPreferences(LoaderPreferences prefs) throws LBParameterException {
        if (prefs instanceof OWLLoaderPreferences) {
            super.setLoaderPreferences(prefs);
        } else {
            throw new LBParameterException("Loader Preferences must be of type 'OWLLoaderPreferences'");
        }
    }
    
    @Override
    public OntologyFormat getOntologyFormat() {
        return OntologyFormat.OWLRDF;
    }

}