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
package org.lexevs.dao.index.metadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.lexevs.logging.LoggerFactory;

/**
 * MetaData Loader for OBO MetaData XML Files.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class OBOMetaDataLoader extends BaseMetaDataLoader {
    private HashSet<String> propertyNames_ = new HashSet<String>();

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public OBOMetaDataLoader() throws Exception {
    	this(	false,
    			true,
    			true,
    			true);
    }

    public OBOMetaDataLoader(
            boolean addNormFields, boolean addDoubleMetaphoneFields, boolean addStemFields, boolean useCompoundFile) throws Exception {
        super();
        normEnabled_ = addNormFields;
        doubleMetaphoneEnabled_ = addDoubleMetaphoneFields;
        stemmingEnabled_ = addStemFields;
        useCompoundFile_ = useCompoundFile;

       
    }
    
    public void loadMetadata(String codingSchemeUri, String version, URI metaDataLocation, boolean appendToExisting) throws Exception {
    	 try {
             // TODO there is a potential threading issue here - can't have two
             // obo loaders
             // both populating this index at the same time.
             if (appendToExisting) {
                 openIndex(codingSchemeUri, version);
             } else {
                 removeMeta(codingSchemeUri, version);
                 openIndex(codingSchemeUri, version);
             }

             loadContent(metaDataLocation);

             closeIndexes();
         } catch (Exception e) {
             String id = getLogger().error("Problem indexing the OBO MetaData", e);
             throw new LBInvocationException("There was a problem indexing the OBO MetaData", id);
         }
    }

    @SuppressWarnings("unchecked")
    private void loadContent(URI metaDataLocation) throws Exception {
        BufferedReader reader = null;
        if (metaDataLocation.getScheme().equals("file")) {
            reader = new BufferedReader(new FileReader(new File(metaDataLocation)));
        } else {
            reader = new BufferedReader(new InputStreamReader(metaDataLocation.toURL().openConnection()
                    .getInputStream()));
        }

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(reader);

        Element root = document.getRootElement();

        ArrayList<String> parentPath = new ArrayList<String>();

        String name = root.getName();
        propertyNames_.add(name);
        String value = root.getTextTrim();
        parentPath.add(name);

        if (value != null && value.length() > 0) {
            addProperty(null, name, value);
        }

        processAttributes(parentPath, root.getAttributes());
        processChildren(parentPath, root.getChildren());

    }

    private void processAttributes(ArrayList<String> parentPath, List<Attribute> attributeList) throws Exception {
        for (Iterator<Attribute> attIter = attributeList.iterator(); attIter.hasNext();) {
            Attribute currentAttribute = (Attribute) attIter.next();

            String name = currentAttribute.getName();
            String value = currentAttribute.getValue();
            addProperty(parentPath, name, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void processChildren(ArrayList<String> parentPath, List<Element> elementList) throws Exception {
        for (Iterator<Element> eleIter = elementList.iterator(); eleIter.hasNext();) {
            Element element = (Element) eleIter.next();

            String name = element.getName();
            propertyNames_.add(name);
            String value = element.getTextTrim();

            if (value != null && value.length() > 0) {
                addProperty(parentPath, name, value);
            }

            ArrayList<String> newParentPath = new ArrayList<String>(parentPath);
            newParentPath.add(name);
            processAttributes(newParentPath, element.getAttributes());
            processChildren(newParentPath, element.getChildren());
        }
    }
}