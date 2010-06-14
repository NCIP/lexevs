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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.valueSets.PickListDefinition;
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
    private static final String REVISION_CHANGE_AGENT = "changeAgent";
    private static final String REVISION_CHANGE_INTRUCTIONS = "changeIntructions";
    private static final String REVISION_CHANGED_ENTRY = "changedEntry";
    private static final String CODING_SCHEMES = "codingSchemes";
    private static final String VALUE_SETS = "valueSetDefinitions";
    private static final String PICK_LISTS = "pickListDefinitions";
    
    private static final int ENTRY_POINT_NOT_FOUND = 0;
    private static final int CS_ENTRY_POINT_TYPE = 1;
    private static final int REV_ENTRY_POINT_TYPE = 2;
    private static final int SR_ENTRY_POINT_TYPE = 3;
    private static final int VS_ENTRY_POINT_TYPE = 4;
    private static final int PL_ENTRY_POINT_TYPE = 5;
    public static final String NO_SCHEME_URL = "http://no.scheme.found";
    public static final String NO_SCHEME_VERSION = "0.0";
    public static final HashMap<CodingScheme, Boolean> systemCSProperties = null;
    public static final HashMap<CodingScheme, Boolean> systemRelationsProperties = null;
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
            listener.setLastMetaDataType(getLastRevisionElement(path, messages));
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
            LgSystemReleaseListener listener = 
                new LgSystemReleaseListener(messages, systemReleaseCodingSchemePropertiesSurvey(path, messages));
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            listener.setSystemReleaseMetaData(getSystemReleaseMetadata(path, messages));
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
        BufferedReader in = null;
        Unmarshaller umr = null;
        CodingScheme[] cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LgPickListListener listener = new LgPickListListener(messages);
            // default is true -- no need to set the validation flag if the user
            // wants to validate.
            if (!validateXML) {
                umr.setValidation(validateXML);
            }
            //listener.setPropertiesPresent(setPropertiesFlag(path, messages));
            umr.setUnmarshalListener(listener);
            umr.setClass(PickListDefinition.class);
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
            messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
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
   public boolean setPropertiesFlag(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        boolean propsPresent = false;
        boolean codingSchemePresent = false;
        boolean csPropsPresent = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT &&( xmlStreamReader.getLocalName().equals("codingScheme")
                        || xmlStreamReader.getLocalName().equals("changedCodingSchemeEntry"))) {
                 //   messages.info("Property metadata detected ... adjusting XML streaming to database");
                    codingSchemePresent = true;
                }
                if (codingSchemePresent && event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("properties")) {
                 //   messages.info("Property metadata detected ... adjusting XML streaming to database");
                    propsPresent = true;
                }
                if (codingSchemePresent && propsPresent && event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("entities")) {
                 //   messages.info("Property metadata detected ... adjusting XML streaming to database");
                    csPropsPresent = true;
                    break;
                }
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        return csPropsPresent;
    }
    
    
    /**
     * @param path
     * @return boolean indicating if a coding scheme contains a property
     */
    public boolean setRelationsPropertiesFlag(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        boolean relationsPresent = false;
        boolean relPropsPresent = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("relations")) {
                  //  messages.info("Property metadata detected ... adjusting XML streaming to database");
                    relationsPresent = true;
                }
                if (relationsPresent && event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("properties")) {
                  //  messages.info("Property metadata detected ... adjusting XML streaming to database");
                    //propsPresent = true;
                    relPropsPresent = true;
                    break;                }
//                if (relationsPresent && propsPresent && event == XMLStreamConstants.END_ELEMENT && xmlStreamReader.getLocalName().equals("relations")) {
//                  //  messages.info("Property metadata detected ... adjusting XML streaming to database");
//                    relPropsPresent = true;
//                    break;
//                }
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        return relPropsPresent;
    }
    
   /**
 * @param path represents a path to the xml file to load
 * @param messages
 * @return flag indicating there is a coding scheme element somewhere in this xml source
 */
public boolean isCodingSchemePresent(String path,  LgMessageDirectorIF messages) {
        BufferedReader in = null;
        boolean schemePresent = false;
        boolean entryStateRemove = false;
        XMLStreamReader xmlStreamReader;

        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT && 
                        (xmlStreamReader.getLocalName().equals("codingScheme") || 
                                xmlStreamReader.getLocalName().equals("changedCodingSchemeEntry"))) {
                  messages.info("This LexGrid XML contains a coding scheme elment");
                    schemePresent = true;}
                if(schemePresent && event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("entryState")){
                    for(int i = 0; i < xmlStreamReader.getAttributeCount(); i++){
                        //System.out.println(xmlStreamReader.getAttributeLocalName(i) + ": " + xmlStreamReader.getAttributeValue(i));
                        if(xmlStreamReader.getAttributeLocalName(i).equals("changeType")){
                            if(xmlStreamReader.getAttributeValue(i).equals("REMOVE")){
                                messages.info("This LexGrid XML contains a revision that will remove a coding scheme");
                                entryStateRemove = true;
                            }
                        }
                    }
                }
                if(schemePresent && entryStateRemove && event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("mappings"))
                { 
                    schemePresent = false;
                    break;}
                if(event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("mappings"))        
                { break;}               
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        } catch (IOException e) {
            messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
            e.printStackTrace();
        }
        System.out.println("Scheme Present: " + schemePresent);
        return schemePresent;
    }

