/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.codec.binary.Base64
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.dynabean.SqlDynaBean;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.io.ConverterConfiguration;
import org.apache.ddlutils.io.DataWriterException;
import org.apache.ddlutils.io.converters.ConversionException;
import org.apache.ddlutils.io.converters.SqlTypeConverter;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

public class DataWriter {
    private static final int MAX_ATTRIBUTE_LENGTH = 255;
    private static final String INDENT_STRING = "  ";
    private final Log _log = LogFactory.getLog(DataWriter.class);
    private ConverterConfiguration _converterConf = new ConverterConfiguration();
    private PrintWriter _output;
    private XMLStreamWriter _writer;
    private String _encoding;
    private boolean _prettyPrinting = true;

    public DataWriter(OutputStream output) throws DataWriterException {
        this(output, null);
    }

    public DataWriter(OutputStream output, String encoding) throws DataWriterException {
        this._output = new PrintWriter(output);
        this._encoding = encoding == null || encoding.length() == 0 ? "UTF-8" : encoding;
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            this._writer = factory.createXMLStreamWriter(output, this._encoding);
        }
        catch (XMLStreamException ex) {
            throw new DataWriterException(ex);
        }
    }

    public DataWriter(Writer output, String encoding) throws DataWriterException {
        this._output = new PrintWriter(output);
        this._encoding = encoding;
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            this._writer = factory.createXMLStreamWriter(this._output);
        }
        catch (XMLStreamException ex) {
            throw new DataWriterException(ex);
        }
    }

    public boolean isPrettyPrinting() {
        return this._prettyPrinting;
    }

    public void setPrettyPrinting(boolean prettyPrinting) {
        this._prettyPrinting = prettyPrinting;
    }

    public ConverterConfiguration getConverterConfiguration() {
        return this._converterConf;
    }

    private void printlnIfPrettyPrinting() throws DataWriterException {
        if (this._prettyPrinting) {
            try {
                this._writer.writeCharacters("\n");
            }
            catch (XMLStreamException ex) {
                throw new DataWriterException(ex);
            }
        }
    }

    private void indentIfPrettyPrinting(int level) throws DataWriterException {
        if (this._prettyPrinting) {
            try {
                int idx = 0;
                while (idx < level) {
                    this._writer.writeCharacters(INDENT_STRING);
                    ++idx;
                }
            }
            catch (XMLStreamException ex) {
                throw new DataWriterException(ex);
            }
        }
    }

    public void writeDocumentStart() throws DataWriterException {
        try {
            this._writer.writeStartDocument(this._encoding, "1.0");
            this.printlnIfPrettyPrinting();
            this._writer.writeStartElement("data");
            this.printlnIfPrettyPrinting();
        }
        catch (XMLStreamException ex) {
            throw new DataWriterException(ex);
        }
    }

    public void writeDocumentEnd() throws DataWriterException {
        try {
            this._writer.writeEndElement();
            this.printlnIfPrettyPrinting();
            this._writer.writeEndDocument();
            this._writer.flush();
            this._writer.close();
            this._output.close();
        }
        catch (XMLStreamException ex) {
            throw new DataWriterException(ex);
        }
    }

    public void write(SqlDynaBean bean) throws DataWriterException {
        SqlDynaClass dynaClass = (SqlDynaClass)bean.getDynaClass();
        Table table = dynaClass.getTable();
        HashMap<String, String> subElements = new HashMap<String, String>();
        try {
            this.indentIfPrettyPrinting(1);
            this._writer.writeStartElement(table.getName());
            int idx = 0;
            while (idx < table.getColumnCount()) {
                Column column = table.getColumn(idx);
                Object value = bean.get(column.getName());
                SqlTypeConverter converter = this._converterConf.getRegisteredConverter(table, column);
                String valueAsText = null;
                if (converter == null) {
                    if (value != null) {
                        valueAsText = value.toString();
                    }
                } else {
                    valueAsText = converter.convertToString(value, column.getTypeCode());
                }
                if (valueAsText != null) {
                    if (valueAsText.length() > 255 || this.analyzeText(valueAsText, null)) {
                        subElements.put(column.getName(), valueAsText);
                    } else {
                        this._writer.writeAttribute(column.getName(), valueAsText);
                    }
                }
                ++idx;
            }
            if (!subElements.isEmpty()) {
                ArrayList cutPoints = new ArrayList();
                for (Map.Entry entry : subElements.entrySet()) {
                    String content = entry.getValue().toString();
                    this.printlnIfPrettyPrinting();
                    this.indentIfPrettyPrinting(2);
                    this._writer.writeStartElement(entry.getKey().toString());
                    cutPoints.clear();
                    boolean writeBase64Encoded = this.analyzeText(content, cutPoints);
                    if (writeBase64Encoded) {
                        this._writer.writeAttribute("base64", "true");
                        this._writer.writeCData(new String(Base64.encodeBase64((byte[])content.getBytes())));
                    } else if (cutPoints.isEmpty()) {
                        this._writer.writeCData(content);
                    } else {
                        int lastPos = 0;
                        Iterator cutPointIt = cutPoints.iterator();
                        while (cutPointIt.hasNext()) {
                            int curPos = (Integer)cutPointIt.next();
                            this._writer.writeCData(content.substring(lastPos, curPos));
                            lastPos = curPos;
                        }
                        if (lastPos < content.length()) {
                            this._writer.writeCData(content.substring(lastPos));
                        }
                    }
                    this._writer.writeEndElement();
                }
                this.printlnIfPrettyPrinting();
                this.indentIfPrettyPrinting(1);
            }
            this._writer.writeEndElement();
            this.printlnIfPrettyPrinting();
        }
        catch (XMLStreamException ex) {
            throw new DataWriterException(ex);
        }
        catch (ConversionException ex) {
            throw new DataWriterException((Throwable)((Object)ex));
        }
    }

    private boolean analyzeText(String text, List cutPoints) {
        ArrayList<Integer> tmpCutPoints = cutPoints == null ? null : new ArrayList<Integer>();
        int numChars = text.length();
        int numFoundCDataEndChars = 0;
        int charPos = 0;
        while (charPos < numChars) {
            char c = text.charAt(charPos);
            if (c < ' ' && c != '\n' && c != '\r' && c != '\t') {
                return true;
            }
            if (cutPoints != null) {
                if (c == ']' && (numFoundCDataEndChars == 0 || numFoundCDataEndChars == 1)) {
                    ++numFoundCDataEndChars;
                } else if (c == '>' && numFoundCDataEndChars == 2) {
                    tmpCutPoints.add(new Integer(charPos));
                    numFoundCDataEndChars = 0;
                } else {
                    numFoundCDataEndChars = 0;
                }
            }
            ++charPos;
        }
        if (cutPoints != null) {
            cutPoints.addAll(tmpCutPoints);
        }
        return false;
    }

    public void write(Iterator beans) throws DataWriterException {
        while (beans.hasNext()) {
            DynaBean bean = (DynaBean)beans.next();
            if (bean instanceof SqlDynaBean) {
                this.write((SqlDynaBean)bean);
                continue;
            }
            this._log.warn((Object)("Cannot write normal dyna beans (type: " + bean.getDynaClass().getName() + ")"));
        }
    }

    public void write(Collection beans) throws DataWriterException {
        this.write(beans.iterator());
    }
}

