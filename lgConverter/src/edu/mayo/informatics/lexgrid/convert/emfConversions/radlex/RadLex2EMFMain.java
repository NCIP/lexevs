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
package edu.mayo.informatics.lexgrid.convert.emfConversions.radlex;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.emf.base.xml.LgXMLResourceImpl;
import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;

/*
 * @author <A HREF="mailto:dks02@mayo.edu">Deepak Sharma</A> @author <A
 * HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 * 
 * @version subversion $Revision: 2917 $ checked in on $Date: 2006-06-19
 *          15:52:21 +0000 (Mon, 19 Jun 2006) $
 */

public class RadLex2EMFMain {
    public static void main(String[] args) {
        LgMessageDirectorIF logger = new CachingMessageDirectorImpl(new CommandLineMessageDirector("radlex2emfLogger"));

        if (args.length != 2) {
            String errorMsg = "Usage: RadLex2EMFMain  <Radlex pprj uri>  <target lexgrid xml file uri>"
                    + "\n\t target file name - place to put output XML.";
            System.out.println(errorMsg);
            logger.error(errorMsg);
            System.exit(0);
        }

        logger.info("Converting this ... \n");
        logger.info("Source: " + args[0] + "\nTarget: " + args[1]);
        logger.info("Started at " + (new java.util.Date()));

        try {

            URI input_uri = createURIFromString(args[0]);
            URI output_uri = new URI(args[1]);

            RadLex2EMFMain r2e = new RadLex2EMFMain();
            CodingScheme cst = r2e.map(input_uri, logger);

            r2e.writeLexGridXML(cst, output_uri, logger);
            System.out.println("Ended!!");
            logger.info("Conversion ended at " + (new java.util.Date()));
        } catch (Exception ex) {
            logger.fatal(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static URI createURIFromString(String str) throws Exception {
        String trimmed = str.trim();

        // Resolve to file, treating the string as either a
        // standard file path or URI.
        File f;
        if (!(f = new File(trimmed)).exists()) {
            // Check if we can find the file using the ClassLoader
            URL url = RadLex2EMFMain.class.getClassLoader().getResource(trimmed);
            if (url != null) {
                return url.toURI();
            } else {
                // Check if the string is a uri string
                f = new File(new URI(trimmed.replace(" ", "%20")));
                // If we can't find the filename, see if we can get it using the
                // ClassLoader
                if (!f.exists()) {
                    throw new FileNotFoundException();
                }
            }
        }
        // Accomodate embedded spaces ...
        return new URI(f.toURI().toString().replace(" ", "%20"));
    }

    public CodingScheme map(Project proj, LgMessageDirectorIF messages) {
        CodingScheme csclass = null;

        KnowledgeBase kb_ = null;
        if (proj != null) {
            kb_ = proj.getKnowledgeBase();
        }

        if ((proj != null) && (kb_ != null)) {
            try {
                RadLex2EMFStaticMapHolders RadlexCodeSys = new RadLex2EMFStaticMapHolders();
                csclass = RadlexCodeSys.getRadlexCodingScheme(kb_);

                if (csclass != null) {
                    RadLex2EMFDynamicMapHolders radlexAttribs = new RadLex2EMFDynamicMapHolders();
                    boolean processed = radlexAttribs.processRadlex(csclass, kb_, messages);

                    if (processed) 
                    {
                        radlexAttribs.populateSupportedProperties(csclass);
                        radlexAttribs.populateSupportedSources(csclass);
                        radlexAttribs.populateSupportedRepresentationalForms(csclass);
                        radlexAttribs.populateSupportedAssociations(csclass);
                        csclass.setApproxNumConcepts(radlexAttribs.getApproxNumberOfConcepts());
                    }
                }
            } catch (Exception e) {
                messages.error("Failed in Radlex Mapping...");
                e.printStackTrace();
            }
        }
        return csclass;
    }

    public CodingScheme map(URI inFileName, LgMessageDirectorIF messages) {
        Collection errors = new ArrayList();
        Project proj = Project.loadProjectFromURI(inFileName, errors);
        return map(proj, messages);
    }

    void writeLexGridXML(CodingScheme codingScheme, URI output_filename, LgMessageDirectorIF messages) {
        LgXMLResourceImpl xml = null;
        try {

            xml = new LgXMLResourceImpl(org.eclipse.emf.common.util.URI.createURI(output_filename.toURL().toString()));
            xml.getContents().add(codingScheme);

            // Perform the save ...
            xml.save();
        } catch (Exception e) {

            messages.fatal("Failed - " + e.toString() + " see log file.");

        }
    }
}