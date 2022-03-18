
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.formatters;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.proxy.CastorProxy;
import org.castor.xml.XMLProperties;
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
            boolean useStreaming, boolean validate, LgMessageDirectorIF messager) {
        Marshaller marshaller = ns_marshaller;
        MarshalListener listener = null;
        if (useStreaming == true) {
            listener = new StreamingLexGridMarshalListener(marshaller, cng, cns, pageSize, messager);
        } else {
            listener = new LexGridMarshalListener(marshaller, cng, cns, pageSize);
        }
        try {
            marshaller.setValidation(validate);
            marshaller.setMarshalListener(listener);
            marshaller.setWriter(writer);
            marshaller.marshal(obj);
            writer.close(); // close the writer after the marshaling job done
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * This method exports the data in LexGRID XML format to a PipedWriter which can be read using Reader as it writes.     
     */
    public Reader marshalToXml(final Object obj, final CodedNodeGraph cng, final CodedNodeSet cns, final int pageSize,
            final boolean useStreaming, final boolean validate, final LgMessageDirectorIF messager) {
        final PipedReader in = new PipedReader();
        final Marshaller marshaller = ns_marshaller;
        try {
            new Thread(new Runnable() {
                PipedWriter out = new PipedWriter(in);
                
                public void run() {         
                    try {
                        MarshalListener listener = null;
                        if (useStreaming == true) {
                            listener = new StreamingLexGridMarshalListener(marshaller, cng, cns, pageSize, messager);
                        } else {
                            listener = new LexGridMarshalListener(marshaller, cng, cns, pageSize);
                        }                        
                        
                        marshaller.setValidation(validate);
                        marshaller.setMarshalListener(listener);
                        marshaller.setWriter(out);
                        marshaller.marshal(obj);
                        out.close(); // close the writer after the marshaling job done
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return in;
    }

}