/**
 * @param path
 * @param messages
 * @return int indicating which revision element occurs in the revision meta data
 * allowing user to get an accurate load of the meta data for Revision.
 */
public int getLastRevisionElement(String path,  LgMessageDirectorIF messages) {
    BufferedReader in = null;
    int lastMetaDataElement = -1;
    XMLStreamReader xmlStreamReader;

    try {
        in = new BufferedReader(new FileReader(path));

        xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

        for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                .next()) {

            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(REVISION_CHANGE_AGENT)) {
                lastMetaDataElement = 0;
            }
            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(REVISION_CHANGE_INTRUCTIONS)) {
                lastMetaDataElement = 1;
            }
            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(REVISION_CHANGED_ENTRY)) {
                lastMetaDataElement = 2;
                break;
            }
        }
        xmlStreamReader.close();
        in.close();
    } catch (XMLStreamException e) {
        messages.error("While streaming file at " + path + "an error occured");
        e.printStackTrace();
    } catch (FactoryConfigurationError e) {
        messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    } catch (IOException e) {
        messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    }
    return lastMetaDataElement;
}

/**
 * We are pre-processing the System Release Meta data to avoid having to load this at
 * the end of the System Release load.
 * @param path
 * @param messages
 * @return
 */
public SystemRelease getSystemReleaseMetadata(String path, LgMessageDirectorIF messages){
    BufferedReader in = null;
    XMLStreamReader xmlStreamReader;  
    SystemRelease systemRelease = new SystemRelease();
    try {
        in = new BufferedReader(new FileReader(path));

        xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

        for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                .next()) {
            if(event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("systemRelease")){
                for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                    if (xmlStreamReader.getAttributeLocalName(i).equals("releaseId")) {
                        systemRelease.setReleaseId(xmlStreamReader.getAttributeValue(i));
                    }
                    if (xmlStreamReader.getAttributeLocalName(i).equals("releaseURI")) {
                        systemRelease.setReleaseURI(xmlStreamReader.getAttributeValue(i));
                    }
                    if (xmlStreamReader.getAttributeLocalName(i).equals("releaseDate")) {
                  
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
                        Date date = formatter.parse(xmlStreamReader.getAttributeValue(i));
                        systemRelease.setReleaseDate(date);
                    }
                    if (xmlStreamReader.getAttributeLocalName(i).equals("releaseAgency")) {
                        systemRelease.setReleaseAgency(xmlStreamReader.getAttributeValue(i));
                    }
                    if (xmlStreamReader.getAttributeLocalName(i).equals("basedOnRelease")) {
                        systemRelease.setReleaseAgency(xmlStreamReader.getAttributeValue(i));
                    }
            }
        }
        
        xmlStreamReader.close();
        in.close();}
    } catch (XMLStreamException e) {
       messages.error("While streaming file at " + path + "an error occured");
        e.printStackTrace();
    } catch (FactoryConfigurationError e) {
       messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
        e.printStackTrace();
    } catch (FileNotFoundException e) {
       messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    } catch (IOException e) {
       messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    } catch (ParseException e) {
        messages.error("Problems parsing the system release date --- please check your source formatting for format dd-mm-yyyy");
        e.printStackTrace();
    }
    return systemRelease;
}

/**
 * This does preprocessing to indicate whether the current system release has
 * a given list of schemes, pick lists or value sets.
 * @param path
 * @param messages
 * @return
 */
