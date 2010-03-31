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

import javax.xml.parsers.SAXParserFactory;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.OWLLoadPreferences.OWLLoaderPreferences;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.locator.LexEvsServiceLocator;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl.ProtegeOwl2LGConstants;
import edu.mayo.informatics.lexgrid.convert.directConversions.protegeOwl.ProtegeOwl2LGMain;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIOwl;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.Owl;
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
    public final static String name = "OWLLoader";
    private final static String description = "This loader loads 'OWL Full' files into the LexGrid format.";

    public OWLLoaderImpl() {
        super();
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

    public void loadNCI(URI uri, URI manifest, boolean memorySafe, boolean stopOnErrors, boolean async)
            throws LBParameterException, LBInvocationException {
        setInUse();
        try {
            NCIOwl nciowl = new NCIOwl(getStringFromURI(uri), manifest);
            nciowl.setCodingSchemeManifest(this.getCodingSchemeManifest());
            nciowl.setLoaderPreferences(this.getLoaderPreferences());
        } catch (Exception e) {
            inUse = false;
            throw new LBParameterException("The NCIOwl file path appears to be invalid - " + e);
        }

        getStatus().setLoadSource(getStringFromURI(uri));
        baseLoad(async);
    }

    public void loadNCIThes(URI uri, URI manifest, boolean memorySafe, boolean stopOnErrors, boolean async)
            throws LBParameterException, LBInvocationException {
        loadNCI(uri, manifest, memorySafe, stopOnErrors, async);
    }

    public void load(URI source, URI codingSchemeManifestURI, int memorySafe, boolean stopOnErrors, boolean async)
            throws LBException {
        setInUse();
        try {
            Owl owl = new Owl(source, codingSchemeManifestURI);
            // Only if the manifest uri is provided, ie the uri is not null, then setCodingSchemeManifestURI.
            if (codingSchemeManifestURI != null ) {
                setCodingSchemeManifestURI(codingSchemeManifestURI);
            }
            owl.setCodingSchemeManifest(this.getCodingSchemeManifest());
            owl.setLoaderPreferences(this.getLoaderPreferences());
        } catch (Exception e) {
            inUse = false;
            throw new LBParameterException("The Owl file path appears to be invalid - " + e);
        }

        getStatus().setLoadSource(getStringFromURI(source));
        baseLoad(async);
    }

    public void validateNCI(URI source, URI manifest, int validationLevel) throws LBException {
        try {
            setInUse();
            try {
                //in_ = new NCIOwl(getStringFromURI(source), manifest);
               //in_.testConnection();
            } catch (Exception e) {
                throw new LBParameterException("The NCI OWL file path appears to be invalid - " + e);
            }
            // Verify content ...

            if (validationLevel == 0) {
                SAXParserFactory.newInstance().newSAXParser().parse(new File(source), new DefaultHandler());
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

    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        holder.getIntegerOptions().add(new IntegerOption(Option.getNameForType(Option.MEMORY_SAFE), 
                ProtegeOwl2LGConstants.MEMOPT_ALL_IN_MEMORY));
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
            
            for(Relations rel : owlScheme.getRelations()) {
                for(AssociationPredicate pred : rel.getAssociationPredicate()) {
                    for(AssociationSource source : pred.getSource()) {
                        if(source.getSourceEntityCodeNamespace() == null) {
                            System.out.println("SOURCE: " + source.getSourceEntityCode());
                        }
                        for(AssociationTarget target : source.getTarget()) {
                            if(target.getTargetEntityCodeNamespace() == null) {
                                System.out.println("Target: " + target.getTargetEntityCode());
                            }
                        }
                    }
                }
            }
            
            this.persistCodingSchemeToDatabase(owlScheme);
            
            return this.constructVersionPairsFromCodingSchemes(owlScheme);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validateNCIThes(URI source, URI manifest, int validationLevel) throws LBException {
        validateNCI(source, manifest, validationLevel);
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

}