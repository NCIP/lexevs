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

import org.LexGrid.LexBIG.Exceptions.LBUnsupportedOperationException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

/**
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 * 
 */
public class LexGridXMLProcessor {

    private static final String CODING_SCHEME_ENTRY_POINT = "codingScheme";
    private static final String REVISION_ENTRY_POINT = "revision";
    private static final String SYSTEM_RELEASE_ENTRY_POINT = "systemRelease";
    private static final String VALUE_SET_ENTRY_POINT = "valueSetDefinition";
    
    private static final String PICK_LIST_ENTRY_POINT = "pickListDefinition";
    
    private static final int ENTRY_POINT_NOT_FOUND = 0;
    private static final int CS_ENTRY_POINT_TYPE = 1;
    private static final int REV_ENTRY_POINT_TYPE = 2;
    private static final int SR_ENTRY_POINT_TYPE = 3;
    private static final int VS_ENTRY_POINT_TYPE = 4;
    private static final int PL_ENTRY_POINT_TYPE = 5;
    public static final String NO_SCHEME_URL = "http://no.scheme.found";
    public static final String NO_SCHEME_VERSION = "0.0";

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return Coding scheme loaded from this LexGrid xml.
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme[] loadCodingScheme(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        CodingScheme[] cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgCodingSchemeListener listener = new LgCodingSchemeListener(messages);
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path, messages));
            umr.setUnmarshalListener(listener);
            umr.setClass(CodingScheme.class);
            cs = new CodingScheme[]{(CodingScheme) umr.unmarshal(in)};
            //service.activateScheme(cs.getCodingSchemeURI(), cs.getRepresentsVersion());
            in.close();

        } catch (MarshalException e) {
            messages.error("the Coding Scheme Listener detected a reading or writing problem");
            e.printStackTrace();
        } catch (ValidationException e) {
            messages.error("Unmarshaller detected invalid xml at: " + path);
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        return cs;

    }

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return Set of coding schemes loaded as changed entry elements in this revision.
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme[] loadRevision(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        CodingScheme[] cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgRevisionListener listener = new LgRevisionListener(messages);
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path, messages));
            umr.setUnmarshalListener(listener);
            umr.setClass(Revision.class);
            umr.unmarshal(in);
            if(isCodingSchemePresent(path, messages)){
                cs = listener.getCodingSchemes();
                }
                else{
                    CodingScheme scheme = new CodingScheme();
                    scheme.setCodingSchemeURI(NO_SCHEME_URL);
                    scheme.setRepresentsVersion(NO_SCHEME_VERSION);
                    cs = new CodingScheme[]{scheme};
                }
            in.close();

        } catch (MarshalException e) {
            messages.error("the Revision Listener detected a reading or writing problem");
            e.printStackTrace();
        } catch (ValidationException e) {
            messages.error("Unmarshaller detected invalid xml at: " + path);
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
            }
        return cs;

    }

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return Set of coding schemes loaded wiith this system release.
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme[] loadSystemRelease(String path, LgMessageDirectorIF messages,
            boolean validateXML) throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        CodingScheme[] cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgSystemReleaseListener listener = new LgSystemReleaseListener(messages);
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setPropertiesPresent(setPropertiesFlag(path, messages));
            listener.setMessages_(messages);
            umr.setUnmarshalListener(listener);
            umr.setClass(SystemRelease.class);
            umr.unmarshal(in);
            if(isCodingSchemePresent(path, messages)){
            cs = listener.getCodingSchemes();
            }
            else{
                CodingScheme scheme = new CodingScheme();
                scheme.setCodingSchemeURI(NO_SCHEME_URL);
                scheme.setRepresentsVersion(NO_SCHEME_VERSION);
                cs = new CodingScheme[]{scheme};
            }
            in.close();

        } catch (MarshalException e) {
            messages.error("the System Release Listener detected a reading or writing problem");
            e.printStackTrace();
        } catch (ValidationException e) {
            messages.error("Unmarshaller detected invalid xml at: " + path);
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
            }
        return cs;

    }
    
    public org.LexGrid.codingSchemes.CodingScheme[] loadValueSetDefinition(String path, LgMessageDirectorIF messages,
            boolean validateXML){
        BufferedReader in = null;
        Unmarshaller umr = null;
        CodingScheme[] cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgValueSetListener listener = new LgValueSetListener(messages);
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            //listener.setPropertiesPresent(setPropertiesFlag(path, messages));
            umr.setUnmarshalListener(listener);
            umr.setClass(ValueSetDefinition.class);
            umr.unmarshal(in);
            if(isCodingSchemePresent(path, messages)){
            cs = listener.getCodingSchemes();
            }
            else{
                CodingScheme scheme = new CodingScheme();
                scheme.setCodingSchemeURI(NO_SCHEME_URL);
                scheme.setRepresentsVersion(NO_SCHEME_VERSION);
                cs = new CodingScheme[]{scheme};
            }
            in.close();

        } catch (MarshalException e) {
            messages.error("the Value Set Listener detected a reading or writing problem");
            e.printStackTrace();
        } catch (ValidationException e) {
            messages.error("Unmarshaller detected invalid xml at: " + path);
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
            }
        return cs;

    }
    public org.LexGrid.codingSchemes.CodingScheme[] loadPickListDefinition(String path, LgMessageDirectorIF messages,
            boolean validateXML){
        return null;
    }
    
    
    /**
     * @param path
     * @return int representation of the Entry Point Type
     */
    public int getEntryPointType(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));
            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if (xmlStreamReader.getLocalName().equals(CODING_SCHEME_ENTRY_POINT)) {
                        messages.info("Top level XML element and entry point: " + CODING_SCHEME_ENTRY_POINT);
                        return CS_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(REVISION_ENTRY_POINT)) {
                        messages.info("Top level XML element and entry point: " + REVISION_ENTRY_POINT);
                        return REV_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(SYSTEM_RELEASE_ENTRY_POINT)) {
                        messages.info("Top level XML element and entry point: " + SYSTEM_RELEASE_ENTRY_POINT);
                        return SR_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(VALUE_SET_ENTRY_POINT)) {
                        messages.info("Top level XML element and entry point: " + VALUE_SET_ENTRY_POINT);
                        return VS_ENTRY_POINT_TYPE;
                    } else if (xmlStreamReader.getLocalName().equals(PICK_LIST_ENTRY_POINT)) {
                        messages.info("Top level XML element and entry point: " + PICK_LIST_ENTRY_POINT);
                        return PL_ENTRY_POINT_TYPE;
                    }
                }
            }
            in.close();
            xmlStreamReader.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "an streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }

        return ENTRY_POINT_NOT_FOUND;
    }

    /**
     * @param path
     * @return boolean indicating if a coding scheme contains a property
     */
    private boolean setPropertiesFlag(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        boolean propsPresent = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {

                if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("properties")) {
                    messages.info("This coding scheme contains coding scheme property metadata ... adjusting XML streaming to database");
                    propsPresent = true;
                    break;
                }
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "an streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        return propsPresent;
    }
    
    
   /**
 * @param path represents a path to the xml file to load
 * @param messages
 * @return flag indicating there is a coding scheme element somewhere in this xml source
 */
private boolean isCodingSchemePresent(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        boolean schemePresent = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {

                if (event == XMLStreamConstants.START_ELEMENT && 
                        (xmlStreamReader.getLocalName().equals("codingScheme") || 
                                xmlStreamReader.getLocalName().equals("changedCodingSchemeEntry"))) {
                  messages.info("This LexGrid XML contains a coding scheme elment, preparing to load coding scheme");
                    schemePresent = true;
                    break;
                }
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "an streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        return schemePresent;
    }


}