public HashMap<String, Boolean> surveySystemRelease(String path,  LgMessageDirectorIF messages) {
    BufferedReader in = null;
    XMLStreamReader xmlStreamReader;
    
    //initialize to false
    HashMap<String, Boolean> systemReleaseSurvey = new HashMap<String, Boolean>();
    systemReleaseSurvey.put(CODING_SCHEMES,false);
    systemReleaseSurvey.put(VALUE_SETS,false);
    systemReleaseSurvey.put(PICK_LISTS,false);
    try {
        in = new BufferedReader(new FileReader(path));

        xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

        for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                .next()) {

            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(CODING_SCHEMES)) {
                systemReleaseSurvey.put(CODING_SCHEMES,Boolean.TRUE);
            }
            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(VALUE_SETS)) {
                systemReleaseSurvey.put(VALUE_SETS,Boolean.TRUE);
            }
            if (event == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals(PICK_LISTS)) {
                systemReleaseSurvey.put(PICK_LISTS,Boolean.TRUE);
                break;
            }
        }
        xmlStreamReader.close();
        in.close();
    } catch (XMLStreamException e) {
       messages.error("While streaming file at " + path + "an error occured");
        e.printStackTrace();
    } catch (FactoryConfigurationError e) {
       messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
        e.printStackTrace();
    } catch (FileNotFoundException e) {
       messages.error("Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    } catch (IOException e) {
      messages.error("IO Problem reading file at: " + (path == null? "path appears to be null": path));
        e.printStackTrace();
    }
    return systemReleaseSurvey;
}

    /**
     *  Surveying the xml of a system release to allow accurate streaming to the data base.  
     *  We need to know if optional properties are present in the coding scheme or it's relations
     *  As well monitor what schemes and revisions have been loaded.
     * @param path
     * @param messages
     * @return
     */
    public ArrayList<SystemReleaseSurvey> systemReleaseCodingSchemePropertiesSurvey(String path,
            LgMessageDirectorIF messages) {
        BufferedReader in = null;
        XMLStreamReader xmlStreamReader;
        boolean inCodingScheme = false;
        boolean propsPresent = false;
        boolean inRelations = false;
        boolean relPropsPresent = false;
        boolean entryStateSet = false;
        AbsoluteCodingSchemeVersionReference cs = null;
        String entryStateId = "NEW";
        ArrayList<SystemReleaseSurvey> systemReleaseSurvey = new ArrayList<SystemReleaseSurvey>();
        messages.info("Surveying for optional coding scheme elements");
        try {
            in = new BufferedReader(new FileReader(path));

            xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(in);

            for (int event = xmlStreamReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlStreamReader
                    .next()) {
                if (event == XMLStreamConstants.START_ELEMENT
                        && (xmlStreamReader.getLocalName().equals("codingScheme") || xmlStreamReader.getLocalName()
                                .equals("changedCodingSchemeEntry"))) {
                    inCodingScheme = true;

                     cs = new AbsoluteCodingSchemeVersionReference();
                    for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
                        if (xmlStreamReader.getAttributeLocalName(i).equals("codingSchemeURI")) {
                            cs.setCodingSchemeURN(xmlStreamReader.getAttributeValue(i));
                        }
                        if (xmlStreamReader.getAttributeLocalName(i).equals("representsVersion"))
                        { cs.setCodingSchemeVersion(xmlStreamReader.getAttributeValue(i));}
                    }
                }
                if((event == XMLStreamConstants.START_ELEMENT ) && inCodingScheme
                        && !entryStateSet && xmlStreamReader.getLocalName().equals("entryState")){
                    for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {

                        if (xmlStreamReader.getAttributeLocalName(i).equals("containingRevision"))
                        { entryStateId = xmlStreamReader.getAttributeValue(i);
                          entryStateSet = true;
                        }
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT && (xmlStreamReader.getLocalName().equals("relations"))) {

                    inRelations = true;
                }
                if (event == XMLStreamConstants.START_ELEMENT && (xmlStreamReader.getLocalName().equals("properties"))) {
                    messages.info("This LexGrid XML contains a coding scheme properties element");
                    propsPresent = true;
                }
                //Set relations properties to indicate properties will be present on this coding scheme.
                if (event == XMLStreamConstants.END_ELEMENT
                        && (xmlStreamReader.getLocalName().equals("relations"))) {
                    inRelations = false;
                }
                if (inRelations && (event == XMLStreamConstants.START_ELEMENT && (xmlStreamReader.getLocalName().equals("properties")))) {
                    System.out.println("This LexGrid XML contains a coding scheme relations properties element");
                    relPropsPresent = true;
                }
                if (event == XMLStreamConstants.END_ELEMENT
                        && (xmlStreamReader.getLocalName().equals("codingScheme") || xmlStreamReader.getLocalName()
                                .equals("changedCodingSchemeEntry"))) {
                    inCodingScheme = false;
                    SystemReleaseSurvey survey = new SystemReleaseSurvey();
                    survey.setCodingScheme(cs);
                    survey.setPropertiesPresent(propsPresent);
                    survey.setRelationsPropertiesPresent(relPropsPresent);
                    survey.setRevisionId(entryStateId);
                    systemReleaseSurvey.add(survey);
                    relPropsPresent = false;
                    propsPresent = false;
                    entryStateSet = false;
                }
            }
            xmlStreamReader.close();
            in.close();
        } catch (XMLStreamException e) {
            messages.error("While streaming file at " + path + "an error occured");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
         messages.error("While streaming file at " + path + "a streaming xml configuration error occured");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
          messages.error("Problem reading file at: " + (path == null ? "path appears to be null" : path));
            e.printStackTrace();
        } catch (IOException e) {
          messages.error("IO Problem reading file at: " + (path == null ? "path appears to be null" : path));
            e.printStackTrace();
        }
        return systemReleaseSurvey;
    }

public static void main (String[] args){
  //  LoadStatus ls = new LoadStatus();
  //  LgMessageDirectorIF messages = new CachingMessageDirectorImpl( new MessageDirector("Test XML",ls));
   LexGridXMLProcessor lp = new LexGridXMLProcessor();
   ArrayList<SystemReleaseSurvey> survey = null;
   //System.out.println("CodingSchemeProps: " + lp.setPropertiesFlag(args[0],null));
   //System.out.println("RelationsProps: " + lp.systemReleaseCodingSchemePropertiesSurvey(args[0],null));
  survey =  lp.systemReleaseCodingSchemePropertiesSurvey(args[0],null);
  for(SystemReleaseSurvey srs : survey){
      
      System.out.println(srs.toString());

  }
}
}
