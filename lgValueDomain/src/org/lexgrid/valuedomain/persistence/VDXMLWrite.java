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
package org.lexgrid.valuedomain.persistence;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.emf.base.xml.LgXMLResourceImpl;
import org.LexGrid.emf.codingSchemes.CodingschemesPackage;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.ValuedomainsPackage;
import org.LexGrid.messaging.LgMessageDirectorIF;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.impl.LexEVSValueDomainServicesImpl;

import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLWrite;
/**
 * Writes XML -> EMF
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VDXMLWrite extends XMLWrite {
    private String xmlFolderLocation;
    private static Logger log = Logger.getLogger("convert.XMLReadWrite");
    private LgMessageDirectorIF messages_;
    private boolean overwrite_;

    public VDXMLWrite(String xmlFolderLocation, boolean overwrite, boolean failOnAllErrors, LgMessageDirectorIF messages) {
    	super(xmlFolderLocation, overwrite, failOnAllErrors, messages);
        this.xmlFolderLocation = xmlFolderLocation;
        this.messages_ = messages;
        this.overwrite_ = overwrite;
        // TODO do something useful with the failOnAllErrors flag.
    }

    public void writeValueDomain(ValueDomainDefinition vdDef) throws Exception {
        LgXMLResourceImpl xml = null;
        try {
        	String fileName = StringUtils.isNotEmpty(vdDef.getValueDomainName()) ? vdDef.getValueDomainName() : vdDef.getValueDomainURI();
            String fileLocation = xmlFolderLocation + System.getProperty("file.separator")
                    + fileName + ".xml";

            messages_.info("Writing to the file '" + fileLocation + "'");

            File temp = new File(fileLocation);
            if (temp.exists() && !overwrite_) {
                messages_
                        .fatalAndThrowException("The output file already exists, and you didn't select the overwrite option.");
            }

            xml = new LgXMLResourceImpl(URI.createFileURI(fileLocation));
            xml.getContents().add(vdDef);
            

            // Perform the save ...
            xml.save();
        }

        catch (Exception e) {
            log.error("Failed...", e);
            messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
        } finally {
            if (xml != null) {
                xml.unload();
            }
        }
    }
    
    public static void main(String[] args){
    	ExportStatus status = new ExportStatus();
    	status.setState(ProcessState.PROCESSING);
        status.setStartTime(new Date(System.currentTimeMillis()));
        
    	VDXMLWrite xmlWrite = new VDXMLWrite("c:\\temp\\", true, true, new MessageDirector("VDExport", status));
    	LexEVSValueDomainServices vds = new LexEVSValueDomainServicesImpl();
    	try {
			ValueDomainDefinition vdDef = vds.getValueDomainDefinition(new java.net.URI("SRITEST:AUTO:EveryThing"));
			xmlWrite.writeValueDomain(vdDef);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}