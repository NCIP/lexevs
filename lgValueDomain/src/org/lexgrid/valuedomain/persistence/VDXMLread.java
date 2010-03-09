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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.LexOnt.CodingSchemeManifest;
import org.LexGrid.emf.base.xml.LgXMLResourceImpl;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.valueDomains.impl.PickListDefinitionImpl;
import org.LexGrid.emf.valueDomains.impl.PickListsImpl;
import org.LexGrid.emf.valueDomains.impl.ValueDomainDefinitionImpl;
import org.LexGrid.emf.valueDomains.impl.ValueDomainsImpl;
import org.LexGrid.emf.versions.SystemRelease;
import org.LexGrid.emf.versions.impl.SystemReleaseImpl;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.lexgrid.convert.emfConversions.XMLRead;
import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.InputFormatInterface;
import edu.mayo.informatics.lexgrid.convert.formats.inputFormats.LexGridXML;

/**
 * <pre>
 *        Title:        VDXMLread.java
 *        Description:  Class that handles reading system release, value domains and pick lists from XML file that is in LexGrid format.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VDXMLread extends XMLRead {
	
	private static Logger log = Logger.getLogger("convert.XMLReadWrite");
    private LgMessageDirectorIF messages_;
    private String xmlFileLocation_;
    private InputStream inputStream_;
    private Mappings vdMappings_;
    private SystemRelease SystemRelease_;
    protected InputFormatInterface in_;
    
	public VDXMLread(InputStream inputStream, LgMessageDirectorIF messages,
			boolean failOnAllErrors) {
		super(inputStream, messages, failOnAllErrors);
		this.messages_ = messages;
		inputStream_ = inputStream;
	}
	
	public VDXMLread(String xmlFileLocation, CodingSchemeManifest codingSchemeManifest, LgMessageDirectorIF messages,
            boolean failOnAllErrors) {
        // TODO failOnAllErrors should be used.
        super(xmlFileLocation, codingSchemeManifest, messages, failOnAllErrors);
        this.messages_ = messages;
        xmlFileLocation_ = xmlFileLocation;
    }
	
	public ValueDomainDefinition[] readAllValueDomains() throws LBParameterException {
        LgXMLResourceImpl xml = null;
        try {
            if (xmlFileLocation_ != null) {
                xml = new LgXMLResourceImpl(URI.createFileURI(xmlFileLocation_));
                xml.load();
            } else if (inputStream_ != null) {
                xml = new LgXMLResourceImpl();
                xml.doLoad(inputStream_, xml.getDefaultLoadOptions());
            } else {
                throw new LBParameterException("User error");
            }

            if (xml.getContents().get(0) instanceof ValueDomainDefinitionImpl) {
                return new ValueDomainDefinition[] { (ValueDomainDefinitionImpl) xml.getContents().get(0) };
            } else if (xml.getContents().get(0) instanceof ValueDomainsImpl) {
            	ValueDomainsImpl temp = (ValueDomainsImpl) xml.getContents().get(0);
            	
            	setVdMappings(temp.getMappings());
                
                List<ValueDomainDefinition> allVDS = new ArrayList<ValueDomainDefinition>();

                Iterator<ValueDomainDefinition> vds = temp.getValueDomainDefinition().iterator();
                while (vds.hasNext()) {
                	ValueDomainDefinition vd = (ValueDomainDefinition) vds.next();
                    allVDS.add((ValueDomainDefinition) vd);
                }
                
                return (ValueDomainDefinition[]) allVDS.toArray(new ValueDomainDefinition[allVDS.size()]);

            } else if (xml.getContents().get(0) instanceof SystemReleaseImpl) {
            	SystemRelease_ = (SystemReleaseImpl) xml.getContents().get(0);
            	
            	setVdMappings(SystemRelease_.getValueDomains().getMappings());
            	
                List<ValueDomainDefinition> allVDS = new ArrayList<ValueDomainDefinition>();

                Iterator<ValueDomainDefinition> vds = SystemRelease_.getValueDomains().getValueDomainDefinition().iterator();
                while (vds.hasNext()) {
                	ValueDomainDefinition vd = (ValueDomainDefinition) vds.next();
                    allVDS.add((ValueDomainDefinition) vd);
                }

                return (ValueDomainDefinition[]) allVDS.toArray(new ValueDomainDefinition[allVDS.size()]);

            } else {
                return new ValueDomainDefinition[] {};
            }
        }

        catch (Exception e) {
            log.error("Failed...", e);
            try {
				messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
			} catch (Exception e1) {
				throw new LBParameterException("Failed - " + e1.toString() + " see log file.");
			}
            return null;
        }
    }
	
	public PickListDefinition[] readAllPickLists() throws LBParameterException {
        LgXMLResourceImpl xml = null;
        try {
            if (xmlFileLocation_ != null) {
                xml = new LgXMLResourceImpl(URI.createFileURI(xmlFileLocation_));
                xml.load();
            } else if (inputStream_ != null) {
                xml = new LgXMLResourceImpl();
                xml.doLoad(inputStream_, xml.getDefaultLoadOptions());
            } else {
                throw new LBParameterException("User error");
            }

            if (xml.getContents().get(0) instanceof PickListDefinitionImpl) {
                return new PickListDefinition[] { (PickListDefinitionImpl) xml.getContents().get(0) };
            } else if (xml.getContents().get(0) instanceof PickListsImpl) {
            	PickListsImpl temp = (PickListsImpl) xml.getContents().get(0);
            	
            	setVdMappings(temp.getMappings());
                
                List<PickListDefinition> allPickLists = new ArrayList<PickListDefinition>();

                Iterator<PickListDefinition> plItr = temp.getPickListDefinition().iterator();
                while (plItr.hasNext()) {
                	PickListDefinition pickList = (PickListDefinition) plItr.next();
                    allPickLists.add((PickListDefinition) pickList);
                }
                
                return (PickListDefinition[]) allPickLists.toArray(new PickListDefinition[allPickLists.size()]);

            } else if (xml.getContents().get(0) instanceof SystemReleaseImpl) {
            	SystemRelease_ = (SystemReleaseImpl) xml.getContents().get(0);
            	
            	setVdMappings(SystemRelease_.getPickLists().getMappings());
            	
                List<PickListDefinition> allPickLists = new ArrayList<PickListDefinition>();

                Iterator<PickListDefinition> plItr = SystemRelease_.getPickLists().getPickListDefinition().iterator();
                while (plItr.hasNext()) {
                	PickListDefinition pickList = (PickListDefinition) plItr.next();
                    allPickLists.add((PickListDefinition) pickList);
                }

                return (PickListDefinition[]) allPickLists.toArray(new PickListDefinition[allPickLists.size()]);

            } else {
                return new PickListDefinition[] {};
            }
        }

        catch (Exception e) {
            log.error("Failed...", e);
            try {
				messages_.fatalAndThrowException("Failed - " + e.toString() + " see log file.", e);
			} catch (Exception e1) {
				throw new LBParameterException("Failed - " + e1.toString() + " see log file.");
			}
            return null;
        }
    }
	
	public void validate(java.net.URI uri, int validationLevel) throws LBParameterException {
        // Verify the file exists ...
        try {
            try {
                in_ = new LexGridXML(getStringFromURI(uri), null);
                in_.testConnection();
            } catch (ConnectionFailure e) {
                throw new LBParameterException("The LexGrid XML file path appears to be invalid - " + e);
            }
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
        } 
    }
	
	public String getStringFromURI(java.net.URI uri) throws LBParameterException {
        if ("file".equals(uri.getScheme()))

        {
            File temp = new File(uri);
            return temp.getAbsolutePath();
        } else {
            throw new LBParameterException("Currently only supports file based URI's", "uri", uri.toString());
        }

    }

	public java.net.URI getSchemaURL() {
        try {
            return new java.net.URI("http://LexGrid.org/schema/2009/01/LexGrid/versions.xsd");
        } catch (URISyntaxException e) {
            return null;
        }
    }
	
	public Mappings getVdMappings() {
		return vdMappings_;
	}

	public void setVdMappings(Mappings vdMappings) {
		this.vdMappings_ = vdMappings;
	}

	/**
	 * @return the systemRelease
	 */
	public SystemRelease getSystemRelease() {
		return SystemRelease_;
	}
}