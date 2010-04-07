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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.logging.Logger;

/**
 * @author  <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer </A>
 *
 */

public class XMLUnmarshaller {

    private static LgMessageDirectorIF messages_;

    /**
     * @param path
     * @param messages
     * @param validateXML
     * @return
     * @throws CodingSchemeAlreadyLoadedException
     */
    public org.LexGrid.codingSchemes.CodingScheme load(String path, LgMessageDirectorIF messages, boolean validateXML)
            throws CodingSchemeAlreadyLoadedException {
        BufferedReader in = null;
        Unmarshaller umr = null;
        XMLDaoServiceAdaptor service = new XMLDaoServiceAdaptor();
        CodingScheme cs = null;

        try {

            in = new BufferedReader(new FileReader(path));
            umr = new Unmarshaller();
            LexGridUnmarshalListener listener = new LexGridUnmarshalListener();
            //default is true -- no need to set the validation flag if the user wants to validate.
                       if(!validateXML){
                umr.setValidation(validateXML);}
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
    
//	public static void main(String[] args) throws Exception {
//		messages_ = new Logger();
//		System.out.println("Loading content from " + args[0] + "...");
//		new XMLUnmarshaller().load(args[0], messages_, false );
//
//	}
}
