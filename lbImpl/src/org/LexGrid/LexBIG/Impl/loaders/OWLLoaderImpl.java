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
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.OWL_Loader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Preferences.loader.OWLLoadPreferences.OWLLoaderPreferences;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.NCIOwl;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.Owl;

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

        super.name_ = OWLLoaderImpl.name;
        super.description_ = OWLLoaderImpl.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(OWLLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(OWLLoaderImpl.class.getName());
        temp.setDescription(description);
        temp.setName(name);
        temp.setVersion(version_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerLoadExtension(temp);
    }

    public void validate(URI uri, URI manifest, int validationLevel) throws LBParameterException {
        try {
            setInUse();
            try {
                in_ = new Owl(uri, manifest);
                in_.testConnection();
            } catch (ConnectionFailure e) {
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
            in_ = nciowl;
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The NCIOwl file path appears to be invalid - " + e);
        }

        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));
        options_.add(new Option(Option.MEMORY_SAFE, new Boolean(memorySafe)));

        status_ = new LoadStatus();
        status_.setLoadSource(getStringFromURI(uri));
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
            in_ = owl;
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The Owl file path appears to be invalid - " + e);
        }

        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));
        options_.add(new Option(Option.MEMORY_SAFE, new Integer(memorySafe)));

        status_ = new LoadStatus();
        status_.setLoadSource(getStringFromURI(source));
        baseLoad(async);
    }

    public void validateNCI(URI source, URI manifest, int validationLevel) throws LBException {
        try {
            setInUse();
            try {
                in_ = new NCIOwl(getStringFromURI(source), manifest);
                in_.testConnection();
            } catch (ConnectionFailure e) {
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