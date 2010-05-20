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
package edu.mayo.informatics.indexer.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.Resource;

import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;

/**
 * This class reads and writes metadata to an xml file.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class MetaData {
    private Document document_;
    private Element root_;
    private File file_;
    private long fileChangeDate_;
    private long fileLength_;
    private boolean writing_ = false;
    private boolean reading_ = false;

    private final Logger logger = Logger.getLogger("Indexer.MetaData");
    
    public MetaData(Resource rootLocation) throws InternalErrorException, IOException {
        this(rootLocation.getFile());
    }

    public MetaData(File rootLocation) throws InternalErrorException {
        try {
            file_ = new File(rootLocation, "metadata.xml");
            WriteLockManager.instance(rootLocation);
            WriteLockManager.instance().lock();
            if (file_.exists()) {
                document_ = new SAXBuilder().build(file_);
                root_ = document_.getRootElement();
                fileChangeDate_ = file_.lastModified();
                fileLength_ = file_.length();
            } else {
                file_.createNewFile();
                document_ = new Document(new Element("IndexerServiceMetaData"));
                root_ = document_.getRootElement();
                writeFile(false);
            }
        } catch (JDOMException e) {
            throw new InternalErrorException("The existing metadata.xml file appears to be invalid.", e);
        } catch (IOException e) {
            throw new InternalErrorException("Cannot create the metadata.xml file", e);
        } finally {
            WriteLockManager.instance().unlock();
        }

    }

    /**
     * Only rereads if necessary (checks file timestamp
     */
    public void rereadFile(boolean releaseLockWhenDone) throws InternalErrorException {
        try {
            if (file_.lastModified() != fileChangeDate_ || file_.length() != fileLength_) {
                logger.debug("Reading updated index metadata.xml");
                WriteLockManager.instance().lock();

                synchronized (document_) {

                    while (writing_) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                    }
                    while (reading_) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            // noop
                        }
                    }
                    reading_ = true;
                    document_ = new SAXBuilder().build(file_);
                    root_ = document_.getRootElement();
                }

            }
        } catch (JDOMException e) {
            throw new InternalErrorException("The existing metadata.xml file appears to be invalid.", e);
        } catch (IOException e) {
            throw new InternalErrorException("Cannot read the metadata.xml file", e);
        } finally {
            reading_ = false;
            if (releaseLockWhenDone) {
                WriteLockManager.instance().unlock();
            }
        }
    }

    public String[] getIndexMetaDataKeys(String indexName) throws InternalErrorException {
        rereadFile(true);
        List list = root_.getChildren("index");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (((String) temp.getAttributeValue("name")).equals(indexName)) {
                return getIndexMetaDataKeys(temp);
            }
        }
        return new String[] {};
    }

    public String getIndexMetaDataValue(String indexName, String key) throws InternalErrorException {
        rereadFile(true);
        List list = root_.getChildren("index");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (((String) temp.getAttributeValue("name")).equals(indexName)) {
                return getIndexMetaDataValue(temp, key);
            }
        }
        return "";
    }

    public String[] getIndexMetaDataKeys() throws InternalErrorException {
        rereadFile(true);
        return getIndexMetaDataKeys(root_);
    }

    public String getIndexMetaDataValue(String key) throws InternalErrorException {
        rereadFile(true);
        return getIndexMetaDataValue(root_, key);
    }

    public void setIndexMetaDataValue(String key, String value) throws InternalErrorException {
        try {
            rereadFile(false);
            setIndexMetaDataValue(root_, key, value);
        } finally {
            WriteLockManager.instance().unlock();

        }

    }

    public void setIndexMetaDataValue(String indexName, String key, String value) throws InternalErrorException {
        try {
            rereadFile(false);
            List list = root_.getChildren("index");
            Iterator iter = list.iterator();
            boolean found = false;
            while (iter.hasNext()) {
                Element temp = (Element) iter.next();
                if (((String) temp.getAttributeValue("name")).equals(indexName)) {
                    setIndexMetaDataValue(temp, key, value);
                    found = true;
                }
            }
            if (!found) {
                Element temp = new Element("index");
                Attribute tempAttr = new Attribute("name", indexName);
                temp.setAttribute(tempAttr);

                root_.addContent(temp);
                writeFile(false);
                setIndexMetaDataValue(temp, key, value);
            }
        } finally {
            WriteLockManager.instance().unlock();
        }
    }

    public void removeIndexMetaDataValue(String key) throws InternalErrorException {
        try {
            rereadFile(false);
            removeIndexMetaDataValue(root_, key);
        } finally

        {
            WriteLockManager.instance().unlock();
        }
    }

    public void removeAllIndexMetaDataValue(String indexName) throws InternalErrorException {
        try {
            rereadFile(false);
            List list = root_.getChildren("index");
            ArrayList elementsToRemove = new ArrayList();
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Element temp = (Element) iter.next();
                if (((String) temp.getAttributeValue("name")).equals(indexName)) {
                    elementsToRemove.add(temp);
                }
            }

            // have to do this below to avoid concurrent modification exception
            for (int i = 0; i < elementsToRemove.size(); i++) {
                root_.removeContent((Element) elementsToRemove.get(i));
            }
            writeFile(false);
        } finally {
            WriteLockManager.instance().unlock();
        }
    }

    public void removeIndexMetaDataValue(String indexName, String key) throws InternalErrorException {
        try {
            rereadFile(false);
            List list = root_.getChildren("index");
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Element temp = (Element) iter.next();
                if (((String) temp.getAttributeValue("name")).equals(indexName)) {
                    removeIndexMetaDataValue(temp, key);
                }
            }
        } finally {
            WriteLockManager.instance().unlock();
        }

    }

    private String[] getIndexMetaDataKeys(Element element) throws InternalErrorException {
        ArrayList resultsToReturn = new ArrayList();
        List list = element.getChildren("note");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            resultsToReturn.add(temp.getAttribute("key").getValue());
        }
        return (String[]) resultsToReturn.toArray(new String[resultsToReturn.size()]);
    }

    private String getIndexMetaDataValue(Element element, String key) throws InternalErrorException {
        List list = element.getChildren("note");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (temp.getAttribute("key").getValue().equals(key)) {
                return temp.getAttribute("value").getValue();
            }

        }
        return "";
    }

    private void setIndexMetaDataValue(Element inElement, String inKey, String inValue) throws InternalErrorException {
        List list = inElement.getChildren("note");
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (temp.getAttribute("key").getValue().equals(inKey)) {
                temp.setAttribute("value", inValue);
                writeFile(false);
                return;
            }
        }

        Element element = new Element("note");
        Attribute key = new Attribute("key", inKey);
        Attribute value = new Attribute("value", inValue);

        element.setAttribute(key);
        element.setAttribute(value);
        inElement.addContent(element);
        writeFile(false);
    }

    private void removeIndexMetaDataValue(Element inElement, String inKey) throws InternalErrorException {
        List list = inElement.getChildren("note");
        Element[] foo = (Element[]) list.toArray(new Element[list.size()]);

        for (int i = 0; i < foo.length; i++) {

            Element temp = foo[i];
            if (temp.getAttribute("key").getValue().equals(inKey)) {
                inElement.removeContent(temp);
                writeFile(false);
            }
        }
    }

    private synchronized void writeFile(boolean unlockWhenDone) throws InternalErrorException {
        try {
            XMLOutputter xmlFormatter = new XMLOutputter(Format.getPrettyFormat());

            WriteLockManager.instance().lock();
            FileWriter writer = new FileWriter(file_);

            writing_ = true;
            while (reading_) {
                Thread.sleep(1);
            }
            logger.debug("Writing updated index metadata.xml");
            writer.write(xmlFormatter.outputString(document_));

            writer.close();

            fileChangeDate_ = file_.lastModified();
            fileLength_ = file_.length();
        } catch (Exception e) {
            throw new InternalErrorException("There was a problem writing the metadata file " + e);
        } finally {
            writing_ = false;
            if (unlockWhenDone) {
                WriteLockManager.instance().unlock();
            }
        }
    }

    public String getMetaLocation() throws IOException {
        return file_.getCanonicalPath();
    }
}