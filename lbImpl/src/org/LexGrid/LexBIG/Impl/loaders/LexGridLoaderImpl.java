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
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.LexGrid_Loader;
import org.LexGrid.LexBIG.Extensions.Load.options.OptionHolder;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * Class to load OWL files into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexGridLoaderImpl extends BaseLoader implements LexGrid_Loader {
    private static final long serialVersionUID = 5405545553067402760L;
    public final static String name = "LexGridLoader";
    private final static String description = "This loader loads LexGrid XML files into the LexGrid database.";

    public LexGridLoaderImpl() {
       super();
    }
    
    public void validate(URI uri, int validationLevel) throws LBParameterException {
        // Verify the file exists ...
        try {
            setInUse();
            
            // Verify content ...

            if (validationLevel == 0) {
                SAXParserFactory.newInstance().newSAXParser().parse(new File(uri), new DefaultHandler());
            } else if (validationLevel == 1) {
                // Load a WXS schema, represented by a Schema instance
                SAXParserFactory spf = SAXParserFactory.newInstance();
                spf.setNamespaceAware(true);
                spf.setValidating(true);
                SAXParser sp = spf.newSAXParser();
                sp.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                        "http://www.w3.org/2001/XMLSchema");
                sp.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", getSchemaURL().toString());
                DefaultHandler handler = new DefaultHandler() {
                    @Override
                    public void error(SAXParseException e) throws SAXException {
                        throw e;
                    }
                };
                sp.parse(new File(uri), handler);
            } else {
                throw new LBParameterException("Unsupported validation level");
            }
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new LBParameterException(e.toString());
        } finally {
            inUse = false;
        }
    }

    public void load(URI uri, boolean stopOnErrors, boolean async) throws LBParameterException, LBInvocationException {
      //
    }
    
    @Override
    protected OptionHolder declareAllowedOptions(OptionHolder holder) {
        return holder;
    }

    @Override
    protected URNVersionPair[] doLoad() throws CodingSchemeAlreadyLoadedException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(LexGridLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(LexGridLoaderImpl.class.getName());
        temp.setDescription(LexGridLoaderImpl.description);
        temp.setName(LexGridLoaderImpl.name);
        
        return temp;
    }

    public URI getSchemaURL() {
        try {
            return new URI("http://LexGrid.org/schema/" + getSchemaVersion() + "/LexGrid/service.xsd");
        } catch (URISyntaxException e) {
            getLogger().error("Unexpected Error", e);
            return null;
        }
    }

    public String getSchemaVersion() {
        return "2005/01";
    }

    public void finalize() throws Throwable {
        getLogger().loadLogDebug("Freeing LexGridLoaderImpl");
        super.finalize();
    }

}