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
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Load.Text_Loader;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridDelimitedText;

/**
 * Class to load a Text files into the LexBIG API.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TextLoaderImpl extends BaseLoader implements Text_Loader {
    private static final long serialVersionUID = -4164433097047462000L;
    public static final String name = "TextLoader";
    private static final String description = "This loader loads LexGrid Text files into the LexGrid database.";

    public TextLoaderImpl() {
        super.name_ = TextLoaderImpl.name;
        super.description_ = TextLoaderImpl.description;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription temp = new ExtensionDescription();
        temp.setExtensionBaseClass(TextLoaderImpl.class.getInterfaces()[0].getName());
        temp.setExtensionClass(TextLoaderImpl.class.getName());
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

    public void validate(URI uri, Character delimiter, boolean triplesFormat, int validationLevel)
            throws LBParameterException {
        try {
            setInUse();
            in_ = new LexGridDelimitedText(getStringFromURI(uri));
            in_.testConnection();
        } catch (ConnectionFailure e) {
            throw new LBParameterException("The Text file path appears to be invalid - " + e);
        } catch (LBInvocationException e) {
            throw new LBParameterException(
                    "A loader can only do one task at a time.  Create a new loader to do additional tasks");
        } finally {
            inUse = false;
        }
    }

    public void load(URI uri, Character delimiter, boolean readDoublesAsTriples, boolean stopOnErrors, boolean async)
            throws LBParameterException, LBInvocationException {
        setInUse();
        try {
            in_ = new LexGridDelimitedText(getStringFromURI(uri));
            in_.testConnection();
        } catch (ConnectionFailure e) {
            inUse = false;
            throw new LBParameterException("The LexGrid Text file path appears to be invalid - " + e);
        }
        status_ = new LoadStatus();
        status_.setLoadSource(getStringFromURI(uri));

        options_.add(new Option(Option.FAIL_ON_ERROR, new Boolean(stopOnErrors)));
        String delimiterVal = "";
        if (delimiter != null) {
            delimiterVal = delimiter.toString();
        }
        options_.add(new Option(Option.DELIMITER, delimiterVal));
        options_.add(new Option(Option.FORCE_FORMAT_B, new Boolean(readDoublesAsTriples)));

        baseLoad(async);
    }
}