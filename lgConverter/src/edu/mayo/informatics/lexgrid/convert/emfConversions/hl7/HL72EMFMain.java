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
package edu.mayo.informatics.lexgrid.convert.emfConversions.hl7;

import org.LexGrid.LexBIG.Preferences.loader.LoadPreferences.LoaderPreferences;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.HL7SQL;

/**
 * Wrapper class that kicks off the main functionality of the loading class
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * 
 */
public class HL72EMFMain {

    private LgMessageDirectorIF messages;
    private String access_URL = null;
    private LoaderPreferences loaderPrefs = null; // CRS

    public CodingScheme map(String codingSchemeURN, String accessPath, boolean failOnAllErrors,
            LgMessageDirectorIF lg_messages) throws Exception {
        this.messages = new CachingMessageDirectorImpl(lg_messages);

        if (accessPath == null) {
            messages.fatalAndThrowException("Error! Input file path string is null.");
        } else {
            access_URL = HL7SQL.MSACCESS_SERVER + accessPath;
        }
        CodingScheme csclass = null;

        try {
            csclass = new CodingScheme();
            HL7MapToLexGrid hl7Map = new HL7MapToLexGrid(codingSchemeURN, access_URL, HL7SQL.MSACCESS_DRIVER, messages);
            hl7Map.setLoaderPrefs(loaderPrefs); // CRS
            hl7Map.initRun(csclass);
            messages.info("Processing DONE!!");
        } catch (Exception e) {
            messages.fatalAndThrowException("Failed to load HL7 Content from: " + accessPath + "", e);
        }

        return csclass;

    }

    public void setLoaderPrefs(LoaderPreferences loaderPrefs) {
        this.loaderPrefs = loaderPrefs;
    }

}