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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package edu.mayo.informatics.lexgrid.convert.directConversions.LgXMLCommon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.logging.Logger;

/**
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * 
 */
public class LexGridXMLProcessor {

    private static LgMessageDirectorIF messages_;

    private static final String CODING_SCHEME_ENTRY_POINT = "codingScheme";
    private static final String REVISION_ENTRY_POINT = "revision";
    private static final String SYSTEM_RELEASE_ENTRY_POINT = "systemRelease";

    private static final int ENTRY_POINT_NOT_FOUND = 0;
    private static final int CS_ENTRY_POINT_TYPE = 1;
    private static final int REV_ENTRY_POINT_TYPE = 2;
    private static final int SR_ENTRY_POINT_TYPE = 3;

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme loadCodingScheme(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        XMLDaoServiceAdaptor service = new XMLDaoServiceAdaptor();
        CodingScheme cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgCodingSchemeListener listener = new LgCodingSchemeListener();
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path));
            umr.setUnmarshalListener(listener);
            umr.setClass(CodingScheme.class);
            cs = (CodingScheme) umr.unmarshal(in);
            service.activateScheme(cs.getCodingSchemeURI(), cs.getRepresentsVersion());
            in.close();

        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (LBParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cs;

    }

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme loadRevision(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        XMLDaoServiceAdaptor service = new XMLDaoServiceAdaptor();
        Revision rev = null;
        CodingScheme cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgRevisionListener listener = new LgRevisionListener();
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path));
            umr.setUnmarshalListener(listener);
            umr.setClass(Revision.class);
            rev = (Revision) umr.unmarshal(in);
            cs = rev.getChangedEntry(0).getChangedCodingSchemeEntry();
            service.activateScheme(cs.getCodingSchemeURI(), cs.getRepresentsVersion());
            in.close();

        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (LBParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cs;

    }

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme loadSystemRelease(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        XMLDaoServiceAdaptor service = new XMLDaoServiceAdaptor();
        CodingScheme cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LexGridUnmarshalListener listener = new LexGridUnmarshalListener();
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path));
            umr.setUnmarshalListener(listener);
            umr.setClass(SystemRelease.class);
            SystemRelease release = (SystemRelease) umr.unmarshal(in);
            // Won't be returning a release with a coding scheme in it. Need to
            // find another way.
            cs = release.getCodingSchemes().getCodingScheme(0);
            service.activateScheme(cs.getCodingSchemeURI(), cs.getRepresentsVersion());
            in.close();

        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (LBParameterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cs;

    }

    /**
     * @param path
     * @return int representation of the Entry Point Type
     */
    private int getEntryPointType(String path) {
        BufferedReader in = null;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT) {
                    System.out.println(xmlStreamReader.getLocalName());
                    if (xmlStreamReader.getLocalName().equals(CODING_SCHEME_ENTRY_POINT)) {
                        return CS_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(REVISION_ENTRY_POINT)) {
                        return REV_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(SYSTEM_RELEASE_ENTRY_POINT)) {
                        return SR_ENTRY_POINT_TYPE;
                    }
                }
            }
            in.close();
            xmlStreamReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ENTRY_POINT_NOT_FOUND;
    }

    /**
     * @param path
     * @return boolean indicating if a coding scheme contains a property
     */
    private boolean setPropertiesFlag(String path) {
        BufferedReader in = null;
        boolean propsPresent = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {

                if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("properties")) {
                    System.out.println(xmlStreamReader.getLocalName());
                    propsPresent = true;
                    break;
                }
                if (event == XMLStreamConstants.START_ELEMENT)
                    System.out.println("Printing local name from stax: " + xmlStreamReader.getLocalName());
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propsPresent;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        messages_ = new Logger();
        System.out.println("Parsing content from " + args[0] + "...");

        try {
            System.out.println("entry point type: " + new LexGridXMLProcessor().getEntryPointType(args[0]));
        } catch (FactoryConfigurationError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
