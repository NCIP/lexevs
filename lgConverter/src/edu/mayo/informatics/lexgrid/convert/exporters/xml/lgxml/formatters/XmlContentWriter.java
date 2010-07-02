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
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters;

import java.io.IOException;
import java.io.Writer;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.proxy.CastorProxy;
import org.castor.xml.XMLProperties;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners.LexGridMarshalListener;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners.StreamingLexGridMarshalListener;

/**
 * The Class AbstractXmlMessageBodyWriter.
 * 
 * Adds common XML marshalling functionality to the AbstractMessageBodyWriter.
 */
// public abstract class AbstractXmlContentWriter extends
// AbstractMessageBodyWriter {
public class XmlContentWriter {

    /** The namespace cognizant marshaller. */
    private Marshaller ns_marshaller;
    {
        ns_marshaller = new Marshaller();
        ns_marshaller.setProperty(XMLProperties.USE_INDENTATION, "true");
        ns_marshaller.setMarshalAsDocument(true);
        ns_marshaller.setMarshalExtendedType(false);
        ns_marshaller.setSuppressNamespaces(false);
        ns_marshaller.setSchemaLocation(LexGridConstants.lgSchemaLocation); // mct
        ns_marshaller.setSupressXMLDeclaration(true);
        ns_marshaller.setSuppressXSIType(false);
        ns_marshaller.setValidation(true);
        // ns_marshaller.setEncoding("UTF-8");
        ns_marshaller.setProperty(XMLProperties.PROXY_INTERFACES, CastorProxy.class.getCanonicalName());
        ns_marshaller.setNamespaceMapping("lgBuiltin", LexGridConstants.lgBuiltin);
        ns_marshaller.setNamespaceMapping("lgCommon", LexGridConstants.lgCommon);
        ns_marshaller.setNamespaceMapping("lgCon", LexGridConstants.lgCon);
        ns_marshaller.setNamespaceMapping("lgCS", LexGridConstants.lgCS);
        ns_marshaller.setNamespaceMapping("lgNaming", LexGridConstants.lgNaming);
        ns_marshaller.setNamespaceMapping("lgRel", LexGridConstants.lgRel);
        ns_marshaller.setNamespaceMapping("lgVD", LexGridConstants.lgVD);
        ns_marshaller.setNamespaceMapping("lgVer", LexGridConstants.lgVer);
        ns_marshaller.setNamespaceMapping("xsi", LexGridConstants.lgXSI); // mct
        ns_marshaller.setInternalContext(ns_marshaller.getInternalContext());

    }

    public void marshalToXml(Object obj, CodedNodeSet cns, Writer writer, int pageSize) {
        Marshaller marshaller = ns_marshaller;
        LexGridMarshalListener listener = new LexGridMarshalListener(marshaller, cns, pageSize);
        try {
            marshaller.setMarshalListener(listener);
            marshaller.setWriter(writer);
            marshaller.marshal(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void marshalToXml(Object obj, CodedNodeGraph cng, Writer writer, int pageSize) {
        Marshaller marshaller = ns_marshaller;
        LexGridMarshalListener listener = new LexGridMarshalListener(marshaller, cng, pageSize);
        try {
            marshaller.setMarshalListener(listener);
            marshaller.setWriter(writer);
            marshaller.marshal(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * public void marshalToXml(Object obj, CodedNodeGraph cng, CodedNodeSet
     * cns, Writer writer, int pageSize) { Marshaller marshaller =
     * ns_marshaller; LexGridMarshalListener listener = new
     * LexGridMarshalListener(marshaller, cng, cns, pageSize); try {
     * marshaller.setMarshalListener(listener); marshaller.setWriter(writer);
     * marshaller.marshal(obj); } catch (Exception e) { throw new
     * RuntimeException(e); } }
     */
    public void marshalToXml(Object obj, CodedNodeGraph cng, CodedNodeSet cns, Writer writer, int pageSize,
            boolean useStreaming, LgMessageDirectorIF messager) {
        Marshaller marshaller = ns_marshaller;
        MarshalListener listener = null;
        if (useStreaming == true) {
            listener = new StreamingLexGridMarshalListener(marshaller, cng, cns, pageSize, messager);
        } else {
            listener = new LexGridMarshalListener(marshaller, cng, cns, pageSize);
        }
        try {
            marshaller.setMarshalListener(listener);
            marshaller.setWriter(writer);
            marshaller.marshal(obj);
            writer.close(); // close the writer after the marshaling job done
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
